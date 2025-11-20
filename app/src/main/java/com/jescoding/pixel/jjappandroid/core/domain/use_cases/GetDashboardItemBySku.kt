package com.jescoding.pixel.jjappandroid.core.domain.use_cases

import com.jescoding.pixel.jjappandroid.core.domain.model.DashboardItem
import com.jescoding.pixel.jjappandroid.core.domain.repository.DashboardRepository
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class GetDashboardItemBySku @Inject constructor(
    private val repository: DashboardRepository
) {
    operator fun invoke(itemSku: String): Flow<DashboardItem?> {
        return repository.getDashboardItemBySku(itemSku)
    }
}