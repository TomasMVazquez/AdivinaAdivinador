package com.toms.android.adivinaadivinador.screens.create

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.toms.android.adivinaadivinador.R
import com.toms.android.adivinaadivinador.database.ItemListDataClass

class CreateListAdapter: ListAdapter<ItemListDataClass, CreateListAdapter.ViewHolder>(ItemListDataClassDiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(holder, item)
    }

    class ViewHolder private constructor(itemView: View): RecyclerView.ViewHolder(itemView){
        //Atributo del card
        val wordTextView: TextView = itemView.findViewById(R.id.txtWord)

        fun bind(holder: ViewHolder, item: ItemListDataClass) {
            holder.wordTextView.text = item.itemWord.toString()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val view =  LayoutInflater.from(parent.context)
                        .inflate(R.layout.database_item_card, parent, false)
                return ViewHolder(view)
            }
        }

    }

    class ItemListDataClassDiffCallback : DiffUtil.ItemCallback<ItemListDataClass>() {
        override fun areItemsTheSame(oldItem: ItemListDataClass, newItem: ItemListDataClass): Boolean {
            return oldItem.itemId == newItem.itemId
        }

        override fun areContentsTheSame(oldItem: ItemListDataClass, newItem: ItemListDataClass): Boolean {
            return oldItem == newItem
        }

    }

}
