package com.jescoding.pixel.jjappandroid.core.data.local.real.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jescoding.pixel.jjappandroid.core.data.local.real.entity.DashboardItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DashboardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(items: List<DashboardItemEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: DashboardItemEntity)

    @Query("SELECT * FROM dashboard_items WHERE syncStatus != 3")
    fun getAllItems(): Flow<List<DashboardItemEntity>>

    @Query("SELECT * FROM dashboard_items WHERE itemSku = :itemSku")
    fun getItemBySku(itemSku: String): DashboardItemEntity?

    @Query("DELETE FROM dashboard_items")
    suspend fun clearAll()

    @Query("DELETE FROM dashboard_items WHERE itemSku = :itemSku")
    suspend fun deleteItemBySku(itemSku: String)

    @Query("SELECT * FROM dashboard_items WHERE syncStatus != 0")
    suspend fun getItemsPendingSync(): List<DashboardItemEntity>

    @Query("UPDATE dashboard_items SET syncStatus = :syncStatus WHERE itemSku = :itemSku")
    suspend fun updateSyncStatus(itemSku: String, syncStatus: Int)

    @Query("UPDATE dashboard_items SET syncStatus = :syncStatus, updatedAt = :updatedAt WHERE itemSku = :itemSku")
    suspend fun markForDeletion(itemSku: String, syncStatus: Int, updatedAt: Long)
}