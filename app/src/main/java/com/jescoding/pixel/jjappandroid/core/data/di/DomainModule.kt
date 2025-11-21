package com.jescoding.pixel.jjappandroid.core.data.di

import android.content.Context
import com.jescoding.pixel.jjappandroid.core.data.providers.DispatcherProviderImpl
import com.jescoding.pixel.jjappandroid.core.data.providers.ResourceProviderImpl
import com.jescoding.pixel.jjappandroid.core.domain.providers.DispatcherProvider
import com.jescoding.pixel.jjappandroid.core.domain.providers.ResourceProvider
import com.jescoding.pixel.jjappandroid.core.domain.repository.DashboardRepository
import com.jescoding.pixel.jjappandroid.core.domain.use_cases.GetDashboardItemBySku
import com.jescoding.pixel.jjappandroid.core.domain.use_cases.GetDashboardItems
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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

    @Provides
    @Singleton
    fun provideDispatcherProvider(): DispatcherProvider {
        return DispatcherProviderImpl()
    }

    @Provides
    @Singleton
    fun provideResourceProvider(
        @ApplicationContext context: Context
    ): ResourceProvider {
        return ResourceProviderImpl(context)
    }
}