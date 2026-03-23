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

val sampleTeamMember = listOf(
    TeamMember("user_1","Sumit","sumit@example.com","manager",isOnline = true),
    TeamMember("user_2","Amit","sumit@example.com","developer",isOnline = true),
    TeamMember("user_3","Vinit","vint@example.com","designer",isOnline = false),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamScreen(){
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(title = { Text("Team") })

        if (sampleTeamMember.isEmpty()){
            Box(modifier = Modifier.fillMaxSize(),contentAlignment = Alignment.Center){
                Text("No team members found.")
            }
        }else{
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(sampleTeamMember, key={it.id}){member ->
                    TeamMemberCard(member = member)
                }
            }
        }
    }
}
@Composable
fun TeamMemberCard(member: TeamMember){
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center){
                    Icon(Icons.Default.Person,contentDescription = null)
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(member.name, style = MaterialTheme.typography.titleMedium)
                Text(member.role, style = MaterialTheme.typography.bodySmall)
                Text(member.email, style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Surface(
                shape = CircleShape,
                color = if (member.isOnline) Color(0xFF4CAF50) else Color.Gray,
                modifier = Modifier.size(10.dp)
            ) { }
        }
    }
}