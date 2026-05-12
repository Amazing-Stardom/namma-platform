package com.nammaraste.health.ui.screen.contractorinfo

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nammaraste.health.data.local.entity.ContractorEntity
import com.nammaraste.health.ui.components.TopAppBarCustom
import com.nammaraste.health.ui.theme.DamageRed
import com.nammaraste.health.ui.theme.RoadGreen
import com.nammaraste.health.ui.theme.WarrantyBadgeGreen
import com.nammaraste.health.ui.theme.WarrantyBadgeRed

@Composable
fun ContractorInfoScreen(
    roadId: Int,
    onBack: () -> Unit,
    viewModel: ContractorInfoViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(roadId) {
        viewModel.loadData(roadId)
    }

    Scaffold(
        topBar = {
            TopAppBarCustom(title = "Contractor Information", onBackClick = onBack)
        }
    ) { padding ->
        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentPadding = PaddingValues(16.dp)
            ) {
                uiState.road?.let { road ->
                    item {
                        RoadHealthCard(road.roadName, road.location, road.healthScore)
                        Spacer(modifier = Modifier.height(16.dp))
                        DigitalLifeBook(road.totalLengthKm, road.constructedYear, uiState.reportCount, road.healthScore)
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

                uiState.contractor?.let { contractor ->
                    item {
                        ContractorDetailsCard(contractor) { number ->
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$number"))
                            context.startActivity(intent)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        WarrantyCard(contractor.warrantyPeriod, contractor.warrantyExpired)
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

                if (uiState.reportCount > 0) {
                    item {
                        DamageAlertBanner(uiState.reportCount)
                    }
                }
            }
        }
    }
}

@Composable
fun RoadHealthCard(name: String, location: String, score: Int) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(name, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text(location, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    progress = { score / 100f },
                    modifier = Modifier.size(100.dp),
                    strokeWidth = 8.dp,
                    color = if (score > 50) RoadGreen else DamageRed
                )
                Text("$score / 100", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(8.dp))
            val status = when {
                score >= 80 -> "EXCELLENT CONDITION"
                score >= 60 -> "GOOD CONDITION"
                score >= 40 -> "MODERATE CONDITION"
                else -> "CRITICAL CONDITION"
            }
            Text(status, color = if (score > 50) RoadGreen else DamageRed, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun DigitalLifeBook(length: Float, year: Int, reports: Int, score: Int) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Road Digital Life-Book", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            InfoRow("📏 Total Length", "$length km")
            InfoRow("📅 Constructed", year.toString())
            InfoRow("🏗️ Total Reports", reports.toString())
            InfoRow("📊 Current Score", "$score / 100")
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label)
        Text(value, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun ContractorDetailsCard(contractor: ContractorEntity, onCall: (String) -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Contractor Information", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            Text(contractor.contractorName, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(contractor.contractorPhone, modifier = Modifier.weight(1f))
                IconButton(onClick = { onCall(contractor.contractorPhone) }) {
                    Icon(Icons.Default.Call, contentDescription = "Call", tint = RoadGreen)
                }
            }
            Text(contractor.contractorEmail)
            Text("Value: ${contractor.contractValue}", modifier = Modifier.padding(top = 8.dp))
            Text("Period: ${contractor.startDate} - ${contractor.completionDate}")
        }
    }
}

@Composable
fun WarrantyCard(period: String, expired: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = if (expired) WarrantyBadgeRed else WarrantyBadgeGreen)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    if (expired) Icons.Default.Info else Icons.Default.Business,
                    contentDescription = null,
                    tint = if (expired) DamageRed else RoadGreen
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    if (expired) "Warranty EXPIRED" else "Warranty ACTIVE",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (expired) DamageRed else RoadGreen
                )
            }
            Text(period, color = if (expired) DamageRed else RoadGreen)
            Text(
                if (expired) "Repairs are now public expenditure" else "Report defects within warranty period",
                style = MaterialTheme.typography.bodySmall,
                color = if (expired) DamageRed else RoadGreen
            )
        }
    }
}

@Composable
fun DamageAlertBanner(count: Int) {
    val color = if (count >= 5) DamageRed else Color(0xFFFFA000)
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f))
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.ReportProblem, contentDescription = null, tint = color)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "${if (count >= 5) "High" else "Moderate"} Damage Alert — $count reports filed",
                color = color,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
