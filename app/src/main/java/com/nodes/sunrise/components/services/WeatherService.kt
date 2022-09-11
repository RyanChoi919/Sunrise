package com.nodes.sunrise.components.services

import com.nodes.sunrise.model.weather.WeatherInfo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("weather?")
    fun getCurrentWeather(
        @Query("lat") lat: String,
        @Query("lon") long: String,
        @Query("APPID") appId: String
    ): Call<WeatherInfo>
}