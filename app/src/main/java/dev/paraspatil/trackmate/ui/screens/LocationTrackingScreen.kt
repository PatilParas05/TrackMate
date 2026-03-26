package dev.paraspatil.trackmate.ui.screens

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOff
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import dev.paraspatil.trackmate.domain.model.TrackingLocation
import dev.paraspatil.trackmate.service.LocationTrackingService
import dev.paraspatil.trackmate.ui.composables.LoadingScreen
import dev.paraspatil.trackmate.ui.location.LocationIntent
import dev.paraspatil.trackmate.ui.location.LocationViewModel
import dev.paraspatil.trackmate.ui.theme.OnlineGreen
import dev.paraspatil.trackmate.utils.toFormattedTime

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun LocationTrackingScreen(
    contentPadding: PaddingValues = PaddingValues(),
    viewModel: LocationViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    val locationPermission = rememberPermissionState(android.Manifest.permission.ACCESS_FINE_LOCATION)

    LaunchedEffect(locationPermission.status.isGranted) {
        viewModel.setLocationPermissionGranted(locationPermission.status.isGranted)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
    ) {
        TopAppBar(title = { Text("Live Location") })

        if (!locationPermission.status.isGranted) {
            PermissionRationaleCard(
                showRationale = locationPermission.status.shouldShowRationale,
                onRequest = { locationPermission.launchPermissionRequest() }
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    TrackingStatusCard(
                        isTracking = state.isTracking,
                        isLoading = state.isLoading,
                        onStart = {
                            viewModel.processIntent(LocationIntent.StartTracking)
                            val intent = Intent(context, LocationTrackingService::class.java).apply {
                                action = LocationTrackingService.ACTION_START
                            }
                            context.startForegroundService(intent)
                        },
                        onStop = {
                            viewModel.processIntent(LocationIntent.StopTracking)
                            val intent = Intent(context, LocationTrackingService::class.java).apply {
                                action = LocationTrackingService.ACTION_STOP
                            }
                            context.startService(intent)
                        }
                    )
                }

                state.currentLocation?.let { loc ->
                    item { CurrentLocationCard(loc) }
                }

                if (state.error != null) {
                    item {
                        Text(
                            "⚠ ${state.error}",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )
                    }
                }

                if (state.teamLocations.isNotEmpty()) {
                    item {
                        Text("Team Members", style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(top = 4.dp))
                    }
                    items(state.teamLocations) { loc ->
                        TeamMemberLocationCard(loc)
                    }
                }
            }
        }
    }
}

@Composable
fun TrackingStatusCard(
    isTracking: Boolean,
    isLoading: Boolean,
    onStart: () -> Unit,
    onStop: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isTracking)
                OnlineGreen.copy(alpha = 0.12f)
            else
                MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Surface(
                shape = CircleShape,
                color = if (isTracking) OnlineGreen else Color.Gray,
                modifier = Modifier.size(56.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        if (isTracking) Icons.Default.LocationOn else Icons.Default.LocationOff,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
            Text(
                if (isTracking) "Location Sharing Active" else "Location Sharing Off",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                if (isTracking) "Your team can see your live location"
                else "Start sharing to let your team see you",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (isLoading) {
                LoadingScreen("Getting GPS signal...")
            } else if (isTracking) {
                OutlinedButton(
                    onClick = onStop,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Icon(Icons.Default.LocationOff, null, modifier = Modifier.size(18.dp))
                    Text("  Stop Sharing")
                }
            } else {
                Button(onClick = onStart, modifier = Modifier.fillMaxWidth()) {
                    Icon(Icons.Default.LocationOn, null, modifier = Modifier.size(18.dp))
                    Text("  Start Sharing")
                }
            }
        }
    }
}

@Composable
fun CurrentLocationCard(location: TrackingLocation) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Default.MyLocation, null, tint = MaterialTheme.colorScheme.primary)
                Text("My Current Location", style = MaterialTheme.typography.titleSmall)
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                LocationInfoItem("Latitude", "%.6f".format(location.latitude))
                LocationInfoItem("Longitude", "%.6f".format(location.longitude))
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                LocationInfoItem("Accuracy", "%.1fm".format(location.accuracy))
                LocationInfoItem("Speed", "%.1f km/h".format(location.speed * 3.6f))
            }
            Text(
                "Last updated: ${location.timestamp.toFormattedTime()}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun LocationInfoItem(label: String, value: String) {
    Column {
        Text(label, style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun TeamMemberLocationCard(location: TrackingLocation) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Surface(shape = CircleShape, color = OnlineGreen, modifier = Modifier.size(10.dp)) {}
            Icon(Icons.Default.LocationOn, null,
                tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(location.userName, style = MaterialTheme.typography.titleSmall)
                Text(
                    "%.5f, %.5f".format(location.latitude, location.longitude),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(location.timestamp.toFormattedTime(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun PermissionRationaleCard(showRationale: Boolean, onRequest: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(Icons.Default.LocationOff, null,
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.error)
            Text("Location Permission Required",
                style = MaterialTheme.typography.titleMedium)
            Text(
                if (showRationale)
                    "TrackMate needs location access to share your position with teammates. Please allow location access."
                else
                    "Grant location permission so your team can see where you are in real time.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
            Button(onClick = onRequest) { Text("Grant Permission") }
        }
    }
}