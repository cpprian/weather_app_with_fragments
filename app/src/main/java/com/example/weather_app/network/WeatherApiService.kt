package com.example.weather_app.network

import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://api.open-meteo.com"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

object WeatherApi {
    val retrofitService: WeatherApiService by lazy {
        retrofit.create(WeatherApiService::class.java)
    }
}

interface WeatherApiService {
    @GET("v1/forecast?current_weather=true&daily=weathercode,temperature_2m_max&timezone=auto")
    suspend fun getWeatherCity(
        @Query("latitude") lat: Double,
        @Query("longitude") long: Double,
        @Query("temperature_unit") unit: String
    ): String
}