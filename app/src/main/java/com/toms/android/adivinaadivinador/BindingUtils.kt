package com.toms.android.adivinaadivinador

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.toms.android.adivinaadivinador.database.ItemListDataClass

@BindingAdapter("itemWord")
fun TextView.setCreatedWord(item: ItemListDataClass?){
    item?.let {
        text = item.itemWord
    }
}

