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
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = label,
            modifier = Modifier.size(150.dp)
        )
        Spacer(modifier = Modifier.height(15.dp))
        Text(
            text = temperature,
            fontSize = 30.sp)
        Text(
            text = label,
            fontSize = 28.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun WeatherCardLandscape(
    icon: Int,
    temperature: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = label,
                modifier = Modifier.size(50.dp)
            )
            Divider(color = Color.Gray, thickness = 1.dp, modifier = Modifier.fillMaxWidth())
            Text(
                text = temperature,
                fontSize = 24.sp)
            Text(
                text = label,
                fontSize = 20.sp,
                color = Color.Gray
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

        }
    }
}