package dev.paraspatil.trackmate.domain.repository

import dev.paraspatil.trackmate.domain.model.TrackingLocation
import kotlinx.coroutines.flow.Flow
import dev.paraspatil.trackmate.domain.model.Result

interface LocationRepository {
    fun observeCurrentLocation(): Flow<Result<TrackingLocation>>
    suspend fun saveLocation(location: TrackingLocation): Result<Unit>
    suspend fun getTeamLocations(teamId: String): Result<List<TrackingLocation>>
    fun observeTeamLocations(teamId: String): Flow<Result<List<TrackingLocation>>>
    suspend fun startLocationUpdates(): Result<Unit>
    suspend fun stopLocationUpdates(): Result<Unit>
}
