package dev.paraspatil.trackmate.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.unit.dp
import dev.paraspatil.trackmate.domain.model.TrackingLocation
import dev.paraspatil.trackmate.utils.toFormattedTime

@Composable
fun LocationCard (location: TrackingLocation){
    Card(
        modifier = Modifier.fillMaxSize(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Surface(
                shape = CircleShape,
                color = Green,
                modifier = Modifier.size(10.dp)
            ) { }
            Icon(
                Icons.Default.LocationOn,
                contentDescription = "Location",
                tint = Green,
                modifier = Modifier.size(20.dp)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(location.userName,style = MaterialTheme.typography.titleSmall)
                Text(
                    "%.5f, %.5f".format(location.latitude, location.longitude),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                location.timestamp.toFormattedTime(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
object Extension{
    fun Long.toFormattedTime(): String{
        val sdf = java.text.SimpleDateFormat("HH:mm:ss",java.util.Locale.getDefault())
        return  sdf.format(java.util.Date(this))
    }
}