package me.ezzattharwat.vacomputing.repository

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import me.ezzattharwat.vacomputing.data.Resource
import me.ezzattharwat.vacomputing.data.db.EquationEntity

class FakeRepo : AppRepositorySource {

    private val equationsItems = mutableListOf<EquationEntity>()
    private val allEquationLiveDataPrivate = MutableLiveData<Resource<List<EquationEntity>>>(Resource.Success(equationsItems))
    private val insertedIDPrivate = MutableLiveData<Long>()


    private fun refreshLiveData() {
        allEquationLiveDataPrivate.postValue(Resource.Success(equationsItems))
    }
    override suspend fun insertEquation(equation: EquationEntity): Long {
        equationsItems.add(equation)
        refreshLiveData()
        insertedIDPrivate.postValue(equation.id!!.toLong())
        return equation.id!!.toLong()
    }

    override suspend fun updateEquation(id: Long) {
        equationsItems[id.toInt()].isCalculated = true
        refreshLiveData()
    }

    override fun getAllEquations(): Flow<List<EquationEntity>> {
       return flow {emit(equationsItems)}
    }
}