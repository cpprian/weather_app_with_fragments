package com.example.weather_app.ui.fragments

import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WeatherCard(
    icon: Int,
    temperature: String,
    label: String,
    weatherUnit: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = label,
            modifier = Modifier.size(200.dp)
        )
        Spacer(modifier = Modifier.height(15.dp))
        Text(
            text = temperature + weatherUnit,
            fontSize = 36.sp)
        Text(
            text = label,
            fontSize = 32.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun WeatherCardLandscape(
    icon: Int,
    temperature: String,
    label: String,
    timezone: String,
    latitude: String,
    longitude: String,
    currentTime: String,
    windDirection: String,
    windSpeed: String,
    weatherUnit: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,

        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = label,
                modifier = Modifier.size(50.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = temperature + weatherUnit,
                fontSize = 24.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = label,
                fontSize = 20.sp,
                color = Color.Gray
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Timezone: $timezone",
                fontSize = 16.sp
            )
            Text(
                text = "Latitude: $latitude, Longitude: $longitude",
                fontSize = 16.sp
            )
            Text(
                text = "Current time: $currentTime",
                fontSize = 16.sp
            )
            Text(
                text = "Wind speed: $windSpeed, Wind direction: $windDirection",
                fontSize = 16.sp
            )
        }
    }
}