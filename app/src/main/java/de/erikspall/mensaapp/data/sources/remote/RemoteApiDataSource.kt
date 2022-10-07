package de.erikspall.mensaapp.data.sources.remote

import android.util.Log
import de.erikspall.mensaapp.data.errorhandling.OptionalResult
import de.erikspall.mensaapp.data.sources.remote.api.MensaApi
import de.erikspall.mensaapp.data.sources.remote.api.model.FoodProviderApiModel
import de.erikspall.mensaapp.data.sources.remote.api.model.MenuApiModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.util.Optional
import kotlin.Exception

class RemoteApiDataSource(
    private val ioDispatcher: CoroutineDispatcher
) {
    suspend fun fetchLatestFoodProviders(): OptionalResult<List<FoodProviderApiModel>> =
        withContext(ioDispatcher) {
            try {
                val result = MensaApi.retrofitService.getLatestFoodProviderInfo()
                return@withContext OptionalResult.of(result.data)
            }catch (socketException: SocketTimeoutException) {
                return@withContext OptionalResult.ofMsg("server unreachable")
            }catch (connectException: ConnectException) {
                return@withContext OptionalResult.ofMsg("no internet")
            } catch (e: Exception) { // TODO: make this more precise
                Log.e("Api", e.stackTraceToString())
                return@withContext OptionalResult.ofMsg("${e.message}")
            }
        }

    suspend fun fetchMenusOfCanteen(cid: Long): OptionalResult<List<MenuApiModel>> =
        withContext(ioDispatcher) {
            try {
                val result = MensaApi.retrofitService.getLatestMenusOfCanteen(cid)
                return@withContext OptionalResult.of(result.data)
            } catch (e: Exception) {
                Log.e("Api", e.stackTraceToString())
                return@withContext OptionalResult.ofMsg("${e.message}")
            }
        }
}