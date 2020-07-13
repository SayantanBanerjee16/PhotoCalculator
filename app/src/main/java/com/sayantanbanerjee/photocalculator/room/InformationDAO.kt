package com.sayantanbanerjee.photocalculator.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface InformationDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(information: Information) : Long

    @Query("SELECT * FROM Information ORDER BY id desc")
    fun getAllInformation() : LiveData<List<Information>>
}
