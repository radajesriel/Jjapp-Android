package com.jescoding.pixel.jjappandroid.core.data.local.real.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jescoding.pixel.jjappandroid.core.data.local.real.dao.DashboardDao
import com.jescoding.pixel.jjappandroid.core.data.local.real.entity.DashboardItemEntity

@Database(
    entities = [DashboardItemEntity::class],
    version = 2,
    exportSchema = false // Recommended to set to true for production apps
)
abstract class AppDatabase : RoomDatabase() {
    abstract val dashboardDao: DashboardDao
}