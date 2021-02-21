package me.ezzattharwat.vacomputing.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import me.ezzattharwat.vacomputing.data.Resource
import me.ezzattharwat.vacomputing.data.db.EquationEntity
import me.ezzattharwat.vacomputing.operators.CalculatorEngine
import me.ezzattharwat.vacomputing.other.Constant.CANT_DIVIDE
import me.ezzattharwat.vacomputing.other.Constant.NUM_1_NULL_MSG
import me.ezzattharwat.vacomputing.other.Constant.NUM_2_NULL_MSG
import me.ezzattharwat.vacomputing.other.Constant.OPERATOR_NULL_OR_EMPTY_MSG
import me.ezzattharwat.vacomputing.other.SingleEvent
import me.ezzattharwat.vacomputing.repository.AppRepositorySource
import javax.inject.Inject

class AppViewModel @Inject constructor(private val appRepository: AppRepositorySource,
                                       private val engine: CalculatorEngine ) : ViewModel() {


    private val allEquationLiveDataPrivate = MutableLiveData<Resource<List<EquationEntity>>>()
    val allEquationLiveData: LiveData<Resource<List<EquationEntity>>> get() = allEquationLiveDataPrivate

    private val insertedIDPrivate = MutableLiveData<SingleEvent<Long>>()
    val insertedID: LiveData<SingleEvent<Long>> get() = insertedIDPrivate


    fun doCalculation(num1: Float?, operator: String?, num2: Float?) : Resource<Float> {
        if (operator.equals("/") && num1 == 0f){
            return Resource.DataError(CANT_DIVIDE)
        }
        when {
            num1 == null -> {
                return Resource.DataError(NUM_1_NULL_MSG)
            }
            num2 == null -> {
                return Resource.DataError(NUM_2_NULL_MSG)
            }
            operator.isNullOrEmpty() -> {
                return Resource.DataError(OPERATOR_NULL_OR_EMPTY_MSG)
            }
            else -> {
                engine.operator = operator
                val result = engine.calculate(num1, num2)
                engine.clear()
                return Resource.Success(result)
            }
        }

    }

    fun insertEquation(equation: EquationEntity){
        viewModelScope.launch {
            insertedIDPrivate.value = SingleEvent(appRepository.insertEquation(equation))
        }
    }

    fun getAllEquations(){
        viewModelScope.launch {
            try {
                appRepository.getAllEquations().collect {
                    allEquationLiveDataPrivate.value = Resource.Success(it)
                }
            } catch (e: Exception) {
                allEquationLiveDataPrivate.value = Resource.DataError(e.toString())
            }
        }
    }
}