package com.nammaraste.health.ui.screen.successmap

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.nammaraste.health.ui.components.TopAppBarCustom
import com.nammaraste.health.ui.theme.DamageRed
import com.nammaraste.health.ui.theme.RoadGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuccessMapScreen(
    viewModel: SuccessMapViewModel = hiltViewModel()
) {
    val allRoads by viewModel.allRoads.collectAsStateWithLifecycle()
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(12.5833, 78.1833), 10f)
    }

    Scaffold(
        topBar = {
            TopAppBarCustom(title = "Road Health Map", subtitle = "Krishnagiri District, Tamil Nadu")
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                LegendChip("Healthy Road", RoadGreen)
                LegendChip("Damaged Road", DamageRed)
            }

            Box(modifier = Modifier.weight(1f)) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState
                ) {
                    allRoads.forEach { road ->
                        val start = LatLng(road.latitude, road.longitude)
                        val end = LatLng(road.endLatitude, road.endLongitude)
                        val color = if (road.healthScore > 50) RoadGreen else DamageRed

                        Polyline(
                            points = listOf(start, end),
                            color = color,
                            width = 12f
                        )

                        Marker(
                            state = MarkerState(position = start),
                            title = road.roadName,
                            snippet = "Score: ${road.healthScore}/100"
                        )
                    }
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                shape = MaterialTheme.shapes.large
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Road Status List", style = MaterialTheme.typography.titleMedium)
                    LazyColumn {
                        items(allRoads) { road ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(12.dp)
                                        .background(
                                            if (road.healthScore > 50) RoadGreen else DamageRed,
                                            MaterialTheme.shapes.small
                                        )
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(road.roadName, modifier = Modifier.weight(1f))
                                Text("${road.healthScore}/100", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LegendChip(label: String, color: Color) {
    Surface(
        color = color.copy(alpha = 0.1f),
        shape = MaterialTheme.shapes.small,
        border = androidx.compose.foundation.BorderStroke(1.dp, color)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.size(8.dp).background(color, MaterialTheme.shapes.small))
            Spacer(modifier = Modifier.width(4.dp))
            Text(label, style = MaterialTheme.typography.labelSmall, color = color)
        }
    }
}
