package com.nodes.sunrise.components.utils

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import java.util.*

class LocationUtil {
    companion object {
        fun getAddressFromLocation(context: Context, location: Location): Address {
            return Geocoder(context, Locale.getDefault()).getFromLocation(
                location.latitude,
                location.longitude,
                1
            )[0]
        }

        fun getAddressFromLatLong(context: Context, latitude: Double, longitude: Double): Address {
            return Geocoder(context, Locale.getDefault()).getFromLocation(
                latitude, longitude, 1
            )[0]
        }
    }
}