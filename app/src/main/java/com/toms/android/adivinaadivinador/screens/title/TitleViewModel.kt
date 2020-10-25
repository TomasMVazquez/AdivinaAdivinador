package com.toms.android.adivinaadivinador.screens.title

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.toms.android.adivinaadivinador.R
import com.toms.android.adivinaadivinador.database.ListDatabaseDao
import com.toms.android.adivinaadivinador.getSomeString
import com.toms.android.adivinaadivinador.network.*
import com.toms.android.adivinaadivinador.screens.game.GameViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TitleViewModel(val database: ListDatabaseDao, application: Application) : AndroidViewModel(application){

    //Game Finish Event
    private val _guessList = MutableLiveData<String>()
    val guessList : LiveData<String>
        get() = _guessList

    private val _eventPlay = MutableLiveData<Boolean>()
    val eventPlay: LiveData<Boolean>
        get() = _eventPlay

    private val _eventCreate = MutableLiveData<Boolean>()
    val eventCreate: LiveData<Boolean>
        get() = _eventCreate

    private val _formatStringToShow = MutableLiveData<Boolean>()
    val formatStringToShow : LiveData<Boolean>
        get() = _formatStringToShow

    private val _showMyList = MutableLiveData<Boolean>()
    val showMyList : LiveData<Boolean>
        get() = _showMyList

    init {
        getAnimalImagesFromAPI()
        _guessList.value = ""
        onShowMyList()
    }

    fun onPlay() {
        _eventPlay.value = true
    }

    fun onCreate(){
        _eventCreate.value = true
    }

    fun onCreateComplete() {
        _eventCreate.value = false
    }

    fun onPlayComplete() {
        _eventPlay.value = false
    }

    fun onChooseList(newList: String){
        _guessList.value = newList
    }

    fun startFormatString(){
        _formatStringToShow.value = true
    }

    fun endFormatString(){
        _formatStringToShow.value = false
    }

    fun onShowMyList(){
        val allWords =  database.getAllWords()
        _showMyList.value = !allWords.isEmpty()
    }

    //Get Images from API
    private fun getAnimalImagesFromAPI(){
        Log.d(TAG, "getAnimalImagesFromAPI: ")
        ImageApi.retrofitService
                .getAnimals(QUERY_KEY,"animals", QUERY_LANG_ES, QUERY_IMAGE_TYPE_PHOTO, QUERY_ORIENTATION_H, QUERY_CATEGORY_ANIMALS, QUERY_COLORS_TRANSPARENT)
                .enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                //_response.value = "Failure: " + t.message
                Log.d(TAG, "onFailure: ${t.message}")
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                //_response.value = response.body()
                Log.d(TAG, "onResponse: ${response.body()}")
            }
        })
    }

    companion object {
        private const val TAG = "TMV:"
    }

}