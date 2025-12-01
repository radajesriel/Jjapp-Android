package com.jescoding.pixel.jjappandroid.features.inventory.screens.item.presentation

import com.jescoding.pixel.jjappandroid.core.domain.model.DashboardItem

data class InventoryUiState(
    val item: DashboardItem? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)