package me.ezzattharwat.vacomputing.ui.calc

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.android.synthetic.main.add_schedule_time_dialog.*
import kotlinx.android.synthetic.main.fragment_calc.*
import me.ezzattharwat.vacomputing.R
import me.ezzattharwat.vacomputing.data.Resource
import me.ezzattharwat.vacomputing.data.db.EquationEntity
import me.ezzattharwat.vacomputing.di.ComponentProvider
import me.ezzattharwat.vacomputing.di.activity.ActivityComponent
import me.ezzattharwat.vacomputing.di.application.AppViewModelFactory
import me.ezzattharwat.vacomputing.di.fragment.FragmentModule
import me.ezzattharwat.vacomputing.other.Constant.ACTION_START_SERVICE
import me.ezzattharwat.vacomputing.other.Constant.EQUATION_ID
import me.ezzattharwat.vacomputing.other.Constant.MATH_EQUATION_SCHEDULED_TIME
import me.ezzattharwat.vacomputing.other.Constant.REQUEST_CODE_LOCATION_PERMISSION
import me.ezzattharwat.vacomputing.other.MyEasyPermissions.hasLocationPermissions
import me.ezzattharwat.vacomputing.service.MathEngineService
import me.ezzattharwat.vacomputing.ui.AppViewModel
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import javax.inject.Inject

class CalcFragment : Fragment(R.layout.fragment_calc), View.OnClickListener,
    EasyPermissions.PermissionCallbacks{

    private val operands = listOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", ".")

    private lateinit var appViewModel : AppViewModel

    @Inject
    lateinit var factory: AppViewModelFactory

    @Inject
    lateinit var mFusedLocationProviderClient:FusedLocationProviderClient
    @Inject
    lateinit var geocoder: Geocoder

    private var timeToScheduled: Int = 0
    private var currentAddress: String  = ""


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ComponentProvider<*>) {
            val provider = context as ComponentProvider<*>
            if (provider.getComponent() is ActivityComponent) {
                (provider.getComponent() as ActivityComponent)
                    .plus(FragmentModule(this))
                    .inject(this)
            } else {
                throw IllegalStateException("Component must be " + ActivityComponent::class.java.name)
            }
        } else {
            throw IllegalStateException("host context must implement " + ComponentProvider::class.java.name)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initViewModel()
        requestPermissions()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        registerButtonClick()
        observeOnInsertedID()

        super.onViewCreated(view, savedInstanceState)
    }

    private fun initViewModel(){
        appViewModel = ViewModelProvider(this, factory).get(AppViewModel::class.java)
    }

    private fun registerButtonClick() {
        (layoutButtonHolder as ViewGroup).children.forEach {
            it.setOnClickListener(this)
        }
    }

    override fun onClick(v: View?) {

        // Every button must have a tag
        if (v == null || v.tag == null) {
            return
        }
        when (val tag = v.tag.toString()) {
            "del" -> {
                // Delete last character from input
                val oldValue = readInputValue()
                if (oldValue.isNotEmpty()) {
                    editTextInput.setText(oldValue.substring(0, oldValue.lastIndex))
                }
            }
            "C" -> resetCalculator()
            in operands -> {
                // Handle numerical values
                if (readInputValue().contains(".") && tag == ".") {
                    return
                }

                val s = readInputValue() + tag
                editTextInput.setText(s)
            }
            else -> {

                if (tag != "=") {
                    textOperator.text = tag
                    if (readInputValue().isNotEmpty()){
                        textResult.text = readInputValue()
                    }
                    editTextInput.setText("")
                } else {
                    if(readInputValue().isNotEmpty()){
                    enterTimeToScheduledTaskDialog()
                    }
                }

            }
        }
    }

    private fun resetCalculator() {
        editTextInput.setText("")
        textOperator.text = "+"
        textResult.text = "0"
        timeToScheduled = 0
    }

    private fun readInputValue() = editTextInput.text.toString()

    @SuppressLint("InflateParams")
    private fun enterTimeToScheduledTaskDialog(){

        AlertDialog.Builder(requireContext()).apply {
            setTitle(getString(R.string.label_scheduled_time))
            setView(
                LayoutInflater.from(requireContext()).inflate(
                    R.layout.add_schedule_time_dialog,
                    null
                )
            )
        }.show().also { myDialog ->
            myDialog.buttonEqualWithTime.setOnClickListener {
                val timeString = myDialog.timeET.text.toString()
                timeToScheduled = if(timeString.isEmpty()){ 0 } else {timeString.toInt() }

                val result = appViewModel.doCalculation(textResult.text.toString().toFloat(),
                    textOperator.text.toString(), readInputValue().toFloat())

                insertEquation(result)

                myDialog.dismiss()

            }

        }

    }

    private fun insertEquation(result: Resource<Float>){
        when(result){
            is Resource.Success -> {
                result.data?.let { equationResult ->
                    appViewModel.insertEquation(
                        EquationEntity(
                            textResult.text.toString().toFloat(),
                            readInputValue().toFloat(), textOperator.text.toString(),
                            equationResult, currentAddress, false)
                    )
                }
            }
            is Resource.DataError -> {
                Toast.makeText(requireContext(),result.errorMassage, Toast.LENGTH_SHORT ).show()
            }

        }

    }

    private fun observeOnInsertedID(){
        appViewModel.insertedID.observe(viewLifecycleOwner) {
            if (it.peekContent() != -1L){
                startMathEngineService(timeToScheduled, it?.peekContent())
            }
        }
    }

    private fun startMathEngineService(time: Int, equationID: Long?) =
        Intent(requireContext(), MathEngineService::class.java).also {
            it.action = ACTION_START_SERVICE
            it.putExtra(MATH_EQUATION_SCHEDULED_TIME, time)
            it.putExtra(EQUATION_ID, equationID)
            requireContext().startService(it)
            resetCalculator()
        }


    @SuppressLint("MissingPermission")
    fun getLastLocation() {

        mFusedLocationProviderClient.lastLocation
            .addOnSuccessListener(requireActivity()) { location ->
                location?.let { it ->
                    val address = geocoder.getFromLocation(it.latitude, it.longitude,1)
                    address?.let {
                        currentAddress =  it[0].locality
                    }
                }
            }
    }

    private fun requestPermissions() {
        if (hasLocationPermissions(requireContext())) {
            return
        }
        EasyPermissions.requestPermissions(
            this,
            "You need to accept location permission to use this app",
            REQUEST_CODE_LOCATION_PERMISSION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            requestPermissions()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        getLastLocation()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

}