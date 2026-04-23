package com.jescoding.pixel.jjappandroid.core.data.di

import com.jescoding.pixel.jjappandroid.core.data.remote.storage.ImageSyncManagerImpl
import com.jescoding.pixel.jjappandroid.core.data.sync.SyncManagerImpl
import com.jescoding.pixel.jjappandroid.core.domain.sync.ImageSyncManager
import com.jescoding.pixel.jjappandroid.core.domain.sync.SyncManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SyncModule {

    @Binds
    @Singleton
    abstract fun bindSyncManager(impl: SyncManagerImpl): SyncManager

    @Binds
    @Singleton
    abstract fun bindImageSyncManager(impl: ImageSyncManagerImpl): ImageSyncManager
}
