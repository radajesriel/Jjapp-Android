package com.jescoding.pixel.jjappandroid.core.data.repository

import com.jescoding.pixel.jjappandroid.core.data.local.real.dao.DashboardDao
import com.jescoding.pixel.jjappandroid.core.data.local.real.entity.toDataModel
import com.jescoding.pixel.jjappandroid.core.data.local.real.entity.toDomainModel
import com.jescoding.pixel.jjappandroid.core.domain.model.DashboardItem
import com.jescoding.pixel.jjappandroid.core.domain.repository.DashboardRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DashboardRepositoryImpl @Inject constructor(
    private val dao: DashboardDao
) : DashboardRepository {
    override fun getDashboardItems(): Flow<List<DashboardItem>> {
        return dao.getAllItems().map {
            it.map { entity -> entity.toDomainModel() }
        }
    }

    override fun getDashboardItemBySku(itemSku: String): DashboardItem? {
        return dao.getItemBySku(itemSku)?.toDomainModel()
    }

    override suspend fun saveDashboardItems(item: DashboardItem) {
        val dashboardEntity = item.toDataModel()
        dao.insertItem(dashboardEntity)
    }

    override suspend fun deleteDashboardItemBySku(itemSku: String) {
        dao.deleteItemBySku(itemSku)
    }

}