package com.example.weather_app.util


fun fromStringToList(stringList: String): List<String> {
    if (stringList.isEmpty()) return emptyList()
    return stringList.split(",").map { it }
}

fun fromListToString(stringList: List<String>): String {
    if (stringList.isEmpty()) return ""
    return stringList.joinToString(",")
}

fun fromStringToListDouble(stringList: String): List<Double> {
    if (stringList.isEmpty()) return emptyList()
    return stringList.split(",").map { it.toDouble() }
}

fun fromListDoubleToString(stringList: List<Double>): String {
    if (stringList.isEmpty()) return ""
    return stringList.joinToString(",")
}
