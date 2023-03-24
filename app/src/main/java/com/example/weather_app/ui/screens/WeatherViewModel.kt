package com.example.weather_app.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather_app.network.WeatherApi
import kotlinx.coroutines.launch

sealed interface WeatherUiState {
    data class Success(val weather: List<String>): WeatherUiState
    object Error: WeatherUiState
    object Loading: WeatherUiState
}

class WeatherViewModel: ViewModel() {
    var weatherUiState: WeatherUiState by mutableStateOf(WeatherUiState.Loading)
        private set

    init {
        getWeatherCity()
    }

    fun getWeatherCity() {
        viewModelScope.launch {
            val listResult = WeatherApi.retrofitService.getWeatherCity()
        }
    }
}

