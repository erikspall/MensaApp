package de.erikspall.mensaapp.data.sources.remote

import android.util.Log
import de.erikspall.mensaapp.data.sources.remote.api.MensaApi
import de.erikspall.mensaapp.data.sources.remote.api.model.FoodProviderApiModel
import de.erikspall.mensaapp.data.sources.remote.api.model.MenuApiModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.util.Optional
import kotlin.Exception

class RemoteApiDataSource(
    private val ioDispatcher: CoroutineDispatcher
) {
    suspend fun fetchLatestFoodProviders(): Optional<List<FoodProviderApiModel>> =
        withContext(ioDispatcher) {
            try {
                val result = MensaApi.retrofitService.getLatestFoodProviderInfo()
                return@withContext Optional.of(result.data)
            } catch (e: Exception) {
                Log.e("Api", e.stackTraceToString())
            }
            return@withContext Optional.empty()
        }

    suspend fun fetchMenusOfCanteen(cid: Long): Optional<List<MenuApiModel>> =
        withContext(ioDispatcher) {
            try {
                val result = MensaApi.retrofitService.getLatestMenusOfCanteen(cid)
                return@withContext Optional.of(result.data)
            } catch (e: Exception) {
                Log.e("Api", e.stackTraceToString())
            }
            return@withContext Optional.empty()
        }
}