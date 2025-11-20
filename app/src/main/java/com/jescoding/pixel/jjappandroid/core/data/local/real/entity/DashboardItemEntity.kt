package com.jescoding.pixel.jjappandroid.core.data.local.real.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "dashboard_items",
    //Best practice to index faster queries
    indices = [Index(value = ["itemSku"], unique = true)]
)
data class DashboardItemEntity(
    @PrimaryKey val itemSku: String,
    val itemName: String,
    val itemVariant: String,
    val availableStock: Int,
    val onHandStock: Int,
    val onTheWayStock: Int,
    val itemCostPrice: Double,
    val itemSellingPrice: Double,
    val itemImageResId: Int,
    val itemUri: String?
)