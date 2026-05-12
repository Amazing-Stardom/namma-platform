package com.nammaraste.health.ui.screen.roaddirectory

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Route
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nammaraste.health.ui.components.RoadCard
import com.nammaraste.health.ui.components.TopAppBarCustom

@Composable
fun RoadDirectoryScreen(
    onReportDamage: (Int) -> Unit,
    onViewContractor: (Int) -> Unit,
    viewModel: RoadDirectoryViewModel = hiltViewModel()
) {
    val roads by viewModel.roads.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val totalCount by viewModel.totalRoadsCount.collectAsStateWithLifecycle()
    val healthyCount by viewModel.healthyRoadsCount.collectAsStateWithLifecycle()
    val damagedCount by viewModel.damagedRoadsCount.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBarCustom(title = "Namma-Raste Health", subtitle = "Road Directory")
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            // Search Bar Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = viewModel::onSearchQueryChange,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Search road name or location...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    shape = MaterialTheme.shapes.medium,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    singleLine = true
                )
            }

            // Stats Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatCard(label = "Total", count = totalCount, icon = Icons.Default.Route, modifier = Modifier.weight(1f))
                StatCard(label = "Healthy", count = healthyCount, icon = Icons.Default.CheckCircle, color = Color(0xFF2E7D32), modifier = Modifier.weight(1f))
                StatCard(label = "Damaged", count = damagedCount, icon = Icons.Default.ReportProblem, color = Color(0xFFC62828), modifier = Modifier.weight(1f))
            }

            // Road List
            if (roads.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(64.dp), tint = Color.LightGray)
                        Text("No roads found", style = MaterialTheme.typography.bodyLarge, color = Color.Gray)
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(roads) { road ->
                        RoadCard(
                            road = road,
                            onReportClick = { onReportDamage(road.id) },
                            onContractorClick = { onViewContractor(road.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StatCard(label: String, count: Int, icon: ImageVector, color: Color = MaterialTheme.colorScheme.primary, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
            Text(text = count.toString(), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(text = label, style = MaterialTheme.typography.labelSmall)
        }
    }
}
