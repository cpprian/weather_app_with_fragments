package com.example.weather_app.ui.fragments

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.weather_app.util.parseDate
import com.example.weather_app.util.parseTime
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ForecastItem(time: String, forecast: Double, weatherCode: Int, weatherUnit: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Column {
            Icon(
                painter = painterResource(WeatherType.fromWMO(weatherCode).iconRes),
                contentDescription = "Weather Icon",
                modifier = Modifier.size(40.dp))
            Text(
                text = forecast.toString() + weatherUnit,
                modifier = Modifier.padding(8.dp))
        }
        Text(
            text = parseDate(time).toString(),
            modifier = Modifier.padding(8.dp))
    }
    Spacer(modifier = Modifier.height(16.dp))
    Divider(color = Color.Gray, thickness = 1.dp, modifier = Modifier.fillMaxWidth())
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ForecastList(time: List<String>, forecasts: List<Double>, weathercodes: List<Int>, weatherUnit: String) {
    if (time.isEmpty() || forecasts.isEmpty() || weathercodes.isEmpty()) return
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        items(forecasts.size) { index ->
            ForecastItem(
                time = time[index],
                forecast = forecasts[index],
                weathercodes[index],
                weatherUnit = weatherUnit)
        }
    }
}

