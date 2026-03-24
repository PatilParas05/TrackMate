package dev.paraspatil.trackmate.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import dev.paraspatil.trackmate.MainActivity
import dev.paraspatil.trackmate.utils.Constants
import kotlin.jvm.java

object NotificationHelper {

    fun createNotificationChannels(context: Context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = context.getSystemService(NotificationManager::class.java)

            val locationChannel = NotificationChannel(
                Constants.LOCATION_CHANNEL_ID,
                Constants.LOCATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Shows when live location tracking is active"
                setShowBadge(false)
            }

            val alertChannel = NotificationChannel(
                Constants.ALERT_CHANNEL_ID,
                Constants.ALERT_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Team alerts and task notifications"
                setShowBadge(true)
            }
             manager.createNotificationChannel(locationChannel)
            manager.createNotificationChannel(alertChannel)
        }
    }

    fun buildLocationNotification(context: Context): Notification {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            context,0, intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(context, Constants.LOCATION_CHANNEL_ID)
            .setContentTitle("TrackMate is tracking your location")
            .setContentText("Your eam can see your live location")
            .setSmallIcon(android.R.drawable.ic_menu_mylocation)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
            .build()
    }
    fun sendAlertNotification(context: Context,title: String,message: String) {
    val manager = context.getSystemService(NotificationManager::class.java)
    val notification = NotificationCompat.Builder(context, Constants.ALERT_CHANNEL_ID)
        .setContentTitle(title)
        .setContentText(message)
        .setSmallIcon(android.R.drawable.ic_dialog_alert)
        .setAutoCancel(true)
        .build()
manager.notify(System.currentTimeMillis().toInt(),notification)
    }
}