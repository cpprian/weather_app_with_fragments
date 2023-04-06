package com.example.weather_app.ui.fragments

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ForecastItem(forecast: Double) {
    Text(text = forecast.toString(), modifier = Modifier.padding(8.dp))
}

@Composable
fun ForecastList(forecasts: List<Double>) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        items(forecasts.size) { index ->
            ForecastItem(forecast = forecasts[index])
        }
    }
}

