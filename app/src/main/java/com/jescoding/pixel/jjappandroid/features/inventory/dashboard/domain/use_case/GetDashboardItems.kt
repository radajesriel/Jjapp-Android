package com.jescoding.pixel.jjappandroid.features.inventory.dashboard.domain.use_case

import com.jescoding.pixel.jjappandroid.features.inventory.dashboard.domain.model.DashboardItem
import com.jescoding.pixel.jjappandroid.features.inventory.dashboard.domain.repository.DashboardRepository
import kotlinx.coroutines.flow.Flow

class GetDashboardItems(private val repository: DashboardRepository) {
    operator fun invoke(): Flow<List<DashboardItem>> {
        return repository.getDashboardItems()
    }
}