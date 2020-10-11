package com.toms.android.adivinaadivinador.screens.create

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import com.toms.android.adivinaadivinador.R
import com.toms.android.adivinaadivinador.database.ItemListDataClass

/*class CreateListAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    var data = listOf<ItemListDataClass>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater
                .inflate(R.layout.database_item_card, parent, false) as CardView
        return RecyclerView.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        //val item = data(position)

    }

    override fun getItemCount() = data.size

}*/
