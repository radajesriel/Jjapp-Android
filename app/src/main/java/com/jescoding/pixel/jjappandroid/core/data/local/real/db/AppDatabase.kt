package com.jescoding.pixel.jjappandroid.core.data.local.real.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jescoding.pixel.jjappandroid.core.data.local.real.dao.DashboardDao
import com.jescoding.pixel.jjappandroid.core.data.local.real.entity.DashboardItemEntity

@Database(
    entities = [DashboardItemEntity::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract val dashboardDao: DashboardDao
}