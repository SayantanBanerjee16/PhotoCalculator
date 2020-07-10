package com.sayantanbanerjee.photocalculator.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Information::class], version = 1)
abstract class InformationDatabase: RoomDatabase() {
    abstract val informationDAO : InformationDAO

    companion object {
    @Volatile private var INSTANCE: InformationDatabase? = null
        fun getInstance(context: Context) : InformationDatabase{
            synchronized(this){
                var instance = INSTANCE
                if(instance == null){
                    instance = Room.databaseBuilder(context.applicationContext ,
                        InformationDatabase::class.java,
                        "information_database")
                        .build()
                }
                return instance
            }
        }
    }
}
