package dev.paraspatil.trackmate.ui.location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.paraspatil.trackmate.domain.model.TrackingLocation
import dev.paraspatil.trackmate.domain.repository.LocationRepository
import dev.paraspatil.trackmate.domain.model.Result
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val locationRepository: LocationRepository
) : ViewModel() {
    private val _state = MutableStateFlow(LocationState())
    val state: StateFlow<LocationState> = _state

    private val _effect = MutableSharedFlow<LocationEffect>()
    val effect = _effect.asSharedFlow()

    fun processIntent(intent: LocationIntent) {
        when (intent) {
            is LocationIntent.RequestLocationPermission -> {
                viewModelScope.launch {
                    _effect.emit(LocationEffect.RequestPermission)
                }
            }

            is LocationIntent.StartTracking -> startTracking()
            is LocationIntent.StopTracking -> stopTracking()
            is LocationIntent.UpdateLocation -> updateLocation(intent.location)
            is LocationIntent.ClearError -> clearError()
        }
    }

    private fun startTracking() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isTracking = true, isLoading = true)

            locationRepository.observeCurrentLocation().collect { result ->
                when (result) {
                    is Result.Success -> {
                        _state.value = _state.value.copy(
                            currentLocation = result.data,
                            isLoading = false,
                            error = null
                        )
                    }

                    is Result.Error -> {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            error = result.exception.message
                        )
                        _effect.emit(
                            LocationEffect.ShowError(
                                result.exception.message ?: "Unknown error"
                            )
                        )
                    }

                    is Result.Loading -> {
                        _state.value = _state.value.copy(isLoading = true)
                    }
                }
            }
        }
    }

    private fun stopTracking() {
        viewModelScope.launch {
            when (val result = locationRepository.stopLocationUpdates()) {
                is Result.Success -> {
                    _state.value = _state.value.copy(isTracking = false,isLoading = false)
                }
                is Result.Error -> {
                    _effect.emit(LocationEffect.ShowError(result.exception.message ?: "Failed to stop tracking"))
                }

                else -> {}
            }
        }
    }
    private fun updateLocation(location: TrackingLocation) {
        viewModelScope.launch {
            _state.value = _state.value.copy(currentLocation = location)
        }
    }

    private fun clearError() {
        _state.value = _state.value.copy(error = null)
    }

    fun setLocationPermissionGranted(granted: Boolean) {
        _state.value = _state.value.copy(locationPermissionGranted = granted)
    }
}
