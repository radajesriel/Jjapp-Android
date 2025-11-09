package com.jescoding.pixel.jjappandroid.core.data.di

import com.jescoding.pixel.jjappandroid.core.domain.repository.DashboardRepository
import com.jescoding.pixel.jjappandroid.features.inventory.domain.use_cases.GetDashboardItemBySku
import com.jescoding.pixel.jjappandroid.features.inventory.domain.use_cases.GetDashboardItems
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {
    @Provides
    @Singleton
    fun provideGetDashboardItems(repository: DashboardRepository): GetDashboardItems {
        return GetDashboardItems(repository)
    }

    @Provides
    @Singleton
    fun provideGetDashboardItemBySku(repository: DashboardRepository): GetDashboardItemBySku {
        return GetDashboardItemBySku(
            repository
        )
    }
}