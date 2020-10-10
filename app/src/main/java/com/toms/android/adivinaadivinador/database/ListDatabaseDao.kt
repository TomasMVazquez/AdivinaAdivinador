package com.toms.android.adivinaadivinador.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ListDatabaseDao {

    //CRUD
    //Insertar
    @Insert
    fun insert(item: ItemListDataClass)

    //Obtener
    @Query("SELECT * FROM list_created_table ORDER BY itemId DESC")
    fun getAllWords(): LiveData<List<ItemListDataClass>>

    //Borrar
    @Query("DELETE FROM list_created_table WHERE itemId = :key")
    fun deleteWord(key: Long): ItemListDataClass?

    //Actualizar
    @Query("UPDATE list_created_table SET word = :wordUpdated WHERE itemId = :key")
    fun updateWord(key: Long, wordUpdated: String): ItemListDataClass?

    @Update
    fun update(item: ItemListDataClass)
}