package com.nodes.sunrise.components.utils

import android.util.Log
import com.nodes.sunrise.components.services.WeatherService
import com.nodes.sunrise.enums.Unit
import com.nodes.sunrise.model.weather.WeatherInfo
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.DecimalFormat
import java.util.*

class WeatherUtil {
    companion object {
        private const val TAG = "WeatherUtil.TAG"

        private const val URL = "https://api.openweathermap.org/data/2.5/"
        private const val API_KEY = "f2a0ea6e64daecf6def7564ad8f421de"

        private val df = DecimalFormat("#.#")

        private val retrofit: Retrofit by lazy {
            Log.d(TAG, "Retrofit: create instance")
            Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        fun getCurrentWeather(
            lat: Double,
            long: Double,
            weatherInfoCallBack: Callback<WeatherInfo>
        ) {
            val weatherService = retrofit.create(WeatherService::class.java)
            Log.d(TAG, "getCurrentWeather: weatherService = $weatherService")

            val weatherCall =
                weatherService.getCurrentWeather(
                    lat.toString(),
                    long.toString(),
                    API_KEY
                )

            Log.d(TAG, "getCurrentWeather: weatherService = $weatherCall")

            weatherCall.enqueue(weatherInfoCallBack)
        }

        fun fromKelvinToLocaleUnit(kelvinTemp: Double?, locale: Locale): String? {
            val unit = getUnitFrom(locale)

            return if (kelvinTemp != null) {
                val degree = when (unit) {
                    Unit.IMPERIAL -> fromKelvinToFahrenheit(kelvinTemp)
                    Unit.METRIC -> fromKelvinToCelsius(kelvinTemp)
                }
                df.format(degree) + unit.tempSymbol
            } else {
                null
            }
        }

        private fun fromKelvinToCelsius(kelvinTemp: Double?): Double? {
            return if (kelvinTemp == null) null else kelvinTemp - 273.15
        }

        private fun fromKelvinToFahrenheit(kelvinTemp: Double?): Double? {
            return if (kelvinTemp == null) null else fromKelvinToCelsius(kelvinTemp)!! * (9 / 5) + 32
        }

        private fun getUnitFrom(locale: Locale): Unit {
            return when (locale.country) {
                "US", "LR", "MM" -> Unit.IMPERIAL
                else -> Unit.METRIC
            }
        }
    }
}