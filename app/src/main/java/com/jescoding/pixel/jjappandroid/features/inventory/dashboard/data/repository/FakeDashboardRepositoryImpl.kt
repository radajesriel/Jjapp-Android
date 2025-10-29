package com.jescoding.pixel.jjappandroid.features.inventory.dashboard.data.repository

import com.jescoding.pixel.jjappandroid.features.inventory.dashboard.domain.model.DashboardItem
import com.jescoding.pixel.jjappandroid.features.inventory.dashboard.domain.repository.DashboardRepository
import com.jescoding.pixel.jjappandroid.features.inventory.dashboard.presentation.data.FakeDashboardData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeDashboardRepositoryImpl : DashboardRepository {
    override fun getDashboardItems(): Flow<List<DashboardItem>> {
        return flowOf(FakeDashboardData.items)
    }
}