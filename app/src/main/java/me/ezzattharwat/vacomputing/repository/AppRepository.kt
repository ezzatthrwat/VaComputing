package me.ezzattharwat.vacomputing.repository


import kotlinx.coroutines.flow.Flow
import me.ezzattharwat.vacomputing.data.db.EquationDAO
import me.ezzattharwat.vacomputing.data.db.EquationEntity
import javax.inject.Inject

class AppRepository @Inject constructor(private val equationsDAO: EquationDAO) : AppRepositorySource {


    override suspend fun insertEquation(equation: EquationEntity): Long = equationsDAO.insertEquation(equation)
    override suspend fun updateEquation(id: Long) = equationsDAO.updateEquation(id)
    override fun getAllEquations(): Flow<List<EquationEntity>> = equationsDAO.getAllEquations()

}