package dev.paraspatil.trackmate.ui.location

import dev.paraspatil.trackmate.domain.model.TrackingLocation

sealed class LocationIntent {
    object RequestLocationPermission : LocationIntent()
    object StartTracking : LocationIntent()
    object StopTracking : LocationIntent()
    data class UpdateLocation(val location: TrackingLocation): LocationIntent()
    object ClearError : LocationIntent()
}