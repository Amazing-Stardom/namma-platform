package com.nammaraste.health.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val route: String, val label: String, val icon: ImageVector) {
    object RoadDirectory : BottomNavItem("road_directory", "Roads", Icons.Default.Map)
    object DamageReport  : BottomNavItem("damage_report",  "Report", Icons.Default.ReportProblem)
    object ContractorInfo: BottomNavItem("contractor_info","Contractors", Icons.Default.Business)
    object SuccessMap    : BottomNavItem("success_map",    "Map", Icons.Default.Explore)
}
