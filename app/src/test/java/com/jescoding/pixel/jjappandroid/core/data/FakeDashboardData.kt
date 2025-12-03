package com.jescoding.pixel.jjappandroid.core.data

import com.jescoding.pixel.jjappandroid.core.domain.model.DashboardItem
import com.jescoding.pixel.jjappandroid.features.inventory.screens.add_edit_product.domain.model.NewProductInput

object FakeDashboardData {
    val items = listOf(
        DashboardItem(
            itemSku = "SKU001",
            itemName = "Dashboard Item 1",
            itemVariant = "dashboard_item_1_variant",
            availableStock = 10,
            onHandStock = 20,
            onTheWayStock = 30,
            itemCostPrice = 120.toBigDecimal(),
            itemSellingPrice = 150.toBigDecimal(),
            itemImageResId = 0,
            itemUri = null
        ),
        DashboardItem(
            itemSku = "SKU002",
            itemName = "Dashboard Item 2",
            itemVariant = "dashboard_item_2_variant",
            availableStock = 3,
            onHandStock = 15,
            onTheWayStock = 12,
            itemCostPrice = 170.toBigDecimal(),
            itemSellingPrice = 200.toBigDecimal(),
            itemImageResId = 0,
            itemUri = null
        )
    )

    val singleItem = DashboardItem(
        itemSku = "SKU001",
        itemName = "Dashboard Item 1",
        itemVariant = "dashboard_item_1_variant",
        availableStock = 10,
        onHandStock = 20,
        onTheWayStock = 30,
        itemCostPrice = 120.toBigDecimal(),
        itemSellingPrice = 150.toBigDecimal(),
        itemImageResId = 0,
        itemUri = null
    )

    val productInput = NewProductInput(
        itemSku = "SKU001",
        itemName = "Dashboard Item 1",
        itemVariant = "dashboard_item_1_variant",
        availableStock = "10",
        onHandStock = "20",
        onTheWayStock = "30",
        itemCostPrice = "120.00",
        itemSellingPrice = "150.00",
        itemImageResId = 0,
        itemUri = null
    )
}