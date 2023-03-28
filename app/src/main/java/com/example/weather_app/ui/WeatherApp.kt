package com.example.weather_app.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weather_app.R
import com.example.weather_app.ui.screens.HomeScreen
import com.example.weather_app.ui.screens.WeatherViewModel

@Composable
fun WeatherApp(modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { TopAppBar(title = { Text(text = stringResource(id = R.string.app_name)) })},
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            color = MaterialTheme.colors.background
        ) {
            val weatherViewModel: WeatherViewModel = viewModel()
            HomeScreen(weatherUiState = weatherViewModel.weatherUiState)
        }
    }
}