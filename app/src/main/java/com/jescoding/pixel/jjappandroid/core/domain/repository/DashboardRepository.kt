package com.jescoding.pixel.jjappandroid.core.domain.repository

import com.jescoding.pixel.jjappandroid.core.domain.model.DashboardItem
import kotlinx.coroutines.flow.Flow

interface DashboardRepository {
    fun getDashboardItems(): Flow<List<DashboardItem>>
    fun getDashboardItemBySku(itemSku: String): DashboardItem?
    suspend fun saveDashboardItems(item: DashboardItem)
    suspend fun deleteDashboardItemBySku(itemSku: String)
}