package com.example.weather_app.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(cityWithWeather: WeatherModel)

    @Query("UPDATE weather_table " +
            "SET currentTime = :newCurrentTime, " +
            "temperature = :newTemperature, " +
            "weatherCode = :newWeatherCode, " +
            "windSpeed = :newWindSpeed, " +
            "windDirection = :newWindDirection, " +
            "hourlyTime = :newHourlyTime, " +
            "temperature_2m = :newTemperature_2m, " +
            "temperatureUnit = :newTemperatureUnit " +
            "WHERE city = :city")
    suspend fun updateWeather(
        newCurrentTime: String,
        newTemperature: Double,
        newWeatherCode: Int,
        newWindSpeed: Double,
        newWindDirection: Double,
        newHourlyTime: String,
        newTemperature_2m: String,
        newTemperatureUnit: String,
        city: String
    )

    @Query("DELETE FROM weather_table WHERE city = :city")
    suspend fun deleteWeather(city: String)

    @Query("SELECT * FROM weather_table WHERE city = :city")
    fun getWeather(city: String): Flow<WeatherModel>

    @Query("SELECT * FROM weather_table ORDER BY city ASC")
    fun getAllWeather(): Flow<List<WeatherModel>>

    @Query("UPDATE weather_table SET temperatureUnit = :unit, temperature = (temperature - 32) * 5 / 9 WHERE temperatureUnit = 'fahrenheit' AND city = :city")
    fun updateWeatherUnitsCelsius(unit: String = "celsius", city: String)

    @Query("UPDATE weather_table SET temperatureUnit = :unit, temperature = temperature * 9 / 5 + 32 WHERE temperatureUnit = 'celsius' AND city = :city")
    fun updateWeatherUnitsFahrenheit(unit: String = "fahrenheit", city: String)
}