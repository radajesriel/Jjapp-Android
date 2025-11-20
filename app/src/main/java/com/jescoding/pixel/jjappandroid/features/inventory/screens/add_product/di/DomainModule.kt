package com.jescoding.pixel.jjappandroid.features.inventory.screens.add_product.di

import com.jescoding.pixel.jjappandroid.core.domain.repository.DashboardRepository
import com.jescoding.pixel.jjappandroid.features.inventory.screens.add_product.domain.repository.ProductImageRepository
import com.jescoding.pixel.jjappandroid.features.inventory.screens.add_product.domain.use_case.GetPermanentImageUri
import com.jescoding.pixel.jjappandroid.features.inventory.screens.add_product.domain.use_case.SaveProduct
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
    fun provideGetImageUri(repository: ProductImageRepository): GetPermanentImageUri {
        return GetPermanentImageUri(repository)
    }

    @Provides
    @Singleton
    fun provideSaveProduct(
        repository: DashboardRepository,
        getPermanentImageUri: GetPermanentImageUri
    ): SaveProduct {
        return SaveProduct(repository, getPermanentImageUri)
    }
}