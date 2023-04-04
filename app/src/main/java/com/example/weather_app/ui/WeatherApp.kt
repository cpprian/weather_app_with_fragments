package com.example.weather_app.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weather_app.R
import com.example.weather_app.data.WeatherDB
import com.example.weather_app.ui.screens.*
import com.example.weather_app.util.extractWeatherData
import com.example.weather_app.util.returnLatLong

@Composable
fun WeatherApp(modifier: Modifier = Modifier) {
    val context = androidx.compose.ui.platform.LocalContext.current
    var (isFavorite, setFavorite) = remember { mutableStateOf(false) }

    val database = WeatherDB.getDatabase(context)
    val dao = database.weatherDao()

    val favorites by dao.getAllWeather().collectAsState(initial = emptyList())

    var city by remember { mutableStateOf("") }
    var latitude by remember { mutableStateOf(0.0) }
    var longitude by remember { mutableStateOf(0.0) }
    var weatherUiState by remember { mutableStateOf(WeatherUiState.Loading) }

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
                        OutlinedTextField(
                            value = inputText,
                            onValueChange = { inputText = it },
                            label = { Text(text = stringResource(id = R.string.city)) },
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
                    setFavorite(false)
                    for (favorite in favorites) {
                        if (favorite.city == city) {
                            setFavorite(true)
                        }
                    }
                }

                LaunchedEffect(isFavorite) {
                    if (isFavorite) {
                        when(val h = weatherViewModel.weatherUiState) {
                            is WeatherUiState.Loading -> {}
                            is WeatherUiState.Error -> {}
                            is WeatherUiState.Success -> {
                                val weatherModel = extractWeatherData(city, h.weather)
                                if (weatherModel.city != city) {
                                    return@LaunchedEffect
                                }
                                dao.insertWeather(weatherModel)
                            }
                        }
                    } else {
                        dao.deleteWeather(city)
                    }
                }

                IconButton(onClick = {
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
                    is CityUiState.Loading -> Text(text = "Loading...")
                    is CityUiState.Error -> Text(text = "Error: ${cityUiState.error}")
                    is CityUiState.Success -> {
//                        Text(text = "Your city from CityViewModel: ${cityUiState.city}")
                        val result = returnLatLong(cityUiState.city)
                        latitude = result.first.toDouble()
                        longitude = result.second.toDouble()
//                        Text(text = "Your latitude: $latitude")
//                        Text(text = "Your longitude: $longitude")
                    }
                }

                Text(text = "len fav: " + favorites.size.toString())
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                ) {
                    items(favorites) { favorite ->
                        Text(text = "Fav city: " + favorite.city)
                    }
                }
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
