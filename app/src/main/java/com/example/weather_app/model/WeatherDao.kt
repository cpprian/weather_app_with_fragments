package com.example.weather_app.model

import androidx.room.*

@Dao
interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteCity(cityWithWeather: WeatherModel)

    @Query("UPDATE weather_table " +
            "SET currentWeather = :cityWithWeather.currentWeather, " +
            "hourly = :cityWithWeather.hourly, " +
            "WHERE cityInfo.city = :cityWithWeather.cityInfo.city")
    suspend fun updateFavoriteCity(cityWithWeather: WeatherModel)

    @Query("DELETE FROM weather_table WHERE cityInfo.city = :city")
    suspend fun deleteFavoriteCity(city: String)

    @Query("SELECT * FROM weather_table WHERE cityInfo.city = :city")
    suspend fun getFavoriteCity(city: String): WeatherModel

    @Query("SELECT * FROM weather_table BY cityInfo.city ASC")
    suspend fun getAllFavoriteCities(): List<WeatherModel>
}