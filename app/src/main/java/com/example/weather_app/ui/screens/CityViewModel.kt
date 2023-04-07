package com.example.weather_app.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weather_app.network.CityApi
import kotlinx.coroutines.launch
import java.io.IOException

sealed interface CityUiState {
    data class Success(val city: String): CityUiState
    data class Error(val error: String): CityUiState
    object Loading: CityUiState
}

class CityViewModel(
    city: String,
): ViewModel() {
    var cityUiState: CityUiState by mutableStateOf(CityUiState.Loading)

    init {
        getCity(city)
    }

    fun getCity(cityName: String) {
        viewModelScope.launch {
            cityUiState = try {
                val result = CityApi.retrofitService.getCity(cityName)
                CityUiState.Success(result)
            } catch(e: IOException) {
                CityUiState.Error("IO error: ${e.message}")
            } catch(e: Exception) {
                CityUiState.Error("Unknown error: ${e.message}")
            }
        }
    }

}

class CityViewModelFactory(private val cityName: String): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CityViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CityViewModel(cityName) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}