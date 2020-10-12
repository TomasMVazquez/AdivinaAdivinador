package com.toms.android.adivinaadivinador.screens.title

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.toms.android.adivinaadivinador.R
import com.toms.android.adivinaadivinador.getSomeString

class TitleViewModel(application: Application) : AndroidViewModel(application){

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


    init {
        _guessList.value = ""
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

}