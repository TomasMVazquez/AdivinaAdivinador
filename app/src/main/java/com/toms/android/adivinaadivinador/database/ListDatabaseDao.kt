package com.toms.android.adivinaadivinador.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ListDatabaseDao {

    //CRUD
    //Insertar
    @Insert
    suspend fun insert(item: ItemListDataClass)

    //Obtener
    @Query("SELECT * FROM list_created_table ORDER BY itemId DESC")
    fun getAllWords(): List<ItemListDataClass>

    //Obtener
    @Query("SELECT * FROM list_created_table ORDER BY itemId DESC LIMIT 1")
    fun getLastWord(): ItemListDataClass?


    //Borrar
    @Query("DELETE FROM list_created_table WHERE itemId = :key")
    fun deleteWord(key: Long)

    //Actualizar
   @Query("UPDATE list_created_table SET word = :wordUpdated WHERE itemId = :key")
    suspend fun updateWord(key: Long, wordUpdated: String)

    /*@Update
    suspend fun update(item: ItemListDataClass)*/
}