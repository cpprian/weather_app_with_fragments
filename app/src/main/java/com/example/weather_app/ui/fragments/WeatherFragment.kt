package com.example.weather_app.ui.fragments

import android.content.res.Configuration
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weather_app.data.WeatherDao
import com.example.weather_app.data.WeatherModel
import com.example.weather_app.util.fromStringToList
import com.example.weather_app.util.fromStringToListDouble
import com.example.weather_app.util.fromStringToListInt

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeatherFragment(city: String, weatherDB: WeatherDao, currentWeatherModel: WeatherModel) {
    if (city.isEmpty()) {
        Text(text = "Please enter a city")
        return
    }
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    val isLargeScreen = configuration.screenWidthDp >= 600
    val currentWeather: WeatherModel = weatherDB.getWeather(city).collectAsState(initial = null).value ?: currentWeatherModel

    if (isPortrait && !isLargeScreen) {
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
                    weatherUnit = currentWeather.temperatureUnit,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Divider(color = Color.Gray, thickness = 1.dp, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Time of fetching: ${currentWeather.currentTime}",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )
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
            ) {
                val weatherType = WeatherType.fromWMO(currentWeather.weatherCode)
                WeatherCardLandscape(
                    icon = weatherType.iconRes,
                    temperature = currentWeather.temperature.toString(),
                    label = weatherType.weatherDesc,
                    timezone = currentWeather.timezone,
                    latitude = currentWeather.latitude.toString(),
                    longitude = currentWeather.longitude.toString(),
                    currentTime = currentWeather.currentTime,
                    windDirection = currentWeather.windDirection.toString(),
                    windSpeed = currentWeather.windSpeed.toString(),
                    currentWeather.temperatureUnit,
                    modifier = Modifier.weight(1f)
                )
            }
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ForecastList(
                    time = fromStringToList(currentWeather.dailyTime),
                    forecasts = fromStringToListDouble(currentWeather.dailyTemperature2mMax),
                    weathercodes = fromStringToListInt(currentWeather.dailyWeatherCode),
                    weatherUnit = currentWeather.temperatureUnit)
            }
        }
    }
}
