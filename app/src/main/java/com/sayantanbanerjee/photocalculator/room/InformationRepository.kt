package com.sayantanbanerjee.photocalculator.room

class InformationRepository(private val dao : InformationDAO) {

    val information = dao.getAllInformation()

    fun insert(information: Information){
        dao.insert(information)
    }
}
