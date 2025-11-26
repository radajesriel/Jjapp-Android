package com.jescoding.pixel.jjappandroid.features.inventory.screens.add_edit_product.domain.use_case

import android.net.Uri
import com.jescoding.pixel.jjappandroid.core.domain.providers.DispatcherProvider
import com.jescoding.pixel.jjappandroid.features.inventory.screens.add_edit_product.domain.repository.ProductImageRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetPermanentImageUri @Inject constructor(
    val repository: ProductImageRepository,
    val dispatcherProvider: DispatcherProvider
) {
    suspend operator fun invoke(imageUri: Uri): Uri? {
        return withContext(dispatcherProvider.io) {
            repository.copyImageToInternalStorage(imageUri)
        }
    }
}