package com.example.weather_app.ui

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.expandHorizontally
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.example.weather_app.R
import com.example.weather_app.data.WeatherDB
import com.example.weather_app.ui.screens.*
import com.example.weather_app.util.extractWeatherData
import com.example.weather_app.util.returnLatLong
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeatherApp(modifier: Modifier = Modifier) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val (isFavorite, setFavorite) = remember { mutableStateOf(false) }

    val database = WeatherDB.getDatabase(context)
    val dao = database.weatherDao()

    val favorites by dao.getAllWeather().collectAsState(initial = emptyList())

    var city by remember { mutableStateOf("") }
    var latitude by remember { mutableStateOf(0.0) }
    var longitude by remember { mutableStateOf(0.0) }
    var errorApi by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false },
        modifier = Modifier.padding(10.dp).fillMaxHeight().width(150.dp)
    ) {
        favorites.forEach { option ->
            DropdownMenuItem(onClick = {
                expanded = false
                city = option.city
            }) {
                Text(text = option.city)
            }
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                modifier = modifier
                    .height(70.dp),
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        var inputText by remember { mutableStateOf("") }

                        IconButton(
                            onClick = { expanded = true },
                            modifier = Modifier.padding(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = stringResource(id = R.string.menu)
                            )
                        }

                        OutlinedTextField(
                            value = inputText,
                            onValueChange = { inputText = it },
                            label = { Text(text = stringResource(id = R.string.city)) },
                            modifier = Modifier.fillMaxWidth(0.75f)
                        )

                        IconButton(
                            onClick = {
                                city = inputText
                            },
                            modifier = Modifier.padding(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = stringResource(id = R.string.search)
                            )
                        }
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

            val weatherViewModel: WeatherViewModel = rememberViewModel {
                WeatherViewModelFactory(latitude, longitude).create(WeatherViewModel::class.java)
            }

            LaunchedEffect(city) {
                cityViewModel.cityUiState = CityUiState.Success(city)
                cityViewModel.getCity(city)
            }

            LaunchedEffect(latitude, longitude) {
                weatherViewModel.weatherUiState = WeatherUiState.Success("Loading...")
                weatherViewModel.getWeatherCity(latitude, longitude)
            }

            Column {
                LaunchedEffect(city) {
                    if (errorApi) return@LaunchedEffect

                    setFavorite(false)
                    for (favorite in favorites) {
                        if (favorite.city == city) {
                            setFavorite(true)
                        }
                    }
                }

                LaunchedEffect(isFavorite) {
                    if (errorApi) return@LaunchedEffect

                    if (isFavorite) {
                        when(val h = weatherViewModel.weatherUiState) {
                            is WeatherUiState.Loading -> {
                                errorApi = false
                            }
                            is WeatherUiState.Error -> {
                                errorApi = true
                            }
                            is WeatherUiState.Success -> {
                                errorApi = false
                                val weatherModel = extractWeatherData(city, h.weather)
                                if (weatherModel.city != city) {
                                    return@LaunchedEffect
                                }

                                val weather = dao.getWeather(city).firstOrNull()
                                if (weather == null) {
                                    dao.insertWeather(weatherModel)
                                } else {
                                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")
                                    val currentTime = LocalDateTime.parse(weatherModel.currentTime, formatter)
                                    val lastUpdateTime = LocalDateTime.parse(weather.currentTime, formatter)

                                    val diffInMinutes = ChronoUnit.MINUTES.between(lastUpdateTime, currentTime)
                                    if (diffInMinutes < 5) {
                                        return@LaunchedEffect
                                    }

                                    dao.updateWeather(
                                        weatherModel.city,
                                        weatherModel.currentTime,
                                        weatherModel.temperature,
                                        weatherModel.weatherCode,
                                        weatherModel.windSpeed,
                                        weatherModel.windDirection,
                                        weatherModel.hourlyTime,
                                        weatherModel.temperature_2m
                                    )
                                }
                            }
                        }
                    } else {
                        dao.deleteWeather(city)
                    }
                }

                IconButton(onClick = {
                    if (errorApi) return@IconButton

                    setFavorite(!isFavorite)

                    Toast.makeText(
                        context,
                        if (isFavorite) "Removed from favorites" else "Added to favorites",
                        Toast.LENGTH_SHORT).show()
                } ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = if (isFavorite) stringResource(id = R.string.favorite)
                        else stringResource(id = R.string.not_favorite),
                        modifier = Modifier.padding(16.dp)
                    )
                }

                when (val cityUiState = cityViewModel.cityUiState) {
                    is CityUiState.Loading -> {
                        errorApi = false
                        /* TODO: Handle loading */
                    }
                    is CityUiState.Error -> {
                        errorApi = true
                        Toast.makeText(
                            context,
                            "Unable to find city: " + cityUiState.error,
                            Toast.LENGTH_SHORT).show()
                    }
                    is CityUiState.Success -> {
                        errorApi = false
                        val result = returnLatLong(cityUiState.city)
                        latitude = result.first.toDouble()
                        longitude = result.second.toDouble()
                    }
                }


            }
        }
    }
}

private suspend fun <T> Flow<T>.isEmpty(): Boolean {
    return this.firstOrNull() == null
}

@Composable
inline fun <reified T : ViewModel> rememberViewModel(
    noinline viewModelFactory: () -> T
): T {
    val factory by remember { mutableStateOf(viewModelFactory) }
    return remember { factory() }
}
