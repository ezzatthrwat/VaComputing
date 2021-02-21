package me.ezzattharwat.vacomputing.di.fragment

import dagger.Subcomponent
import me.ezzattharwat.vacomputing.di.scope.FragmentScope
import me.ezzattharwat.vacomputing.ui.calc.CalcFragment
import me.ezzattharwat.vacomputing.ui.log.LogFragment

/**
 * This interface is used by dagger to generate the code that defines the connection between the provider of objects
 * (i.e. [FragmentModule]), and the object which expresses a dependency.
 */
@FragmentScope
@Subcomponent(modules = [FragmentModule::class])
interface FragmentComponent {
    fun inject(calcFragment: CalcFragment)
    fun inject(logFragment: LogFragment)
}