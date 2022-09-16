package de.erikspall.mensaapp.data.sources.remote

import android.util.Log
import de.erikspall.mensaapp.data.sources.remote.api.MensaApi
import java.lang.Exception

class ApiSource {
    suspend fun getLatestFoodProviderInfo() {
        try {
            val result = MensaApi.retrofitService.getLatestFoodProviderInfo()
            println(result.data)
        } catch (e: Exception) {
            Log.e("Api", e.stackTraceToString())
        }
    }
}