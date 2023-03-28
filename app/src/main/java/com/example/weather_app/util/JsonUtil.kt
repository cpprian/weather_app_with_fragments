package com.example.weather_app.util

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