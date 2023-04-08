package com.example.weather_app.data

import android.content.Context
import androidx.room.*

@Database(entities = [WeatherModel::class], version = 3, exportSchema = false)
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
                    "weatherDB"
                ).allowMainThreadQueries().build().also {
                    INSTANCE = it
                }
            }
        }
    }
}
