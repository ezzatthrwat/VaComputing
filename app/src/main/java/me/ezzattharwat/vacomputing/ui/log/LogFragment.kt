package me.ezzattharwat.vacomputing.ui.log


import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_log.*

import me.ezzattharwat.vacomputing.R
import me.ezzattharwat.vacomputing.data.Resource
import me.ezzattharwat.vacomputing.data.db.EquationEntity
import me.ezzattharwat.vacomputing.di.ComponentProvider
import me.ezzattharwat.vacomputing.di.activity.ActivityComponent
import me.ezzattharwat.vacomputing.di.application.AppViewModelFactory
import me.ezzattharwat.vacomputing.di.fragment.FragmentModule
import me.ezzattharwat.vacomputing.ui.AppViewModel
import timber.log.Timber
import javax.inject.Inject

class LogFragment: Fragment(R.layout.fragment_log)  {

   private lateinit var appViewModel : AppViewModel

   @Inject
   lateinit var factory: AppViewModelFactory

    private lateinit var logAdapter: LogAdapter

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        initAppViewModel()
        setupRV()
        appViewModel.getAllEquations()
        setSubscribeEquationsData()
        super.onViewCreated(view, savedInstanceState)
    }


    private fun initAppViewModel(){
        appViewModel = ViewModelProvider(this, factory).get(AppViewModel::class.java)
    }

    private fun setupRV(){
        val layoutManager = LinearLayoutManager(requireContext())
        equationsListRV.layoutManager = layoutManager
        equationsListRV.setHasFixedSize(true)
        logAdapter = LogAdapter()
        equationsListRV.adapter = logAdapter


    }

    private fun setSubscribeEquationsData(){
        appViewModel.allEquationLiveData.observe(viewLifecycleOwner){
            it?.let {
                when (it){
                    is Resource.Success -> it.data?.let { equationList -> bindListData(equationList) }
                    is Resource.DataError -> {
                        Timber.e(it.errorMassage)
                        showDataView(false)
                    }
                }
            }
        }
    }

    private fun bindListData(list: List<EquationEntity>){
        if (!(list.isNullOrEmpty())) {
            logAdapter.setData(list)
            logAdapter.notifyDataSetChanged()
            showDataView(true)
        } else {
            showDataView(false)
        }
    }

    private fun showDataView(show: Boolean) {
        noDataTV.visibility = if (show) View.GONE else View.VISIBLE
        equationsListRV.visibility = if (show) View.VISIBLE else View.GONE
        equationsListPB.visibility = View.GONE
    }
}