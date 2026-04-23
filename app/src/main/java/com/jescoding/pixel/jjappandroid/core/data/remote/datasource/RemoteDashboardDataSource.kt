package com.jescoding.pixel.jjappandroid.core.data.remote.datasource

import com.jescoding.pixel.jjappandroid.core.data.remote.dto.DashboardItemDto

interface RemoteDashboardDataSource {
    suspend fun upsertItem(dto: DashboardItemDto): Result<Unit>
    suspend fun softDeleteItem(itemSku: String, userId: String): Result<Unit>
    suspend fun getItemsModifiedAfter(timestamp: String, userId: String): Result<List<DashboardItemDto>>
}
