package com.jescoding.pixel.jjappandroid.core.data.local.real.entity

import androidx.core.net.toUri
import com.jescoding.pixel.jjappandroid.core.domain.model.DashboardItem

fun DashboardItemEntity.toDomainModel(): DashboardItem {
    return DashboardItem(
        itemSku = this.itemSku,
        itemName = this.itemName,
        itemVariant = this.itemVariant,
        availableStock = this.availableStock,
        onHandStock = this.onHandStock,
        onTheWayStock = this.onTheWayStock,
        itemCostPrice = this.itemCostPrice.toBigDecimal(),
        itemSellingPrice = this.itemSellingPrice.toBigDecimal(),
        itemImageResId = this.itemImageResId,
        itemUri = this.itemUri?.toUri()
    )
}

fun DashboardItem.toDataModel(): DashboardItemEntity {
    return DashboardItemEntity(
        itemSku = this.itemSku,
        itemName = this.itemName,
        itemVariant = this.itemVariant,
        availableStock = this.availableStock,
        onHandStock = this.onHandStock,
        onTheWayStock = this.onTheWayStock,
        itemCostPrice = this.itemCostPrice.toDouble(),
        itemSellingPrice = this.itemSellingPrice.toDouble(),
        itemImageResId = this.itemImageResId,
        itemUri = this.itemUri?.toString()
    )
}