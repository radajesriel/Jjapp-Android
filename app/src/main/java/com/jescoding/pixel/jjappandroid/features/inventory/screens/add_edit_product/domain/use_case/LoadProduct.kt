package com.jescoding.pixel.jjappandroid.features.inventory.screens.add_edit_product.domain.use_case

import com.jescoding.pixel.jjappandroid.core.domain.model.DashboardItem
import com.jescoding.pixel.jjappandroid.core.domain.providers.DispatcherProvider
import com.jescoding.pixel.jjappandroid.core.domain.repository.DashboardRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoadProduct @Inject constructor(
    private val repository: DashboardRepository,
    private val dispatcherProvider: DispatcherProvider
) {
    suspend operator fun invoke(itemSku: String): DashboardItem? {
        return withContext(dispatcherProvider.io) {
            repository.getDashboardItemBySku(itemSku)
        }
    }

}