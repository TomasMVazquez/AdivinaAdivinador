package com.toms.android.adivinaadivinador.screens.create

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.toms.android.adivinaadivinador.database.ItemListDataClass
import com.toms.android.adivinaadivinador.databinding.DatabaseItemCardBinding

class CreateListAdapter: ListAdapter<ItemListDataClass, CreateListAdapter.ViewHolder>(ItemListDataClassDiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ViewHolder private constructor(val binding: DatabaseItemCardBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(item: ItemListDataClass) {
            binding.word = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater =  LayoutInflater.from(parent.context)
                val binding = DatabaseItemCardBinding.inflate(layoutInflater,parent,false)
                return ViewHolder(binding)
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
