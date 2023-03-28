package com.example.weather_app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.weather_app.R

@Composable
fun HomeScreen(
    weatherUiState: WeatherUiState,
    modifier: Modifier = Modifier
) {
    when(weatherUiState) {
        is WeatherUiState.Loading -> LoadingScreen(modifier)
        is WeatherUiState.Error -> ErrorScreen(weatherUiState.error, modifier)
        is WeatherUiState.Success -> ResultScreen(weatherUiState.weather, modifier)
    }
}

@Composable
fun ErrorScreen(
    weather: String,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
//        Text(stringResource(id = R.string.error_message))
        Text(text = weather)
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Image(
            modifier = modifier.size(200.dp),
            painter = painterResource(id = R.drawable.loading_img),
            contentDescription = stringResource(id = R.string.loading_message)
        )
    }
}

@Composable
fun ResultScreen(
    weather: String,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Text(text = weather)
    }
}