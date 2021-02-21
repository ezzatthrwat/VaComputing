package me.ezzattharwat.vacomputing.di.activity

import dagger.Subcomponent
import me.ezzattharwat.vacomputing.di.fragment.FragmentComponent
import me.ezzattharwat.vacomputing.di.fragment.FragmentModule
import me.ezzattharwat.vacomputing.di.scope.ActivityScope
import me.ezzattharwat.vacomputing.ui.HostActivity

/**
 * This interface is used by dagger to generate the code that defines the connection between the provider of objects
 * (i.e. [ActivityModule]), and the object which expresses a dependency.
 */
@ActivityScope
@Subcomponent(modules = [ActivityModule::class])
interface ActivityComponent {
    fun plus(fragmentModule: FragmentModule): FragmentComponent
    fun inject(hostActivity: HostActivity)
}