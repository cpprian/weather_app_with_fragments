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
            "dailyTime = :newDailyTime, " +
            "dailyWeatherCode = :newDailyWeatherCode, " +
            "dailyTemperature2mMax = :newDailyTemperature2mMax, " +
            "temperatureUnit = :newTemperatureUnit " +
            "WHERE city = :city")
    suspend fun updateWeather(
        newCurrentTime: String,
        newTemperature: Double,
        newWeatherCode: Int,
        newWindSpeed: Double,
        newWindDirection: Double,
        newDailyTime: String,
        newDailyWeatherCode: String,
        newDailyTemperature2mMax: String,
        newTemperatureUnit: String,
        city: String
    )

    @Query("DELETE FROM weather_table WHERE city = :city")
    suspend fun deleteWeather(city: String)

    @Query("SELECT * FROM weather_table WHERE city = :city")
    fun getWeather(city: String): Flow<WeatherModel>

    @Query("SELECT * FROM weather_table ORDER BY city ASC")
    fun getAllWeather(): Flow<List<WeatherModel>>

    @Query("UPDATE weather_table SET temperatureUnit = :unit, temperature = (temperature - 32) * 5 / 9 WHERE temperatureUnit = '°F'")
    fun updateWeatherUnitsCelsius(unit: String = "°C")

    @Query("UPDATE weather_table SET temperatureUnit = :unit, temperature = temperature * 9 / 5 + 32 WHERE temperatureUnit = '°C'")
    fun updateWeatherUnitsFahrenheit(unit: String = "°F")
}