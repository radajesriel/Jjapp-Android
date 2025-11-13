package com.jescoding.pixel.jjappandroid.navigation

sealed class Screen(val route: String) {
    object Dashboard : Screen("dashboard_screen")
    object Item : Screen("item_screen")
    object AddProduct : Screen("add_product_screen")
}
