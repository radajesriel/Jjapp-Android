package com.jescoding.pixel.jjappandroid.core.data.repository

import com.jescoding.pixel.jjappandroid.core.data.local.real.dao.DashboardDao
import com.jescoding.pixel.jjappandroid.core.data.local.real.entity.toDataModel
import com.jescoding.pixel.jjappandroid.core.data.local.real.entity.toDomainModel
import com.jescoding.pixel.jjappandroid.core.data.sync.SyncStatus
import com.jescoding.pixel.jjappandroid.core.data.sync.SyncWorkScheduler
import com.jescoding.pixel.jjappandroid.core.domain.model.DashboardItem
import com.jescoding.pixel.jjappandroid.core.domain.repository.DashboardRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DashboardRepositoryImpl @Inject constructor(
    private val dao: DashboardDao,
    private val syncWorkScheduler: SyncWorkScheduler
) : DashboardRepository {

    override fun getDashboardItems(): Flow<List<DashboardItem>> {
        return dao.getAllItems().map {
            it.map { entity -> entity.toDomainModel() }
        }
    }

    override fun getDashboardItemBySku(itemSku: String): DashboardItem? {
        return dao.getItemBySku(itemSku)?.toDomainModel()
    }

    override suspend fun saveDashboardItem(item: DashboardItem) {
        val existingItem = dao.getItemBySku(item.itemSku)
        val syncStatus = if (existingItem != null) SyncStatus.PENDING_UPDATE else SyncStatus.PENDING_CREATE
        val dashboardEntity = item.toDataModel(
            updatedAt = System.currentTimeMillis(),
            syncStatus = syncStatus
        )
        dao.insertItem(dashboardEntity)
        syncWorkScheduler.requestImmediateSync()
    }

    override suspend fun deleteDashboardItemBySku(itemSku: String) {
        dao.markForDeletion(itemSku, SyncStatus.PENDING_DELETE, System.currentTimeMillis())
        syncWorkScheduler.requestImmediateSync()
    }
}