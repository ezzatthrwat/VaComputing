package me.ezzattharwat.vacomputing.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [EquationEntity::class],
    version = 1
)
abstract class EquationsDatabase : RoomDatabase(){
    abstract fun getEquationDAO(): EquationDAO
}