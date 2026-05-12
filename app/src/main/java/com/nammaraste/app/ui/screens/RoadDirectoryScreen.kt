package com.nammaraste.app.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.nammaraste.app.data.Road
import com.nammaraste.app.viewmodel.RoadViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoadDirectoryScreen(viewModel: RoadViewModel, onRoadClick: (Int) -> Unit) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val roads by viewModel.filteredRoads.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.updateSearchQuery(it) },
            label = { Text("Search Road") },
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(roads) { road ->
                RoadItem(road = road, onClick = { onRoadClick(road.id) })
            }
        }
    }
}

@Composable
fun RoadItem(road: Road, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = road.roadName, style = MaterialTheme.typography.titleMedium)
            Text(text = "Location: ${road.location}", style = MaterialTheme.typography.bodyMedium)
            
            val statusColor = if (road.healthScore > 50) Color(0xFF4CAF50) else Color(0xFFF44336)
            val statusText = if (road.healthScore > 50) "Good" else "Bad"
            
            Text(
                text = "Status: $statusText (${road.healthScore})",
                color = statusColor,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}
