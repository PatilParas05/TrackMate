package dev.paraspatil.trackmate.feature.map

import java.lang.reflect.Member

sealed class MapState {
    object Idle : MapState()
    object Loading : MapState()
    data class Success(val members: List<Member>) : MapState()
    data class LocationUpdated(val lat: Double, val lng: Double) : MapState()
    data class Error(val message: String) : MapState()
    object SOSSent : MapState()



}