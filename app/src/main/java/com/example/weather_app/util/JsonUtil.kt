package com.example.weather_app.util

import android.util.Log
import android.widget.Toast
import com.example.weather_app.data.WeatherModel
import org.json.JSONObject

data class CityInfo(val latitude: String, val longitude: String, val timezone: String) {
    override fun toString(): String {
        return "CityInfo(latitude='$latitude', longitude='$longitude', timeZone='$timezone')"
    }
}

fun returnCityInfo(data: String): CityInfo {
    return try {
        val jsonObject = JSONObject(data)
        val latitude = jsonObject.getJSONArray("results").getJSONObject(0).getString("latitude")
        val longitude = jsonObject.getJSONArray("results").getJSONObject(0).getString("longitude")
        val timezone = jsonObject.getJSONArray("results").getJSONObject(0).getString("timezone")
        CityInfo(latitude, longitude, timezone)
    } catch (e: Exception) {
        CityInfo("0", "0", "0")
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
        val dailyTime = mutableListOf<String>()
        val dailyTemperature2mMax = mutableListOf<Double>()
        val dailyWeatherCode = mutableListOf<Int>()
        for (i in 0..6) {
            dailyTime.add(jsonObject.getJSONObject("hourly").getJSONArray("time").getString(i))
            dailyWeatherCode.add(jsonObject.getJSONObject("hourly").getJSONArray("weathercode").getInt(i))
            dailyTemperature2mMax.add(jsonObject.getJSONObject("hourly").getJSONArray("temperature_2m").getDouble(i))
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
            dailyTime = fromListToString(dailyTime),
            dailyWeatherCode = fromListIntToString(dailyWeatherCode),
            dailyTemperature2mMax = fromListDoubleToString(dailyTemperature2mMax)
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
            dailyTime = "0",
            dailyWeatherCode = "0",
            dailyTemperature2mMax = "0"
        )
    }
}