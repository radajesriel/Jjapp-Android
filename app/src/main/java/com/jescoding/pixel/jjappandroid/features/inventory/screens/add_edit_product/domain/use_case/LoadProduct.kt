package com.jescoding.pixel.jjappandroid.features.inventory.screens.add_edit_product.domain.use_case

import com.jescoding.pixel.jjappandroid.core.domain.model.DashboardItem
import com.jescoding.pixel.jjappandroid.core.domain.repository.DashboardRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoadProduct @Inject constructor(
    private val repository: DashboardRepository
) {
    operator fun invoke(itemSku: String) : Flow<DashboardItem?> {
        return repository.getDashboardItemBySku(itemSku)
    }

}