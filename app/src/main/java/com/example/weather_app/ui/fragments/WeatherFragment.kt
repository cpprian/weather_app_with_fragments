package com.example.weather_app.ui.fragments

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weather_app.data.WeatherDao
import com.example.weather_app.data.WeatherModel
import com.example.weather_app.util.fromStringToListDouble

@Composable
fun WeatherFragment(city: String, weatherDB: WeatherDao, currentWeatherModel: WeatherModel) {
    if (city.isEmpty()) {
        Text(text = "Please enter a city")
        return
    }
    val isPortrait = LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT
    val currentWeather: WeatherModel = weatherDB.getWeather(city).collectAsState(initial = null).value ?: currentWeatherModel

    if (isPortrait) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
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

            Spacer(modifier = Modifier.height(16.dp))
            Divider(color = Color.Gray, thickness = 1.dp, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Today's Forecast",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    } else {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(0.5f),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            )
            {
                val weatherType = WeatherType.fromWMO(currentWeather.weatherCode)
                WeatherCardLandscape(
                    icon = weatherType.iconRes,
                    temperature = currentWeather.temperature.toString(),
                    label = weatherType.weatherDesc,
                    modifier = Modifier.weight(1f)
                )
            }
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Today's Forecast",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp)
                )

                ForecastList(forecasts = fromStringToListDouble(currentWeather.dailyTemperature2mMax))
            }
        }
    }
}
