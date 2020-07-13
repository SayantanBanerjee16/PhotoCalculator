package com.sayantanbanerjee.photocalculator

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sayantanbanerjee.photocalculator.databinding.ListItemBinding
import com.sayantanbanerjee.photocalculator.room.Information

class HistoryAdapter
    : RecyclerView.Adapter<MyViewHolder>() {
    private val informationList = ArrayList<Information>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ListItemBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.list_item, parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return informationList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(informationList[position])
    }

    fun setList(informations: List<Information>) {
        informationList.clear()
        informationList.addAll(informations)
    }

}

class MyViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(information: Information) {
        binding.expressionTextView.text = information.expression
        binding.resultTextView.text = information.result
    }
}