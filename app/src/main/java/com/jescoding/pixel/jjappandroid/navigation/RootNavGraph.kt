package com.jescoding.pixel.jjappandroid.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.jescoding.pixel.jjappandroid.features.inventory.screens.dashboard.presentation.DashboardScreen
import com.jescoding.pixel.jjappandroid.features.inventory.screens.item.presentation.ItemScreen

sealed class Screen(val route: String) {
    object Dashboard : Screen("dashboard_screen")
    object Item : Screen("item_screen")
}

@Composable
fun RootNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Dashboard.route,
        // Optional: Add custom transitions here for a more polished feel
    ) {
        composable(route = Screen.Dashboard.route) {
            DashboardScreen(
                // Navigate to the item detail screen. In a real app, you would pass an item ID.
                // e.g., onNavigateToItem = { itemId -> navController.navigate("${Screen.Item.route}/$itemId") }
                onNavigateToItem = { itemSku ->
                    navController.navigate("${Screen.Item.route}/$itemSku")
                }
            )
        }

        composable(
            route = "${Screen.Item.route}/{itemSku}",
            arguments = listOf(navArgument("itemSku") { type = NavType.StringType })
        ) {
            ItemScreen(
                onNavigateUp = {
                    // Provides a standard "back" navigation action
                    navController.navigateUp()
                }
            )
        }
    }
}