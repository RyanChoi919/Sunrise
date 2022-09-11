package com.nodes.sunrise.components.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.nodes.sunrise.components.listeners.OnPermissionRationaleResultListener
import com.nodes.sunrise.enums.Permission
import java.util.*

class LocationUtil {
    companion object {
        fun getAddressFromLatLong(context: Context, latitude: Double, longitude: Double): Address {
            return Geocoder(context, Locale.getDefault()).getFromLocation(
                latitude, longitude, 1
            )[0]
        }

        fun getAddressFromLocation(context: Context, location: Location): Address {
            return getAddressFromLatLong(context, location.latitude, location.longitude)
        }

        @SuppressLint("MissingPermission")
        fun getCurrentLocation(activity: Activity, onSuccessListener: OnSuccessListener<Location>) {
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)

            val hasFineLocationPermission =
                PermissionUtil.hasPermission(activity, Permission.FINE_LOCATION.androidName)
            val hasCoarseLocationPermission =
                PermissionUtil.hasPermission(activity, Permission.COARSE_LOCATION.androidName)

            if (hasFineLocationPermission && hasCoarseLocationPermission) {
                fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, object :
                    CancellationToken() {
                    override fun onCanceledRequested(p0: OnTokenCanceledListener): CancellationToken {
                        return CancellationTokenSource().token
                    }

                    override fun isCancellationRequested(): Boolean {
                        return false
                    }
                }).addOnSuccessListener(onSuccessListener)
            } else {
                PermissionUtil.requestLocationPermissions(activity)
            }
        }
    }
}