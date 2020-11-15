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
    //list created
    private var _list = MutableLiveData<List<ItemListDataClass>>()
    val list : LiveData<List<ItemListDataClass>>
        get() = _list

    //reset EditText Event
    private val _eventDoneAdding = MutableLiveData<Boolean>()
    val eventDoneAdding : LiveData<Boolean>
        get() = _eventDoneAdding

    //Delete Item
    private val _eventDoneDelete = MutableLiveData<Boolean>()
    val eventDoneDelete : LiveData<Boolean>
        get() = _eventDoneDelete

    init {
        initializeData()
    }

    private fun initializeData() {
        //Usamos una corutina para obtener la data de la BD para no bloquear la UI mientras esperamos los rdos
        uiScope.launch {
            _list.value = getDataFromDataBase()
        }
        _eventDoneAdding.value = false
    }

    //La marcamos como suspend porque la llamamos desde un corutina
    private suspend fun getDataFromDataBase(): List<ItemListDataClass>? {
        return withContext(Dispatchers.IO){
            var data = database.getAllWords()
            data
        }
    }

    fun onSaveWordToDataBase(word: String){
        if (!word.isNullOrEmpty()) {
            val newWord = ItemListDataClass(itemWord = word)
            uiScope.launch {
                insert(newWord)
                _list.value = getDataFromDataBase()
                _eventDoneAdding.value = true
            }
        }else{
//            Log.d(TAG, "onSaveWordToDataBase: SE INTENTO CLICKEAR SIN PALABRAS")
        }
    }

    fun onDeleteWordFromDataBase(id: Long){
        uiScope.launch {
            delete(id)
            _list.value = getDataFromDataBase()
            _eventDoneDelete.value = true
        }
    }

    private suspend fun insert(item: ItemListDataClass){
        withContext(Dispatchers.IO){
            database.insert(item)
        }
    }

    private suspend fun delete(id: Long){
        withContext(Dispatchers.IO){
            database.deleteWord(id)
        }
    }

    fun onFinishAdding(){
        _eventDoneAdding.value = false
    }

    fun onStartDeleting(){
        _eventDoneDelete.value = false
    }

    companion object {
        private const val TAG = "TMV"
    }

}