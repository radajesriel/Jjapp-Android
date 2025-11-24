package com.jescoding.pixel.jjappandroid.core.data.local.real.db

import android.content.Context
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.jescoding.pixel.jjappandroid.core.data.local.real.dao.DashboardDao
import com.jescoding.pixel.jjappandroid.core.data.local.real.data.SeedData
import com.jescoding.pixel.jjappandroid.core.domain.providers.ResourceProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

class AppDatabaseCallback @Inject constructor(
    private val dashboardDao: Provider<DashboardDao>,
    private val resourceProvider: ResourceProvider,
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
        val items = SeedData.items.map { item ->
            // If the item has a drawable resource ID, convert it to a URI
            if (item.itemImageResId != 0) {
                val uri = resourceProvider.getDrawableUri(item.itemImageResId)
                item.copy(itemUri = uri.toString())
            } else {
                item
            }
        }
        dashboardDao.get().insertItems(items)
    }
}