package dev.paraspatil.trackmate.domain.model

import android.location.Location as AndroidLocation
data class TrackingLocation(
    val id:String,
    val latitude:Double,
    val longitude:Double,
    val accuracy:Float,
    val timestamp: Long,
    val userId:String,
    val userName:String,
    val speed:Float=0f,
    val bearing:Float=0f
){
    companion object{
        fun fromAndroidLocation(
            location:AndroidLocation,
            userId:String,
            userName:String
        ):TrackingLocation = TrackingLocation(
            id = "$userId.${System.currentTimeMillis()}",
            latitude = location.latitude,
            longitude = location.longitude,
            accuracy = location.accuracy,
            timestamp = location.time,
            userId = userId,
            userName = userName,
            speed = location.speed,
            bearing = location.bearing
        )
    }
    fun distanceTo(other:TrackingLocation):Float{
        val result = FloatArray(1)
        AndroidLocation.distanceBetween(
            latitude,
            longitude,
            other.latitude,
            other.longitude,
            result
        )
        return result[0]
    }
}