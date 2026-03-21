package dev.paraspatil.trackmate.data.repository

import android.annotation.SuppressLint
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import dev.paraspatil.trackmate.domain.model.Result
import dev.paraspatil.trackmate.domain.model.TrackingLocation
import dev.paraspatil.trackmate.domain.repository.LocationRepository
import javax.inject.Inject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlin.collections.emptyList

class LocationRepositoryImpl @Inject constructor(
    private val fusedLocationClient: FusedLocationProviderClient
) : LocationRepository {

    private var currentLocationCallBack: LocationCallback? = null
    private var currentUserId:String ="user_1"
    private var currentUserName: String ="Current User"

    override fun observeCurrentLocation(): Flow<Result<TrackingLocation>> =
        callbackFlow{
            val locationCallback = object : LocationCallback(){
                override fun onLocationResult(result: LocationResult) {
                    val location = result.lastLocation?:return
                    val trackingLocation = TrackingLocation.fromAndroidLocation(
                        location,
                        currentUserId,
                        currentUserName
                    )
                    trySend(Result.Success(trackingLocation))
                }
            }
            try {
                val locationRequest = LocationRequest.Builder(5000L)
                    .setMinUpdateIntervalMillis(2000L)
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .build()

                @SuppressLint("MissingPermission")
                fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    null
                )
            currentLocationCallBack = locationCallback

                awaitClose {
                    fusedLocationClient.removeLocationUpdates(locationCallback)
                }
            }catch (e:Exception){
                trySend(Result.Error(e))
                close(e)
            }
        }

    override suspend fun saveLocation(location: TrackingLocation): Result<Unit> {
        return try {
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getTeamLocations(teamId: String): Result<List<TrackingLocation>> {
        return try {
            Result.Success(emptyList())
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override fun observeTeamLocations(teamId: String): Flow<Result<List<TrackingLocation>>> = callbackFlow {
        try {
            trySend(Result.Success(emptyList()))
            awaitClose { }
        } catch (e: Exception) {
            trySend(Result.Error(e))
        }
    }

    override suspend fun startLocationUpdates(): Result<Unit> {
        return try {
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun stopLocationUpdates(): Result<Unit> {
        return try {
            currentLocationCallBack?.let {
                fusedLocationClient.removeLocationUpdates(it)
                currentLocationCallBack = null
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}

