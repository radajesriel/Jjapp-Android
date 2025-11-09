package com.jescoding.pixel.jjappandroid.core.data.local.real.db

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.jescoding.pixel.jjappandroid.core.data.local.real.dao.DashboardDao
import com.jescoding.pixel.jjappandroid.core.data.local.real.data.SeedData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Provider

class AppDatabaseCallback(
    private val dashboardDaoProvider: Provider<DashboardDao>
) : RoomDatabase.Callback() {
    private val applicationScope = CoroutineScope(SupervisorJob())

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        applicationScope.launch {
            populateDatabase()
        }
    }

    private suspend fun populateDatabase() {
        // Get the DAO instance and insert the seed data
        dashboardDaoProvider.get().insertItems(SeedData.items)
    }
}