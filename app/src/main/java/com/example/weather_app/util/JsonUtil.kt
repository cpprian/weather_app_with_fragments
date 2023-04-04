package com.example.weather_app.util

import android.util.Log
import android.widget.Toast
import com.example.weather_app.data.WeatherModel
import org.json.JSONObject

fun returnLatLong(data: String): Pair<String, String> {
    return try {
        val jsonObject = JSONObject(data)
        val latitude = jsonObject.getJSONArray("results").getJSONObject(0).getString("latitude")
        val longitude = jsonObject.getJSONArray("results").getJSONObject(0).getString("longitude")
        Pair(latitude, longitude)
    } catch (e: Exception) {
        Pair("0", "0")
    }
}

fun extractWeatherData(city: String, data: String): WeatherModel {
    return try {
        val jsonObject = JSONObject(data)
        val timezone = jsonObject.getString("timezone")
        val latitude = jsonObject.getDouble("latitude")
        val longitude = jsonObject.getDouble("longitude")
        val currentTime = jsonObject.getJSONObject("current_weather").getString("time")
        val temperature = jsonObject.getJSONObject("current_weather").getDouble("temperature")
        val temperatureUnit = jsonObject.getJSONObject("hourly_units").getString("temperature_2m")
        val weatherCode = jsonObject.getJSONObject("current_weather").getInt("weathercode")
        val windSpeed = jsonObject.getJSONObject("current_weather").getDouble("windspeed")
        val windDirection = jsonObject.getJSONObject("current_weather").getDouble("winddirection")
        val hourlyTime = mutableListOf<String>()
        val temperature_2m = mutableListOf<Double>()
        for (i in 0..23) {
            hourlyTime.add(jsonObject.getJSONObject("hourly").getJSONArray("time").getString(i))
            temperature_2m.add(jsonObject.getJSONObject("hourly").getJSONArray("temperature_2m").getDouble(i))
        }
        WeatherModel(
            city = city,
            timezone = timezone,
            latitude = latitude,
            longitude = longitude,
            currentTime = currentTime,
            temperature = temperature,
            temperatureUnit = temperatureUnit,
            weatherCode = weatherCode,
            windSpeed = windSpeed,
            windDirection = windDirection,
            hourlyTime = fromListToString(hourlyTime),
            temperature_2m = fromListDoubleToString(temperature_2m)
        )
    } catch (e: Exception) {
        Log.d("extractWeatherData", data + "\n" +  e.toString())
        WeatherModel(
            city = "",
            timezone = "0",
            latitude = 0.0,
            longitude = 0.0,
            currentTime = "0",
            temperature = 0.0,
            temperatureUnit = "celsius",
            weatherCode = 0,
            windSpeed = 0.0,
            windDirection = 0.0,
            hourlyTime = "0",
            temperature_2m = "0"
        )
    }
}