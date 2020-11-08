package com.toms.android.adivinaadivinador.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.lifecycle.LiveData

enum class NetworkAvailability {
    UNKNOWN,
    CONNECTED,
    DISCONNECTED
}

class ConnectionStateLiveData(val context: Context) : LiveData<NetworkAvailability>() {

    companion object {
        private lateinit var instance: ConnectionStateLiveData

        fun get(context: Context): ConnectionStateLiveData {
            instance = if (::instance.isInitialized){
                instance
            }else{
                ConnectionStateLiveData(context)
            }
            return instance
        }
    }

    private val connectivityBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            val connectivityManager =
                    context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val netInfo = connectivityManager.activeNetworkInfo

            value = if (netInfo != null && netInfo.isConnected) {
                NetworkAvailability.CONNECTED
            } else {
                NetworkAvailability.DISCONNECTED
            }
        }
    }

    override fun onActive() {
        super.onActive()
        value = NetworkAvailability.UNKNOWN
        val broadcastIntent = IntentFilter()
        broadcastIntent.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        context.registerReceiver(connectivityBroadcastReceiver, broadcastIntent)
    }

    override fun onInactive() {
        super.onInactive()
        context.unregisterReceiver(connectivityBroadcastReceiver)
    }
}