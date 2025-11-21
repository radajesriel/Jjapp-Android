package com.jescoding.pixel.jjappandroid.features.inventory.screens.add_edit_product.domain.repository

import android.net.Uri

interface ProductImageRepository {
    suspend fun copyImageToInternalStorage(imageUri: Uri): Uri?
}