package me.ezzattharwat.vacomputing.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "equations_table")
data class EquationEntity(
    var number1: Float?,
    var number2: Float?,
    var operator: String?,
    var equationResult: Float?,
    var location:String?,
    var isCalculated: Boolean
){
    @PrimaryKey(autoGenerate = true)
    var id: Int?= null
}
