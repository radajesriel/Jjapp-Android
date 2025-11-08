package com.jescoding.pixel.jjappandroid.core.data.local.repository

import com.jescoding.pixel.jjappandroid.core.domain.model.DashboardItem
import com.jescoding.pixel.jjappandroid.core.domain.repository.DashboardRepository
import com.jescoding.pixel.jjappandroid.features.inventory.screens.dashboard.presentation.data.FakeDashboardData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeDashboardRepositoryImpl @Inject constructor() : DashboardRepository {
    private val items = FakeDashboardData.items.toMutableList()

    override fun getDashboardItems(): Flow<List<DashboardItem>> {
        return flow {
            emit(items)
        }
    }

    override fun getDashboardItemBySku(itemSku: String): Flow<DashboardItem?> {
        return flow {
            emit(items.find { it.itemSku == itemSku })
        }
    }
}