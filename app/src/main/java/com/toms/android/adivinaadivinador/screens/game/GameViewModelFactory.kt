package com.toms.android.adivinaadivinador.screens.game

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.toms.android.adivinaadivinador.database.ListDatabaseDao

class GameViewModelFactory(private val dataSource: ListDatabaseDao,
                           private val application: Application,
                           private val list: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            return GameViewModel(dataSource,application,list) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}