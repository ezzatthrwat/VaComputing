package me.ezzattharwat.vacomputing.di.application

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.ezzattharwat.vacomputing.di.scope.ApplicationScope
import me.ezzattharwat.vacomputing.operators.CalculatorEngine
import me.ezzattharwat.vacomputing.repository.AppRepository
import me.ezzattharwat.vacomputing.ui.AppViewModel
import javax.inject.Inject

@ApplicationScope
class AppViewModelFactory @Inject constructor(private var appRepository: AppRepository,private var engine: CalculatorEngine) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AppViewModel(appRepository, engine)  as T
    }
}