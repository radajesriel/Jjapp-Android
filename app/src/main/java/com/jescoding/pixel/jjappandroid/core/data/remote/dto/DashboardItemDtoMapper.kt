package com.jescoding.pixel.jjappandroid.core.data.remote.dto

import com.jescoding.pixel.jjappandroid.core.data.local.real.entity.DashboardItemEntity
import com.jescoding.pixel.jjappandroid.core.data.sync.SyncStatus
import kotlinx.datetime.Instant

fun DashboardItemEntity.toDto(userId: String?): DashboardItemDto {
    return DashboardItemDto(
        itemSku = this.itemSku,
        itemName = this.itemName,
        itemVariant = this.itemVariant,
        availableStock = this.availableStock,
        onHandStock = this.onHandStock,
        onTheWayStock = this.onTheWayStock,
        itemCostPrice = this.itemCostPrice,
        itemSellingPrice = this.itemSellingPrice,
        itemImageResId = this.itemImageResId,
        itemUri = this.itemUri,
        updatedAt = Instant.fromEpochMilliseconds(this.updatedAt).toString(),
        isDeleted = this.syncStatus == SyncStatus.PENDING_DELETE,
        userId = userId
    )
}

fun DashboardItemDto.toEntity(): DashboardItemEntity {
    val epochMillis = try {
        Instant.parse(this.updatedAt).toEpochMilliseconds()
    } catch (e: Exception) {
        System.currentTimeMillis()
    }

    return DashboardItemEntity(
        itemSku = this.itemSku,
        itemName = this.itemName,
        itemVariant = this.itemVariant,
        availableStock = this.availableStock,
        onHandStock = this.onHandStock,
        onTheWayStock = this.onTheWayStock,
        itemCostPrice = this.itemCostPrice,
        itemSellingPrice = this.itemSellingPrice,
        itemImageResId = this.itemImageResId,
        itemUri = this.itemUri,
        updatedAt = epochMillis,
        syncStatus = SyncStatus.SYNCED
    )
}
