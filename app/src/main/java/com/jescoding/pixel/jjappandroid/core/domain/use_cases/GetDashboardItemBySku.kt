package com.jescoding.pixel.jjappandroid.core.domain.use_cases

import com.jescoding.pixel.jjappandroid.core.domain.model.DashboardItem
import com.jescoding.pixel.jjappandroid.core.domain.providers.DispatcherProvider
import com.jescoding.pixel.jjappandroid.core.domain.repository.DashboardRepository
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ViewModelScoped
class GetDashboardItemBySku @Inject constructor(
    private val repository: DashboardRepository,
    private val dispatcherProvider: DispatcherProvider
) {
    suspend operator fun invoke(itemSku: String): DashboardItem? {
        return withContext(dispatcherProvider.io) {
            repository.getDashboardItemBySku(itemSku)
        }
    }
}