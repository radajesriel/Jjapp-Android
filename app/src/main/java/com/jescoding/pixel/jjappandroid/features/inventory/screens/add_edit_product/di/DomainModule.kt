package com.jescoding.pixel.jjappandroid.features.inventory.screens.add_edit_product.di

import com.jescoding.pixel.jjappandroid.core.domain.providers.DispatcherProvider
import com.jescoding.pixel.jjappandroid.core.domain.repository.DashboardRepository
import com.jescoding.pixel.jjappandroid.features.inventory.screens.add_edit_product.domain.repository.ProductImageRepository
import com.jescoding.pixel.jjappandroid.features.inventory.screens.add_edit_product.domain.use_case.DeleteProduct
import com.jescoding.pixel.jjappandroid.features.inventory.screens.add_edit_product.domain.use_case.GetPermanentImageUri
import com.jescoding.pixel.jjappandroid.features.inventory.screens.add_edit_product.domain.use_case.SaveProduct
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
    fun provideGetImageUri(
        repository: ProductImageRepository,
        dispatcherProvider: DispatcherProvider
    ): GetPermanentImageUri {
        return GetPermanentImageUri(repository, dispatcherProvider)
    }

    @Provides
    @Singleton
    fun provideSaveProduct(
        repository: DashboardRepository,
        getPermanentImageUri: GetPermanentImageUri,
        dispatcherProvider: DispatcherProvider
    ): SaveProduct {
        return SaveProduct(repository, getPermanentImageUri, dispatcherProvider)
    }

    @Provides
    @Singleton
    fun provideDeleteProduct(
        repository: DashboardRepository,
        dispatcherProvider: DispatcherProvider
    ): DeleteProduct {
        return DeleteProduct(
            repository,
            dispatcherProvider
        )
    }

}