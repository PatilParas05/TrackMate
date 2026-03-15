package dev.paraspatil.trackmate.domain.repository

import dev.paraspatil.trackmate.domain.model.TrackingLocation
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    fun observeCurrentLocation(): Flow<Result<TrackingLocation>>
    suspend fun saveLocation(location: TrackingLocation): Result<Unit>
    suspend fun getTeamLocation(teamId: String): Result<List<TrackingLocation>>
    fun observeTeamLocation(teamId: String): Flow<Result<List<TrackingLocation>>>
    suspend fun startLocationUpdates(): Result<Unit>
    suspend fun stopLocationUpdates(): Result<Unit>
}
sealed class Result<out T>{
    data class Success<out T>(val data:T):Result<T>()
    data class Error(val exception: Throwable):Result<Nothing>()
    object Loading:Result<Nothing>()
}