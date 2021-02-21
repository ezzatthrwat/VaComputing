package me.ezzattharwat.vacomputing.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface EquationDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEquation(equation: EquationEntity) : Long

    @Query("UPDATE equations_table SET isCalculated= 1 WHERE id = :id ")
    suspend fun updateEquation(id: Long)

    @Query("SELECT * From equations_table ORDER BY isCalculated DESC")
    fun getAllEquations(): Flow<List<EquationEntity>>
}