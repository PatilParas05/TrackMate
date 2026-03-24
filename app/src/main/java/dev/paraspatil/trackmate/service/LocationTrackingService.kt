package dev.paraspatil.trackmate.service

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.google.android.gms.location.*
import dagger.hilt.android.AndroidEntryPoint
import dev.paraspatil.trackmate.utils.Constants
import javax.inject.Inject

@AndroidEntryPoint
class LocationTrackingService : Service() {

    @Inject
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private var locationCallback: LocationCallback? = null

    override fun onCreate() {
        super.onCreate()
        NotificationHelper.createNotificationChannels(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> startForegroundService()
            ACTION_STOP -> stopServiceLogic()
        }
        return START_STICKY
    }

    @SuppressLint("MissingPermission")
    private fun startForegroundService() {
        startForeground(
            Constants.LOCATION_SERVICE_NOTIFICATION_ID,
            NotificationHelper.buildLocationNotification(this)
        )

        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            Constants.LOCATION_UPDATE_INTERVAL_MS
        ).apply {
            setMinUpdateIntervalMillis(Constants.LOCATION_FASTEST_INTERVAL_MS)
        }.build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                super.onLocationResult(result)
                result.lastLocation?.let {

                }
            }
        }

        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback!!,
            mainLooper
        )
    }

    private fun stopServiceLogic() {
        locationCallback?.let {
            fusedLocationProviderClient.removeLocationUpdates(it)
        }
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    override fun onDestroy() {
        locationCallback?.let {
            fusedLocationProviderClient.removeLocationUpdates(it)
        }
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    companion object {
        const val ACTION_START = "ACTION_START_LOCATION"
        const val ACTION_STOP = "ACTION_STOP_LOCATION"
    }
}