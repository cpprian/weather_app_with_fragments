package com.example.weather_app.ui.fragments

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun ForecastItem(time: String, forecast: Double, weatherCode: Int) {
    Column {
        Icon(
            painter = painterResource(id = weatherCode),
            contentDescription = "Weather Icon",
            modifier = Modifier.size(25.dp)
        )
        Text(
            text = "$time:$forecast",
            modifier = Modifier.padding(8.dp))
    }
}

@Composable
fun ForecastList(time: List<String>, forecasts: List<Double>, weathercodes: List<Int>) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        items(forecasts.size) { index ->
            ForecastItem(time = time[index], forecast = forecasts[index], weathercodes[index])
        }
    }
}

