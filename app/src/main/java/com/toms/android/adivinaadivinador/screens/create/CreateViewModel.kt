package com.toms.android.adivinaadivinador.screens.create

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.toms.android.adivinaadivinador.database.ItemListDataClass
import com.toms.android.adivinaadivinador.database.ListDatabaseDao
import kotlinx.coroutines.*

class CreateViewModel(val database: ListDatabaseDao, application: Application): AndroidViewModel(application){

    // The current word
    internal var _word = MutableLiveData<String>()
    val word : LiveData<String>
        get() = _word

    //Define viewModelJob and assign it an instance of Job.
    private var viewModelJob = Job()

    override fun onCleared() {
        super.onCleared()
        //Para que todas las coroutines se cancelen al clear la viewmodel
        viewModelJob.cancel()
    }

    //Define a uiScope for the coroutines:
    private val uiScope = CoroutineScope(Dispatchers.Main +  viewModelJob)

    //Define a variable to hold the current data, and make it MutableLiveData:
    private var data = MutableLiveData<ItemListDataClass?>()

    private val listCreated = database.getAllWords()

    init {
        initializeData()
    }

    private fun initializeData() {
        //Usamos una corutina para obtener la data de la BD para no bloquear la UI mientras esperamos los rdos
        uiScope.launch {
            data.value = getDataFromDataBase()
        }
    }

    //La marcamos como suspend porque la llamamos desde un corutina
    private suspend fun getDataFromDataBase(): ItemListDataClass? {
        return withContext(Dispatchers.IO){
            var data = database.getLastWord()
            data
        }
    }

    fun onSaveWordToDataBase(){
        Log.d(Companion.TAG, "onSaveWordToDataBase: ${word.toString()}")
        if (!word.value.isNullOrEmpty()) {
            uiScope.launch {
                val newWord = ItemListDataClass(0, "$word")
                insert(newWord)
                data.value = getDataFromDataBase()
            }
        }else{
            Toast.makeText(getApplication(),"No hay palabara",Toast.LENGTH_SHORT)
        }
    }

    private suspend fun insert(word: ItemListDataClass){
        withContext(Dispatchers.IO){
            database.insert(word)
        }
    }

    companion object {
        private const val TAG = "TMV"
    }

}