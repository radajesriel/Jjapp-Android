package com.jescoding.pixel.jjappandroid.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.jescoding.pixel.jjappandroid.features.inventory.screens.add_edit_product.presentation.AddProductScreen
import com.jescoding.pixel.jjappandroid.features.inventory.screens.dashboard.presentation.DashboardScreen
import com.jescoding.pixel.jjappandroid.features.inventory.screens.item.presentation.ItemScreen

@Composable
fun RootNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Dashboard.route,
    ) {
        composable(route = Screen.Dashboard.route) {
            DashboardScreen(
                onNavigateToItem = { itemSku ->
                    navController.navigate("${Screen.Item.route}/$itemSku")
                },
                onNavigateToProduct = {
                    navController.navigate(Screen.AddProduct.route)
                }
            )
        }

        composable(
            route = "${Screen.Item.route}/{itemSku}",
            arguments = listOf(
                navArgument("itemSku") {
                    type = NavType.StringType
                }
            )
        ) {
            val itemSku = it.arguments?.getString("itemSku")
            ItemScreen(
                onNavigateUp = {
                    navController.navigateUp()
                },
                onEditClicked = {
                    navController.navigate("${Screen.AddProduct.route}?itemSku=$itemSku")
                }
            )
        }

        composable(
            route = "${Screen.AddProduct.route}?itemSku={itemSku}",
            arguments = listOf(
                navArgument("itemSku") {
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) {
            AddProductScreen(
                onNavigateUp = {
                    navController.navigateUp()
                },
                navigateOnProductSaved = {
                    navController.navigate(Screen.Dashboard.route)
                }
            )
        }
    }
}