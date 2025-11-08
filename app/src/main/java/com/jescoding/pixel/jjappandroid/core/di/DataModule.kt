package com.jescoding.pixel.jjappandroid.core.di

import com.jescoding.pixel.jjappandroid.core.data.local.repository.FakeDashboardRepositoryImpl
import com.jescoding.pixel.jjappandroid.core.domain.repository.DashboardRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindDashboardRepository(
        impl: FakeDashboardRepositoryImpl
    ): DashboardRepository

}