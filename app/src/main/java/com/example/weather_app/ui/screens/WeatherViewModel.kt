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

    fun getWeatherCity() {
        viewModelScope.launch {
            weatherUiState = try {
                val listResult = WeatherApi.retrofitService.getWeatherCity()
                WeatherUiState.Success("Success: $listResult")
            } catch(e: IOException) {
                WeatherUiState.Error("IO error: ${e.message}")
            } catch(e: Exception) {
                WeatherUiState.Error("Unknown error: ${e.message}")
            }
        }
    }
}

