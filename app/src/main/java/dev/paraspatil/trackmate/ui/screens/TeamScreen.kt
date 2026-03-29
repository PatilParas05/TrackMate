package dev.paraspatil.trackmate.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.paraspatil.trackmate.domain.model.TeamMember
import dev.paraspatil.trackmate.ui.theme.OfflineGray
import dev.paraspatil.trackmate.ui.theme.OnlineGreen
import dev.paraspatil.trackmate.utils.timeAgo

// Sample data — wire up to a TeamMemberViewModel + Room when you add auth
val sampleTeam = listOf(
    TeamMember("user_1", "Alice Johnson", "alice@company.com", "Team Lead",
        isOnline = true, lastSeenAt = System.currentTimeMillis()),
    TeamMember("user_2", "Bob Smith", "bob@company.com", "Field Agent",
        isOnline = false, lastSeenAt = System.currentTimeMillis() - 900_000),
    TeamMember("user_3", "Charlie Brown", "charlie@company.com", "Field Agent",
        isOnline = true, lastSeenAt = System.currentTimeMillis()),
    TeamMember("user_4", "Diana Prince", "diana@company.com", "Coordinator",
        isOnline = false, lastSeenAt = System.currentTimeMillis() - 7_200_000)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamScreen(contentPadding: PaddingValues = PaddingValues()) {
    val online = sampleTeam.filter { it.isOnline }
    val offline = sampleTeam.filter { !it.isOnline }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(contentPadding)) {
        TopAppBar(
            title = {
                Column {
                    Text("Team")
                    Text("${online.size} online • ${sampleTeam.size} total",
                        style = MaterialTheme.typography.labelSmall,
                        color = OnlineGreen)
                }
            }
        )
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Text("Online", style = MaterialTheme.typography.titleSmall,
                    color = OnlineGreen, modifier = Modifier.padding(vertical = 4.dp))
            }
            items(online) { TeamMemberCard(it) }

            item {
                Text("Offline", style = MaterialTheme.typography.titleSmall,
                    color = OfflineGray, modifier = Modifier.padding(top = 12.dp, bottom = 4.dp))
            }
            items(offline) { TeamMemberCard(it) }
        }
    }
}

@Composable
fun TeamMemberCard(member: TeamMember) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.size(48.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Person, null,
                            modifier = Modifier.size(28.dp),
                            tint = MaterialTheme.colorScheme.onPrimaryContainer)
                    }
                }
                Surface(
                    shape = CircleShape,
                    color = if (member.isOnline) OnlineGreen else OfflineGray,
                    modifier = Modifier
                        .size(12.dp)
                        .align(Alignment.BottomEnd)
                ) {}
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(member.name, style = MaterialTheme.typography.titleSmall)
                Text(member.role, style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary)
                Text(member.email, style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            Column(horizontalAlignment = Alignment.End) {
                Surface(
                    shape = CircleShape,
                    color = if (member.isOnline) OnlineGreen.copy(0.15f) else OfflineGray.copy(0.15f)
                ) {
                    Text(
                        if (member.isOnline) "Online" else "Offline",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (member.isOnline) OnlineGreen else OfflineGray,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
                if (!member.isOnline) {
                    Text(member.lastSeenAt.timeAgo(),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 4.dp))
                }
            }
        }
    }
}