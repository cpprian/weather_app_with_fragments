package com.example.weather_app.ui.fragments

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.example.weather_app.data.WeatherDao
import com.example.weather_app.data.WeatherModel

@Composable
fun WeatherFragment(city: String, weatherDB: WeatherDao, currentWeatherModel: WeatherModel) {
    if (city.isEmpty()) {
        Text(text = "Please enter a city")
        return
    }
    Text(text = "Your city is $city")

    val weather = weatherDB.getWeather(city).collectAsState(initial = null).value
    if (weather != null) {
        Text(text = "Weather from your city: ${weather.temperature}")
    } else {
        Text(text = "Weather from your city: ${currentWeatherModel.temperature}")
    }
}