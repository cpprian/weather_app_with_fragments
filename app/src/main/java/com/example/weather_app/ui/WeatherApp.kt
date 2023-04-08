package com.example.weather_app.ui

import android.os.Build
import android.widget.Space
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import com.example.weather_app.R
import com.example.weather_app.data.WeatherDB
import com.example.weather_app.data.WeatherModel
import com.example.weather_app.ui.fragments.WeatherFragment
import com.example.weather_app.ui.screens.*
import com.example.weather_app.util.extractWeatherData
import com.example.weather_app.util.parseTime
import com.example.weather_app.util.returnLatLong
import kotlinx.coroutines.flow.firstOrNull
import java.time.temporal.ChronoUnit

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeatherApp(modifier: Modifier = Modifier) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val focusManager = LocalFocusManager.current
    val (isFavorite, setFavorite) = remember { mutableStateOf(false) }

    val database = WeatherDB.getDatabase(context)
    val dao = database.weatherDao()

    val favorites by dao.getAllWeather().collectAsState(initial = emptyList())

    if (favorites.isNotEmpty() && favorites[0].temperatureUnit == "°F") {
        dao.updateWeatherUnitsFahrenheit()
    }

    var city by rememberSaveable { mutableStateOf("") }
    var latitude by remember { mutableStateOf(0.0) }
    var longitude by remember { mutableStateOf(0.0) }
    var errorApi by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    var weatherUnit by remember { mutableStateOf("celsius") }

    var weatherModel by remember { mutableStateOf(
        WeatherModel(
            currentTime = "",
            temperature = 0.0,
            weatherCode = 0,
            windSpeed = 0.0,
            windDirection = 0.0,
            dailyTime = "",
            dailyWeatherCode = "",
            dailyTemperature2mMax = "",
            temperatureUnit = "",
            city = "",
            timezone = "",
            latitude = 0.0,
            longitude = 0.0
    )) }

    val cityViewModel: CityViewModel = rememberViewModel {
        CityViewModelFactory(city).create(CityViewModel::class.java)
    }

    val weatherViewModel: WeatherViewModel = rememberViewModel {
        WeatherViewModelFactory(latitude, longitude, weatherUnit).create(WeatherViewModel::class.java)
    }

    LaunchedEffect(city) {
        cityViewModel.cityUiState = CityUiState.Success(city)
        cityViewModel.getCity(city)
    }

    LaunchedEffect(latitude, longitude, weatherUnit) {
        weatherViewModel.weatherUiState = WeatherUiState.Success("")
        weatherViewModel.getWeatherCity(latitude, longitude, weatherUnit)
    }

    LaunchedEffect(city) {
        if (errorApi) return@LaunchedEffect

        setFavorite(false)
        for (favorite in favorites) {
            if (favorite.city == city) {
                setFavorite(true)
                dao.updateWeather(
                    favorite.currentTime,
                    favorite.temperature,
                    favorite.weatherCode,
                    favorite.windSpeed,
                    favorite.windDirection,
                    favorite.dailyTime,
                    favorite.dailyWeatherCode,
                    favorite.dailyTemperature2mMax,
                    favorite.temperatureUnit,
                    favorite.city
                )
                Toast.makeText(context, "This city is already in your favorites", Toast.LENGTH_SHORT).show()
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
                    val weatherModelExtractor = extractWeatherData(city, h.weather)
                    if (weatherModelExtractor.city != city) {
                        return@LaunchedEffect
                    }

                    val weather = dao.getWeather(city).firstOrNull()
                    if (weather == null) {
                        if (favorites.size >= 5) {
                            Toast.makeText(context, "You can only add 5 favorites", Toast.LENGTH_SHORT).show()
                            return@LaunchedEffect
                        }
                        dao.insertWeather(weatherModelExtractor)
                    } else {
                        val currentTime = parseTime(weatherModelExtractor.currentTime)
                        val lastUpdateTime = parseTime(weather.currentTime)

                        val diffInMinutes = ChronoUnit.MINUTES.between(lastUpdateTime, currentTime)
                        if (diffInMinutes < 5) {
                            return@LaunchedEffect
                        }

                        dao.updateWeather(
                            weatherModelExtractor.currentTime,
                            weatherModelExtractor.temperature,
                            weatherModelExtractor.weatherCode,
                            weatherModelExtractor.windSpeed,
                            weatherModelExtractor.windDirection,
                            weatherModelExtractor.dailyTime,
                            weatherModelExtractor.dailyWeatherCode,
                            weatherModelExtractor.dailyTemperature2mMax,
                            weatherModelExtractor.temperatureUnit,
                            weatherModelExtractor.city
                        )
                    }
                }
            }
        } else {
            dao.deleteWeather(city)
        }
    }

    LaunchedEffect(weatherUnit) {
        if (weatherUnit == "celsius") {
            dao.updateWeatherUnitsCelsius()
        } else if (weatherUnit == "fahrenheit") {
            dao.updateWeatherUnitsFahrenheit()
        }
    }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false },
        modifier = Modifier
            .padding(10.dp)
            .fillMaxHeight()
            .width(150.dp)
    ) {
        IconButton(onClick = {
            expanded = false

            val tempCity = city
            city = ""
            city = tempCity
        }) {
            Row {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = stringResource(id = R.string.sync)
                )
                Text(text = stringResource(id = R.string.sync))
            }
        }

        IconButton(onClick = {
            expanded = false
            weatherUnit = if (weatherUnit == "celsius") "fahrenheit" else "celsius"
        }) {
            Row {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = stringResource(id = R.string.swap_temperature_units)
                )
                Text(text = stringResource(id = R.string.swap_temperature_units))
            }
        }

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
                        modifier = Modifier.fillMaxWidth()
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
                        Spacer(modifier = Modifier.width(75.dp))
                        OutlinedTextField(
                            value = inputText,
                            onValueChange = { inputText = it },
                            label = { Text(text = stringResource(id = R.string.city)) },
                            modifier = Modifier.fillMaxWidth(0.5f),
                            keyboardActions = KeyboardActions(onDone = {
                                focusManager.clearFocus()

                                if (errorApi) return@KeyboardActions
                                city = inputText
                            }),
                            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done, keyboardType = KeyboardType.Text)
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
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
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
                    Text(
                        text = city,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(20.dp)
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
                        latitude = result.first
                        longitude = result.second
                    }
                }

                when (val weatherUiState = weatherViewModel.weatherUiState) {
                    is WeatherUiState.Loading -> {
                        Text(text = "Loading...", modifier = Modifier.padding(20.dp))
                    }
                    is WeatherUiState.Error -> {
                        Toast.makeText(
                            context,
                            "Error: " + weatherUiState.error,
                            Toast.LENGTH_SHORT).show()
                    }
                    is WeatherUiState.Success -> { weatherModel = extractWeatherData(city, weatherUiState.weather) }
                }

                WeatherFragment(city = city, weatherDB = dao, currentWeatherModel = weatherModel)
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
