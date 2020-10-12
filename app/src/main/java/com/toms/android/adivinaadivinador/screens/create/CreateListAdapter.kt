package com.toms.android.adivinaadivinador.screens.create

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.toms.android.adivinaadivinador.R
import com.toms.android.adivinaadivinador.database.ItemListDataClass

class CreateListAdapter: RecyclerView.Adapter<CreateListAdapter.ViewHolder>(){

    var data = listOf<ItemListDataClass>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater
                .inflate(R.layout.database_item_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.wordTextView.text = item.itemWord.toString()
    }

    override fun getItemCount() = data.size

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val wordTextView: TextView = itemView.findViewById(R.id.txtWord)

    }
}
