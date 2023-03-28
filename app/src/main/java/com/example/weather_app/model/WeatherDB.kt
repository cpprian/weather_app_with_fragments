package com.example.weather_app.model

import android.content.Context
import androidx.room.*

@Database(entities = [WeatherModel::class], version = 1, exportSchema = false)
abstract class WeatherDB: RoomDatabase() {
    abstract fun weatherDao(): WeatherDao

    companion object {
        @Volatile
        private var INSTANCE: WeatherDB? = null

        fun getDatabase(context: Context): WeatherDB {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    WeatherDB::class.java,
                    "weather_database"
                ).build().also {
                    INSTANCE = it
                }
            }
        }
    }
}
