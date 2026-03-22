package dev.paraspatil.trackmate.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.paraspatil.trackmate.domain.model.Task

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskCard(

){
    Card(
        modifier = Modifier.fillMaxSize(),
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(text = "Task Title", style = MaterialTheme.typography.titleMedium)

            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                SuggestionChip(
                    onClick = { /*TODO*/ },
                    label = { Text("Priority") }
                )
                SuggestionChip(
                    onClick = { /*TODO*/ },
                    label = { Text("Status") }
                )
            }
            Text("Due")
        }
    }
}
@Preview
@Composable
fun TaskCardPreview(){
    TaskCard()
}