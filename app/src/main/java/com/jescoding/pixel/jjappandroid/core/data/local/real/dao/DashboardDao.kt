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

    @Query("SELECT * FROM dashboard_items")
    fun getAllItems(): Flow<List<DashboardItemEntity>>

    @Query("SELECT * FROM dashboard_items WHERE itemSku = :itemSku")
    fun getItemBySku(itemSku: String): DashboardItemEntity?

    @Query("DELETE FROM dashboard_items")
    suspend fun clearAll()

    @Query("DELETE FROM dashboard_items WHERE itemSku = :itemSku")
    suspend fun deleteItemBySku(itemSku: String)
}