package com.sayantanbanerjee.photocalculator.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface InformationDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(information: Information) : Long

    @Query("SELECT * FROM Information")
    fun getAllInformation() : LiveData<List<Information>>
}
