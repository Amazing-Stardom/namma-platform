package com.nammaraste.health.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.nammaraste.health.ui.screen.contractorinfo.ContractorInfoScreen
import com.nammaraste.health.ui.screen.damagereport.DamageReportScreen
import com.nammaraste.health.ui.screen.roaddirectory.RoadDirectoryScreen
import com.nammaraste.health.ui.screen.successmap.SuccessMapScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.RoadDirectory.route
    ) {
        composable(BottomNavItem.RoadDirectory.route) {
            RoadDirectoryScreen(
                onReportDamage = { roadId ->
                    navController.navigate("damage_report/$roadId")
                },
                onViewContractor = { roadId ->
                    navController.navigate("contractor_info/$roadId")
                }
            )
        }
        
        composable(
            route = "damage_report/{roadId}",
            arguments = listOf(navArgument("roadId") { type = NavType.IntType; defaultValue = 1 })
        ) { backStackEntry ->
            val roadId = backStackEntry.arguments?.getInt("roadId") ?: 1
            DamageReportScreen(
                roadId = roadId,
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = "damage_report",
        ) {
            DamageReportScreen(
                roadId = 1, // Default to first road if accessed from bottom nav
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = "contractor_info/{roadId}",
            arguments = listOf(navArgument("roadId") { type = NavType.IntType; defaultValue = 1 })
        ) { backStackEntry ->
            val roadId = backStackEntry.arguments?.getInt("roadId") ?: 1
            ContractorInfoScreen(
                roadId = roadId,
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = "contractor_info",
        ) {
            ContractorInfoScreen(
                roadId = 1, // Default to first road if accessed from bottom nav
                onBack = { navController.popBackStack() }
            )
        }

        composable(BottomNavItem.SuccessMap.route) {
            SuccessMapScreen()
        }
    }
}
