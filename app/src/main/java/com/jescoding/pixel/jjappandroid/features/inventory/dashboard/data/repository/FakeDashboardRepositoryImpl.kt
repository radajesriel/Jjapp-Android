package com.jescoding.pixel.jjappandroid.features.inventory.dashboard.data.repository

import com.jescoding.pixel.jjappandroid.R
import com.jescoding.pixel.jjappandroid.features.inventory.dashboard.domain.model.DashboardItem
import com.jescoding.pixel.jjappandroid.features.inventory.dashboard.domain.repository.DashboardRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeDashboardRepositoryImpl : DashboardRepository {
    override fun getDashboardItems(): Flow<List<DashboardItem>> {
        val hardcodedItems = listOf(
            DashboardItem(
                itemName = "Original Motowolf Cellphone Holder V5",
                itemVariant = "Clamp Type",
                availableStock = 110,
                itemImageResId = R.drawable.sample
            ),
            DashboardItem(
                itemName = "Sec 2-Way Motorcycle Alarm System",
                itemVariant = "With Auto Start",
                availableStock = 45,
                itemImageResId = R.drawable.sample
            ),
            DashboardItem(
                itemName = "HJC C70 Full-Face Helmet - Solid White",
                itemVariant = "Large",
                availableStock = 8,
                itemImageResId = R.drawable.sample
            ),
            DashboardItem(
                itemName = "RCB S2 Series Monoshock Absorber",
                itemVariant = "305mm, Yellow",
                availableStock = 22,
                itemImageResId = R.drawable.sample
            )
        )
        return flowOf(hardcodedItems)
    }
}