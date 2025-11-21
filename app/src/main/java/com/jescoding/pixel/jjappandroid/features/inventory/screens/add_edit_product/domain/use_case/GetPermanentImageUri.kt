package com.jescoding.pixel.jjappandroid.features.inventory.screens.add_edit_product.domain.use_case

import android.net.Uri
import com.jescoding.pixel.jjappandroid.features.inventory.screens.add_edit_product.domain.repository.ProductImageRepository
import javax.inject.Inject

class GetPermanentImageUri @Inject constructor(
    val repository: ProductImageRepository
) {
    suspend operator fun invoke(imageUri: Uri): Uri? {
        return repository.copyImageToInternalStorage(imageUri)
    }
}