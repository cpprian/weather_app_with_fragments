package com.example.weather_app.network

import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://geocoding-api.open-meteo.com"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

object CityApi {
    val retrofitService: CityApiService by lazy {
        retrofit.create(CityApiService::class.java)
    }
}

interface CityApiService {
    @GET("v1/search?count=1")
    suspend fun getCity(
        @Query("name") city: String
    ): String
}