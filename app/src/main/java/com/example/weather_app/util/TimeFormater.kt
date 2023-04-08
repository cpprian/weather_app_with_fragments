package com.example.weather_app.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
fun parseTime(time: String): LocalDateTime {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")
    return LocalDateTime.parse(time, formatter)
}

@RequiresApi(Build.VERSION_CODES.O)
fun parseDate(time: String): LocalDate? {
    if (time == "0") {
        return LocalDate.now()
    }
    return LocalDate.parse(time)
}