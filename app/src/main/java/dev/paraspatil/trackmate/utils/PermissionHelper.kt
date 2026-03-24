package dev.paraspatil.trackmate.utils

import android.os.Build

object PermissionHelper {
    val locationPermissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        arrayOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }else{
        arrayOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }

    val notificationPermission = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
     arrayOf(android.Manifest.permission.POST_NOTIFICATIONS)
    }else{
        emptyArray()
    }
}