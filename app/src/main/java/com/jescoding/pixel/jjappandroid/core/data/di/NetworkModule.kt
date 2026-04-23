package com.jescoding.pixel.jjappandroid.core.data.di

import com.jescoding.pixel.jjappandroid.core.data.providers.NetworkMonitorImpl
import com.jescoding.pixel.jjappandroid.core.data.remote.SupabaseClientProvider
import com.jescoding.pixel.jjappandroid.core.data.remote.auth.AuthManagerImpl
import com.jescoding.pixel.jjappandroid.core.data.remote.datasource.RemoteDashboardDataSource
import com.jescoding.pixel.jjappandroid.core.data.remote.datasource.RemoteDashboardDataSourceImpl
import com.jescoding.pixel.jjappandroid.core.domain.auth.AuthManager
import com.jescoding.pixel.jjappandroid.core.domain.providers.NetworkMonitor
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideSupabaseClient(): SupabaseClient {
        return SupabaseClientProvider.create()
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkBindingsModule {

    @Binds
    @Singleton
    abstract fun bindAuthManager(impl: AuthManagerImpl): AuthManager

    @Binds
    @Singleton
    abstract fun bindNetworkMonitor(impl: NetworkMonitorImpl): NetworkMonitor

    @Binds
    @Singleton
    abstract fun bindRemoteDataSource(impl: RemoteDashboardDataSourceImpl): RemoteDashboardDataSource
}
