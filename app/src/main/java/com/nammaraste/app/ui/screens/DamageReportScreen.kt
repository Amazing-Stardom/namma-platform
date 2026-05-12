package com.nammaraste.app.ui.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Looper
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.nammaraste.app.data.Road
import com.nammaraste.app.viewmodel.RoadViewModel
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DamageReportScreen(viewModel: RoadViewModel) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    
    var description by remember { mutableStateOf("") }
    var location by remember { mutableStateOf<Location?>(null) }
    var photoUri by remember { mutableStateOf<Uri?>(null) }
    
    val allRoads by viewModel.allRoads.collectAsState()
    var selectedRoad by remember { mutableStateOf<Road?>(null) }
    var expanded by remember { mutableStateOf(false) }

    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    fun requestFreshLocation() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000).build()
            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(p0: LocationResult) {
                    p0.lastLocation?.let {
                        location = it
                    }
                    fusedLocationClient.removeLocationUpdates(this)
                }
            }
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        }
    }
    
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                coroutineScope.launch {
                    try {
                        val loc = fusedLocationClient.lastLocation.await()
                        if (loc != null) {
                            location = loc
                        } else {
                            requestFreshLocation()
                        }
                    } catch (e: SecurityException) {
                        // Handle missing permission
                    }
                }
            } else {
                Toast.makeText(context, "Location permission needed", Toast.LENGTH_SHORT).show()
            }
        }
    )

    // Auto-capture GPS if permission is granted
    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            try {
                val loc = fusedLocationClient.lastLocation.await()
                if (loc != null) {
                    location = loc
                } else {
                    requestFreshLocation()
                }
            } catch (e: SecurityException) {
                 // Ignored
            }
        } else {
             locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview(),
        onResult = { bitmap ->
            // In a real app we'd save the bitmap to file and get URI. MOCK URI FOR NOW.
            if(bitmap != null) {
                photoUri = Uri.parse("mock://photo_${System.currentTimeMillis()}")
            }
        }
    )

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Report Damage", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        // Road Selection Dropdown
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedRoad?.roadName ?: "Select Road",
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                allRoads.forEach { road ->
                    DropdownMenuItem(
                        text = { Text(road.roadName) },
                        onClick = {
                            selectedRoad = road
                            expanded = false
                        }
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Damage Description") },
            modifier = Modifier.fillMaxWidth().height(120.dp)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Button(onClick = { takePictureLauncher.launch(null) }) {
                Text("Take Photo")
            }
            
            Button(onClick = {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    coroutineScope.launch {
                        val loc = fusedLocationClient.lastLocation.await()
                        if (loc != null) {
                            location = loc
                        } else {
                            requestFreshLocation()
                        }
                    }
                } else {
                    locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }
            }) {
                Text("Get GPS")
            }
        }
        
        if (location != null) {
            Text("Lat: ${location?.latitude}, Lng: ${location?.longitude}")
        } else {
            Text("Fetching GPS...")
        }
        
        if (photoUri != null) {
            Text("Photo captured.")
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Button(
            onClick = {
                if (selectedRoad != null) {
                    val lat = location?.latitude ?: 0.0
                    val lng = location?.longitude ?: 0.0
                    viewModel.reportDamage(
                        roadId = selectedRoad!!.id,
                        photoUri = photoUri?.toString(),
                        lat = lat,
                        lng = lng,
                        desc = description
                    )
                    Toast.makeText(context, "Report Submitted", Toast.LENGTH_SHORT).show()
                    description = ""
                    photoUri = null
                } else {
                    Toast.makeText(context, "Please select a road.", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Submit Report")
        }
    }
}
