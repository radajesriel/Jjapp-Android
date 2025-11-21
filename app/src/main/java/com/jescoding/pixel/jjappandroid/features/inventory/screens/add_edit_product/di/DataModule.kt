package com.jescoding.pixel.jjappandroid.features.inventory.screens.add_edit_product.di

import com.jescoding.pixel.jjappandroid.features.inventory.screens.add_edit_product.data.ProductImageRepositoryImpl
import com.jescoding.pixel.jjappandroid.features.inventory.screens.add_edit_product.domain.repository.ProductImageRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    @Singleton
    abstract fun bindProductImageRepository(
        impl: ProductImageRepositoryImpl
    ): ProductImageRepository
}