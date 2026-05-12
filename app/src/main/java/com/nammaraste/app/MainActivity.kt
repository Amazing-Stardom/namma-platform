package com.nammaraste.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.nammaraste.app.ui.Screen
import com.nammaraste.app.ui.screens.*
import com.nammaraste.app.viewmodel.RoadViewModel

class MainActivity : ComponentActivity() {
    private val roadViewModel: RoadViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                MainScreen(roadViewModel)
            }
        }
    }
}

@Composable
fun MainScreen(viewModel: RoadViewModel) {
    val navController = rememberNavController()
    var selectedRoadId by remember { mutableStateOf<Int?>(null) }

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            NavHost(navController, startDestination = Screen.Directory.route) {
                composable(Screen.Directory.route) {
                    RoadDirectoryScreen(viewModel = viewModel, onRoadClick = {
                        selectedRoadId = it
                        navController.navigate(Screen.ContractorInfo.route)
                    })
                }
                composable(Screen.Report.route) {
                    DamageReportScreen(viewModel = viewModel)
                }
                composable(Screen.ContractorInfo.route) {
                    ContractorInfoScreen(roadId = selectedRoadId, viewModel = viewModel)
                }
                composable(Screen.Map.route) {
                    SuccessMapScreen(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        NavigationItem("Directory", Screen.Directory.route, Icons.Default.List),
        NavigationItem("Report", Screen.Report.route, Icons.Default.Warning),
        NavigationItem("Info", Screen.ContractorInfo.route, Icons.Default.Info),
        NavigationItem("Map", Screen.Map.route, Icons.Default.LocationOn)
    )

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}

data class NavigationItem(val title: String, val route: String, val icon: androidx.compose.ui.graphics.vector.ImageVector)
