package com.sayantanbanerjee.photocalculator

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sayantanbanerjee.photocalculator.room.InformationDatabase
import com.sayantanbanerjee.photocalculator.room.InformationRepository

class HistoryActivity : AppCompatActivity() {

    private lateinit var repository : InformationRepository
    private lateinit var adapter: HistoryAdapter
    private lateinit var informationRecyclerView : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list)
        repository = InformationRepository(InformationDatabase.getInstance(application).informationDAO)
        informationRecyclerView = findViewById(R.id.information_recyclerView)
        initRecyclerView()
    }

    private fun initRecyclerView(){
        informationRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = HistoryAdapter()
        informationRecyclerView.adapter = adapter
        displaySubscribersList()
    }

    private fun displaySubscribersList(){
        repository.information.observe(this, Observer {
            adapter.setList(it)
            adapter.notifyDataSetChanged()
        })
    }
}
