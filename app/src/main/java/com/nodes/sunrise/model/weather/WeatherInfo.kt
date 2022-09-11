package com.nodes.sunrise.model.weather

import com.google.gson.annotations.SerializedName

data class WeatherInfo (
    @SerializedName("weather") var weather: ArrayList<Weather> = ArrayList(),
    @SerializedName("main") var main: Main? = null
)