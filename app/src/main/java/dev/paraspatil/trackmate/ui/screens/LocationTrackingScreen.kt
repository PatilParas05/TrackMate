package dev.paraspatil.trackmate.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import dev.paraspatil.trackmate.ui.composables.LoadingScreen
import dev.paraspatil.trackmate.ui.location.LocationIntent
import dev.paraspatil.trackmate.ui.location.LocationViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationTrackingScreen(
    contentPadding: PaddingValues = PaddingValues(0.dp),
    viewModel: LocationViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsState()
    val locationPermission = rememberPermissionState(
        android.Manifest.permission.ACCESS_FINE_LOCATION
    )
    LaunchedEffect(Unit) {
        if (!locationPermission.status.isGranted) {
            viewModel.processIntent(LocationIntent.RequestLocationPermission)
        } else {
            viewModel.setLocationPermissionGranted(true)
        }
    }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
        ){
            if(state.value.isLoading){
                LoadingScreen("Getting your location...")
            }else{
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 16.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = "Location",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                            state.value.currentLocation?.let { location ->
                                Text(
                                    text = "Current Location",
                                    style = MaterialTheme.typography.headlineSmall
                                )
                                Text(
                                    text = "Lat: ${String.format("%.6f",location.latitude)}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = "Lng: ${String.format("%.6f",location.longitude)}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = "Accuracy: ${String.format("%.2f", location.accuracy)}m",
                                    style = MaterialTheme.typography.bodySmall
                                )
                                Text(
                                    text = "Updated: ${formatTime(location.timestamp)}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            } ?: run {
                                Text(
                                    text = "No location data available",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (!locationPermission.status.isGranted){
                            Button(
                                onClick = { locationPermission.launchPermissionRequest()},
                                    modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Grant Location Permission")
                            }
                        }else{
                            Button(
                                onClick = {
                                    if (state.value.isTracking){
                                        viewModel.processIntent(LocationIntent.StopTracking)
                                    }else{
                                        viewModel.processIntent(LocationIntent.StartTracking)
                                    }
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(if (state.value.isTracking) "Stop Tracking" else "Start Tracking")
                            }
                        }
                        state.value.error?.let { error ->
                            Text(
                                text = "Error :$error",
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        }
}

private fun formatTime(timestamp: Long): String {
    return SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date(timestamp))
}