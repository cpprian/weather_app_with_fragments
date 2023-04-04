package com.example.weather_app.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weather_app.network.WeatherApi
import kotlinx.coroutines.launch
import java.io.IOException

sealed interface WeatherUiState {
    data class Success(val weather: String): WeatherUiState
    data class Error(val error: String): WeatherUiState
    object Loading: WeatherUiState
}

class WeatherViewModel(
    lat: Double,
    long: Double,
    unit: String
): ViewModel() {
    var weatherUiState: WeatherUiState by mutableStateOf(WeatherUiState.Loading)

    init {
        getWeatherCity(lat, long, unit)
    }

    fun getWeatherCity(
        lat: Double,
        long: Double,
        unit: String
    ) {
        viewModelScope.launch {
            weatherUiState = try {
                val result = WeatherApi.retrofitService.getWeatherCity(lat, long, unit)
                WeatherUiState.Success(result)
            } catch(e: IOException) {
                WeatherUiState.Error("IO error: ${e.message}")
            } catch(e: Exception) {
                WeatherUiState.Error("Unknown error: ${e.message}")
            }
        }
    }
}

class WeatherViewModelFactory(private val lat: Double, private val long: Double, private val unit: String): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WeatherViewModel(lat, long, unit) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}