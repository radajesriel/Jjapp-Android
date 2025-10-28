package com.jescoding.pixel.jjappandroid.di

import com.jescoding.pixel.jjappandroid.features.inventory.dashboard.data.repository.FakeDashboardRepositoryImpl
import com.jescoding.pixel.jjappandroid.features.inventory.dashboard.domain.repository.DashboardRepository
import com.jescoding.pixel.jjappandroid.features.inventory.dashboard.domain.use_case.GetDashboardItems
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFakeDashboardRepository(): DashboardRepository {
        return FakeDashboardRepositoryImpl()
    }

    @Provides
    @Singleton
    fun provideGetDashboardItems(repository: DashboardRepository): GetDashboardItems {
        return GetDashboardItems(repository)
    }

}
