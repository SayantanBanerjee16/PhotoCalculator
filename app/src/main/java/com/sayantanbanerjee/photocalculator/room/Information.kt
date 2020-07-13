package com.sayantanbanerjee.photocalculator.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Information(
    @PrimaryKey(autoGenerate = true)
    val id : Int,
    val expression : String,
    val result : String
)
