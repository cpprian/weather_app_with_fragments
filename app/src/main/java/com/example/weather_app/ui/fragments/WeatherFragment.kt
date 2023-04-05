package com.example.weather_app.ui.fragments

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weather_app.data.WeatherDao
import com.example.weather_app.data.WeatherModel

@Composable
fun WeatherFragment(city: String, weatherDB: WeatherDao, currentWeatherModel: WeatherModel) {
    if (city.isEmpty()) {
        Text(text = "Please enter a city")
        return
    }
    val isPortrait = LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT
    val currentWeather: WeatherModel = weatherDB.getWeather(city).collectAsState(initial = null).value ?: currentWeatherModel
    
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Today's Weather in $city",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            val weatherType = WeatherType.fromWMO(currentWeather.weatherCode)
            WeatherCard(
                icon = weatherType.iconRes,
                temperature = currentWeather.temperature.toString(),
                label = weatherType.weatherDesc,
                modifier = Modifier.weight(1f)
            )
        }
    }
}