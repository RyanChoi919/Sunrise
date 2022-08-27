package com.nodes.sunrise.enums

import android.Manifest

enum class Permission(val androidName: String) {
    FINE_LOCATION(Manifest.permission.ACCESS_FINE_LOCATION),
    COARSE_LOCATION(Manifest.permission.ACCESS_COARSE_LOCATION)
}