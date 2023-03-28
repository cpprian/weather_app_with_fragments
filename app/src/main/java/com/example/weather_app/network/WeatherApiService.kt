package com.example.weather_app.network

import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://api.openweathermap.org"

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
    @GET("data/2.5/weather?lat=44.34&lon=10.99&appid=228b2145a90a6100269cb25044d16334")
    suspend fun getWeatherCity(): String
}