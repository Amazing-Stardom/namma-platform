package com.nammaraste.health.ui.screen.damagereport

import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.nammaraste.health.ui.components.DamageReportCard
import com.nammaraste.health.ui.components.HealthBadge
import com.nammaraste.health.ui.components.TopAppBarCustom
import com.nammaraste.health.ui.theme.DamageRed
import com.nammaraste.health.utils.CameraHelper
import com.nammaraste.health.utils.LocationHelper
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun DamageReportScreen(
    roadId: Int,
    onBack: () -> Unit,
    viewModel: DamageReportViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    val locationPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success && imageUri != null) {
            viewModel.onPhotoTaken(imageUri!!)
        }
    }

    LaunchedEffect(roadId) {
        viewModel.loadRoad(roadId)
    }

    LaunchedEffect(uiState.isSubmitted) {
        if (uiState.isSubmitted) {
            scope.launch {
                snackbarHostState.showSnackbar("Report submitted successfully!")
                onBack()
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBarCustom(
                title = "Report Road Damage",
                onBackClick = onBack,
                containerColor = DamageRed
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ) {
            uiState.road?.let { road ->
                item {
                    Card(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(road.roadName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Text(road.location, style = MaterialTheme.typography.bodySmall)
                            Spacer(modifier = Modifier.height(8.dp))
                            HealthBadge(score = road.healthScore)
                        }
                    }
                }
            }

            item {
                Text("Capture Damage Photo", style = MaterialTheme.typography.titleSmall, modifier = Modifier.padding(bottom = 8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .border(1.dp, Color.Gray, MaterialTheme.shapes.medium)
                        .clickable {
                            if (cameraPermissionState.status.isGranted) {
                                val uri = CameraHelper.createImageUri(context)
                                imageUri = uri
                                cameraLauncher.launch(uri)
                            } else {
                                cameraPermissionState.launchPermissionRequest()
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (uiState.photoUri != null) {
                        AsyncImage(
                            model = uiState.photoUri,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        Button(
                            onClick = {
                                val uri = CameraHelper.createImageUri(context)
                                imageUri = uri
                                cameraLauncher.launch(uri)
                            },
                            modifier = Modifier.align(Alignment.BottomEnd).padding(8.dp)
                        ) {
                            Text("Retake")
                        }
                    } else {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.CameraAlt, contentDescription = null, modifier = Modifier.size(48.dp))
                            Text("Tap to Capture")
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text("GPS Location", style = MaterialTheme.typography.titleSmall)
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = if (uiState.latitude != 0.0) Color.Green else Color.Gray)
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            if (uiState.latitude != 0.0) {
                                Text("Lat: ${uiState.latitude}")
                                Text("Lng: ${uiState.longitude}")
                            } else {
                                Text("Location not captured", color = Color.Gray)
                            }
                        }
                        IconButton(onClick = {
                            if (locationPermissionState.status.isGranted) {
                                scope.launch {
                                    val loc = LocationHelper.getCurrentLocation(context)
                                    if (loc != null) viewModel.onLocationCaptured(loc.first, loc.second)
                                }
                            } else {
                                locationPermissionState.launchPermissionRequest()
                            }
                        }) {
                            Icon(Icons.Default.MyLocation, contentDescription = "Capture Location")
                        }
                    }
                }
            }

            item {
                OutlinedTextField(
                    value = uiState.description,
                    onValueChange = viewModel::onDescriptionChange,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    label = { Text("Describe the Damage") },
                    placeholder = { Text("e.g. Large pothole near the 3km marker...") },
                    minLines = 3
                )
                OutlinedTextField(
                    value = uiState.reporterName,
                    onValueChange = viewModel::onReporterNameChange,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    label = { Text("Your Name (Optional)") }
                )
            }

            item {
                Button(
                    onClick = viewModel::submitReport,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DamageRed),
                    enabled = !uiState.isSubmitting
                ) {
                    if (uiState.isSubmitting) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                    } else {
                        Text("Submit Damage Report")
                    }
                }
            }

            if (uiState.existingReports.isNotEmpty()) {
                item {
                    Text("Previous Reports", style = MaterialTheme.typography.titleSmall, modifier = Modifier.padding(top = 16.dp, bottom = 8.dp))
                }
                items(uiState.existingReports) { report ->
                    DamageReportCard(report = report)
                }
            }
        }
    }
}
