package com.example.weather_app.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weather_app.R
import com.example.weather_app.ui.screens.*
import com.example.weather_app.util.returnLatLong
import org.json.JSONObject

@Composable
fun WeatherApp(modifier: Modifier = Modifier) {
    var city by remember { mutableStateOf("") }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                modifier = modifier
                    .height(70.dp),
                title = {
                    Text(text = stringResource(id = R.string.app_name))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = city,
                            onValueChange = { city = it },
                            label = { Text(text = stringResource(id = R.string.city)) },
                        )
                    }

                }
            )
         },
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            color = MaterialTheme.colors.background
        ) {
            val cityViewModel: CityViewModel = rememberViewModel {
                CityViewModelFactory(city).create(CityViewModel::class.java)
            }

            LaunchedEffect(city) {
                cityViewModel.cityUiState = CityUiState.Success(city)
                cityViewModel.getCity(city)
            }

            Column {
                Text(text = "Your city: $city")

                when (val cityUiState = cityViewModel.cityUiState) {
                    is CityUiState.Loading -> Text(text = "Loading...")
                    is CityUiState.Error -> Text(text = "Error: ${cityUiState.error}")
                    is CityUiState.Success -> {
                        Text(text = "Your city from CityViewModel: ${cityUiState.city}")
                        val result = returnLatLong(cityUiState.city)
                        Text(text = "Your latitude: ${result.first}")
                        Text(text = "Your longitude: ${result.second}")
                    }
                }

                val weatherViewModel: WeatherViewModel = viewModel()
                HomeScreen(weatherUiState = weatherViewModel.weatherUiState)
            }
        }
    }
}

@Composable
inline fun <reified T : ViewModel> rememberViewModel(
    noinline viewModelFactory: () -> T
): T {
    val factory by remember { mutableStateOf(viewModelFactory) }
    return remember { factory() }
}
