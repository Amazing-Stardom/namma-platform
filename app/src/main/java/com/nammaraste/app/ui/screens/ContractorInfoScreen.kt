package com.nammaraste.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
fun ContractorInfoScreen(roadId: Int?, viewModel: RoadViewModel) {
    var road by remember { mutableStateOf<Road?>(null) }

    LaunchedEffect(roadId) {
        if (roadId != null) {
            road = viewModel.getRoadById(roadId)
        }
    }

    if (road == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Select a road from Directory to view details.")
        }
        return
    }

    val r = road!!
    val isGood = r.healthScore > 50
    val badgeColor = if (isGood) Color(0xFF4CAF50) else Color(0xFFF44336)
    val badgeText = if (isGood) "Healthy" else "Requires Maintenance"

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Contractor Information", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Road: ${r.roadName}", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Contractor: ${r.contractorName}", style = MaterialTheme.typography.bodyLarge)
                Text(text = "Contact Number: ${r.contactNumber}", style = MaterialTheme.typography.bodyLarge)
                Text(text = "Warranty Period: ${r.warrantyPeriod}", style = MaterialTheme.typography.bodyLarge)
                Text(text = "Health Score: ${r.healthScore}", style = MaterialTheme.typography.bodyLarge)
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Box(
                    modifier = Modifier
                        .background(color = badgeColor, shape = RoundedCornerShape(8.dp))
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(text = badgeText, color = Color.White)
                }
            }
        }
    }
}
