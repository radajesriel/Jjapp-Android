package com.jescoding.pixel.jjappandroid.core.data.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.jescoding.pixel.jjappandroid.core.data.local.real.dao.DashboardDao
import com.jescoding.pixel.jjappandroid.core.data.local.real.db.AppDatabase
import com.jescoding.pixel.jjappandroid.core.data.local.real.db.AppDatabaseCallback
import com.jescoding.pixel.jjappandroid.core.data.repository.DashboardRepositoryImpl
import com.jescoding.pixel.jjappandroid.core.domain.providers.ResourceProvider
import com.jescoding.pixel.jjappandroid.core.domain.repository.DashboardRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Provider
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabaseCallback(
        daoProvider: Provider<DashboardDao>,
        resourceProvider: ResourceProvider,
    ): AppDatabaseCallback {
        return AppDatabaseCallback(daoProvider, resourceProvider)
    }

    val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE dashboard_items ADD COLUMN updatedAt INTEGER NOT NULL DEFAULT 0")
            db.execSQL("ALTER TABLE dashboard_items ADD COLUMN syncStatus INTEGER NOT NULL DEFAULT 0")
        }
    }

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context,
        callback: AppDatabaseCallback
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "jjapp_database"
        )
            .addCallback(callback)
            .addMigrations(MIGRATION_2_3)
            .build()
    }

    @Provides
    @Singleton
    fun provideDashboardDao(database: AppDatabase): DashboardDao {
        return database.dashboardDao
    }
}


@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    @Binds
    @Singleton
    abstract fun bindDashboardRepository(
        // Use FakeDashboardRepository for testing with fake data
        impl: DashboardRepositoryImpl
    ): DashboardRepository

}