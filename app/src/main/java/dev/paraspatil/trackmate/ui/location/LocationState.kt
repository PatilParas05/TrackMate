package dev.paraspatil.trackmate.ui.location

import dev.paraspatil.trackmate.domain.model.TrackingLocation

data class LocationState (
    val isTracking: Boolean = false,
    val currentLocation: TrackingLocation? = null,
    val teamLocations: List<TrackingLocation> = emptyList(),
    val error: String? = null,
    val locationPermissionGranted: Boolean = false
    )
sealed class LocationEffect{
    data class ShowError(val message: String): LocationEffect()
    object RequestPermission: LocationEffect()
    object NavigateToMap: LocationEffect()
}
