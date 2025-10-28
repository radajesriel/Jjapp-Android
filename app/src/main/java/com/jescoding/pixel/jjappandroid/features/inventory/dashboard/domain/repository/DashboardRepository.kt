package com.jescoding.pixel.jjappandroid.features.inventory.dashboard.domain.repository

import com.jescoding.pixel.jjappandroid.features.inventory.dashboard.domain.model.DashboardItem
import kotlinx.coroutines.flow.Flow

interface DashboardRepository {
    fun getDashboardItems(): Flow<List<DashboardItem>>
}