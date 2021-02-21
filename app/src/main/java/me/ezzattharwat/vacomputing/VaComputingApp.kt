package me.ezzattharwat.vacomputing

import android.app.Application
import android.content.Context
import com.google.android.libraries.places.api.Places
import me.ezzattharwat.vacomputing.di.ComponentProvider
import me.ezzattharwat.vacomputing.di.application.AppComponent
import me.ezzattharwat.vacomputing.di.application.AppModule
import me.ezzattharwat.vacomputing.di.application.DaggerAppComponent
import timber.log.Timber

class VaComputingApp: Application(), ComponentProvider<AppComponent> {

    companion object{
        fun getComponent(context: Context): AppComponent {
            return getApp(context).getComponent()
        }

        private fun getApp(context: Context): VaComputingApp {
            return context.applicationContext as VaComputingApp
        }
    }


    private val appComponent: AppComponent = createAppComponent()

    private fun createAppComponent(): AppComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()


    override fun onCreate() {
        super.onCreate()
        Places.initialize(this,getString(R.string.google_api_key))

        appComponent.inject(this)
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    override fun getComponent(): AppComponent = appComponent


}