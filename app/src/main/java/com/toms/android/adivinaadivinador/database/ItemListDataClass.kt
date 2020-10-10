package com.toms.android.adivinaadivinador.database

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "list_created_table")
data class ItemListDataClass(
        @PrimaryKey(autoGenerate = true)
        var itemId: Long = 0L,
        @ColumnInfo(name = "word")
        var itemWord: String?
        //TODO agregar imagen para las palabras
        /*,
        @ColumnInfo(name = "picture")
        val itemPicture: Bitmap?*/
)