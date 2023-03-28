package com.example.weather_app.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather_app.network.WeatherApi
import kotlinx.coroutines.launch
import java.io.IOException

sealed interface WeatherUiState {
    data class Success(val weather: String): WeatherUiState
    data class Error(val error: String): WeatherUiState
    object Loading: WeatherUiState
}

class WeatherViewModel: ViewModel() {
    var weatherUiState: WeatherUiState by mutableStateOf(WeatherUiState.Loading)
        private set

    init {
        getWeatherCity()
    }

    private fun getWeatherCity(
        lat: Double = 48.8534,
        long: Double = 2.3488
    ) {
        viewModelScope.launch {
            weatherUiState = try {
                val result = WeatherApi.retrofitService.getWeatherCity(lat, long)
                WeatherUiState.Success("Success: $result")
            } catch(e: IOException) {
                WeatherUiState.Error("IO error: ${e.message}")
            } catch(e: Exception) {
                WeatherUiState.Error("Unknown error: ${e.message}")
            }
        }
    }
}

