package me.ezzattharwat.vacomputing.repository

import kotlinx.coroutines.flow.Flow
import me.ezzattharwat.vacomputing.data.db.EquationEntity

interface AppRepositorySource {

    suspend fun insertEquation(equation: EquationEntity) : Long
    suspend fun updateEquation(id: Long)
     fun getAllEquations(): Flow<List<EquationEntity>>

}