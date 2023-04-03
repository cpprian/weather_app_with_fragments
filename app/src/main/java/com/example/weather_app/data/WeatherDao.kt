package com.example.weather_app.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(cityWithWeather: WeatherModel)

    @Query("UPDATE weather_table " +
            "SET currentTime = :currentTime, " +
            "temperature = :temperature, " +
            "weatherCode = :weatherCode, " +
            "windSpeed = :windSpeed, " +
            "windDirection = :windDirection, " +
            "hourlyTime = :hourlyTime, " +
            "temperature_2m = :temperature_2m " +
            "WHERE city = :city")
    suspend fun updateWeather(
        city: String,
        currentTime: String,
        temperature: Double,
        weatherCode: Int,
        windSpeed: Double,
        windDirection: Double,
        hourlyTime: List<String>,
        temperature_2m: List<Double>
    )

    @Query("DELETE FROM weather_table WHERE city = :city")
    suspend fun deleteWeather(city: String)

    @Query("SELECT * FROM weather_table WHERE city = :city")
    fun getWeather(city: String): Flow<WeatherModel>

    @Query("SELECT * FROM weather_table ORDER BY city ASC")
    fun getAllWeather(): Flow<List<WeatherModel>>
}