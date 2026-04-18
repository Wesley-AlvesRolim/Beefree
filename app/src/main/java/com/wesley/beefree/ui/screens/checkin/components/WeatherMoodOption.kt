package com.wesley.beefree.ui.screens.checkin.components

import com.wesley.beefree.R

data class WeatherMoodOption(
    val level: Int,
    val emoji: String,
    val labelRes: Int,
)

val weatherMoodOptions =
    listOf(
        WeatherMoodOption(1, "😣", R.string.check_in_weather_heavy),
        WeatherMoodOption(2, "😟", R.string.check_in_weather_tired),
        WeatherMoodOption(3, "😐", R.string.check_in_weather_steady),
        WeatherMoodOption(4, "😊", R.string.check_in_weather_light),
        WeatherMoodOption(5, "😁", R.string.check_in_weather_bright),
    )
