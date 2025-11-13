package com.jescoding.pixel.jjappandroid.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.jescoding.pixel.jjappandroid.features.inventory.screens.add_product.presentation.AddProductScreen
import com.jescoding.pixel.jjappandroid.features.inventory.screens.dashboard.presentation.DashboardScreen
import com.jescoding.pixel.jjappandroid.features.inventory.screens.item.presentation.ItemScreen

@Composable
fun RootNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.AddProduct.route,
        // Optional: Add custom transitions here for a more polished feel
    ) {
        composable(route = Screen.Dashboard.route) {
            DashboardScreen(
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
                    navController.navigateUp()
                }
            )
        }

        composable(route = Screen.AddProduct.route) {
            AddProductScreen(
                onNavigateUp = {
                    navController.navigateUp()
                }
            )
        }
    }
}