package com.nammaraste.app.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.nammaraste.app.viewmodel.RoadViewModel

@Composable
fun SuccessMapScreen(viewModel: RoadViewModel) {
    // Map needs all roads, not just search results
    val roads by viewModel.allRoads.collectAsState()

    // Default center to Bengaluru
    val center = LatLng(12.9716, 77.5946)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(center, 11f)
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        roads.forEachIndexed { index, road ->
            // Creating dummy coordinates around Bengaluru for each road
            val offsetLat = index * 0.02
            val startPoint = LatLng(12.9716 + offsetLat, 77.5946)
            val endPoint = LatLng(12.9716 + offsetLat, 77.6546)
            
            val color = if (road.healthScore > 50) Color.Green else Color.Red
            
            Polyline(
                points = listOf(startPoint, endPoint),
                color = color,
                width = 10f
            )
            
            Marker(
                state = MarkerState(position = startPoint),
                title = road.roadName,
                snippet = "Score: ${road.healthScore}"
            )
        }
    }
}
