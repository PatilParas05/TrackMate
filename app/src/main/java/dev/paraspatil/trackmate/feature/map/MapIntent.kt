package dev.paraspatil.trackmate.feature.map

sealed class MapIntent {
    object LoadMembers : MapIntent()
    data class UpdateLocation(val lat: Double, val lng: Double) : MapIntent()
    object TriggerSOS : MapIntent()
    object RefreshMap : MapIntent()
}
