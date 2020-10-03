package com.toms.android.adivinaadivinador.screens.title

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TitleViewModel : ViewModel(){

    //Game Finish Event
    private val _guessList = MutableLiveData<String>()
    val guessList : LiveData<String>
        get() = _guessList

    private val _eventPlay = MutableLiveData<Boolean>()
    val eventPlay: LiveData<Boolean>
        get() = _eventPlay

    init {
        _guessList.value = ""
    }

    fun onPlay() {
        _eventPlay.value = true
    }

    fun onPlayComplete() {
        _eventPlay.value = false
    }

    fun onChooseList(newList: String){
        _guessList.value = newList
    }

}