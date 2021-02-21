package me.ezzattharwat.vacomputing.ui


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*
import me.ezzattharwat.vacomputing.R
import me.ezzattharwat.vacomputing.VaComputingApp
import me.ezzattharwat.vacomputing.di.ComponentProvider
import me.ezzattharwat.vacomputing.di.activity.ActivityComponent
import me.ezzattharwat.vacomputing.di.activity.ActivityModule
import me.ezzattharwat.vacomputing.di.application.AppComponent

class HostActivity : AppCompatActivity(), ComponentProvider<ActivityComponent>{

    private var activityComponent: ActivityComponent? = null

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initDagger()

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        bottom_navigation_view.setupWithNavController(navController)

    }
    private fun initDagger(){
        val appComponent: AppComponent = VaComputingApp.getComponent(applicationContext)
        activityComponent = appComponent.plus(ActivityModule(this))
        activityComponent!!.inject(this)
    }
    override fun getComponent(): ActivityComponent {

        if (activityComponent == null){
            val appComponent: AppComponent = VaComputingApp.getComponent(applicationContext)
            activityComponent = appComponent.plus(ActivityModule(this))
            activityComponent?.inject(this)
        }
        return activityComponent!!
    }

}