package de.erikspall.mensaapp.data.sources.remote.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import de.erikspall.mensaapp.data.sources.remote.api.model.ApiResponse
import de.erikspall.mensaapp.data.sources.remote.api.model.FoodProviderApiModel
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

object MensaApi {
    val retrofitService : MensaApiService by lazy {
        retrofit.create(MensaApiService::class.java)
    }

    private const val BASE_URL =
        "http://192.168.0.17:8080"

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(BASE_URL)
        .build()


}

interface MensaApiService {
    @GET("canteens")
    suspend fun getLatestFoodProviderInfo(): ApiResponse<List<FoodProviderApiModel>>
}