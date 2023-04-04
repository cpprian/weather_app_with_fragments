package com.example.weather_app.util


fun fromStringToList(stringList: String): List<String> {
    return stringList.split(",").map { it }
}

fun fromListToString(stringList: List<String>): String {
    return stringList.joinToString(",")
}

fun fromStringToListDouble(stringList: String): List<Double> {
    return stringList.split(",").map { it.toDouble() }
}

fun fromListDoubleToString(stringList: List<Double>): String {
    return stringList.joinToString(",")
}
