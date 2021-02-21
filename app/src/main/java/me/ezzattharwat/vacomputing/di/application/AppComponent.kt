package me.ezzattharwat.vacomputing.di.application

import dagger.Component
import me.ezzattharwat.vacomputing.VaComputingApp
import me.ezzattharwat.vacomputing.alarmbroadcast.AlarmBroadcastReceiver
import me.ezzattharwat.vacomputing.di.activity.ActivityComponent
import me.ezzattharwat.vacomputing.di.activity.ActivityModule
import me.ezzattharwat.vacomputing.di.scope.ApplicationScope

@ApplicationScope
@Component(modules = [AppModule::class])
interface AppComponent {

    fun plus(activityModule: ActivityModule?): ActivityComponent?

    fun inject(vaComputing: VaComputingApp)
    fun inject(alarmBroadcastReceiver: AlarmBroadcastReceiver)
}