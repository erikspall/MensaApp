package de.erikspall.mensaapp.data.sources.remote.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import de.erikspall.mensaapp.data.sources.remote.api.model.ApiResponse
import de.erikspall.mensaapp.data.sources.remote.api.model.FoodProviderApiModel
import de.erikspall.mensaapp.data.sources.remote.api.model.MenuApiModel
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

object MensaApi {
    val retrofitService : MensaApiService by lazy {
        retrofit.create(MensaApiService::class.java)
    }

    private const val BASE_URL =
        "http://192.168.178.50:8080"

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(BASE_URL)
        .build()


}

interface MensaApiService {
    @GET("foodproviders")
    suspend fun getLatestFoodProviderInfo(): ApiResponse<List<FoodProviderApiModel>>

    @GET("/canteens/{id}/menus/today-and-beyond")
    suspend fun getLatestMenusOfCanteen(@Path("id") cid: Long): ApiResponse<List<MenuApiModel>>
}