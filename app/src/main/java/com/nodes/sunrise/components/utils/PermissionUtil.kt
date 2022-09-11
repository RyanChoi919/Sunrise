package com.nodes.sunrise.components.utils

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import com.nodes.sunrise.components.listeners.OnPermissionRationaleResultListener
import com.nodes.sunrise.enums.Permission

class PermissionUtil {
    companion object {

        const val TAG = "PermissionUTil.TAG"

        fun hasPermission(context: Context, androidPermission: String): Boolean {
            val result = ActivityCompat.checkSelfPermission(
                context, androidPermission
            ) == PackageManager.PERMISSION_GRANTED
            Log.d(TAG, "hasPermission: $androidPermission = $result")
            return result
        }

        fun requestLocationPermissions(activity: Activity) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    activity,
                    Permission.COARSE_LOCATION.androidName
                ) ||
                ActivityCompat.shouldShowRequestPermissionRationale(
                    activity,
                    Permission.FINE_LOCATION.androidName
                )
            ) {
                AlertDialogUtil.showLocationPermissionRationaleDialog(
                    activity.applicationContext,
                    object : OnPermissionRationaleResultListener {
                        override fun onResultSet(isPositive: Boolean) {
                            when {
                                isPositive -> {
                                    ActivityCompat.requestPermissions(
                                        activity, arrayOf(
                                            Permission.COARSE_LOCATION.androidName,
                                            Permission.FINE_LOCATION.androidName
                                        ), this::class.java.hashCode()
                                    )
                                }
                                else -> {
                                    // do nothing (permission not granted)
                                }
                            }
                        }
                    })
            } else {
                ActivityCompat.requestPermissions(
                    activity, arrayOf(
                        Permission.COARSE_LOCATION.androidName,
                        Permission.FINE_LOCATION.androidName
                    ), this::class.java.hashCode()
                )
            }
        }
    }
}