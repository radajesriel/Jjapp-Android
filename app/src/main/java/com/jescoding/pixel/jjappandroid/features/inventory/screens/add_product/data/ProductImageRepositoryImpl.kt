package com.jescoding.pixel.jjappandroid.features.inventory.screens.add_product.data

import android.content.Context
import android.net.Uri
import android.util.Log
import com.jescoding.pixel.jjappandroid.core.domain.dispatcher.DispatcherProvider
import com.jescoding.pixel.jjappandroid.features.inventory.screens.add_product.domain.repository.ProductImageRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.withContext
import java.io.File
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductImageRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dispatcherProvider: DispatcherProvider
) : ProductImageRepository {

    override suspend fun copyImageToInternalStorage(imageUri: Uri): Uri? {
        return withContext(dispatcherProvider.io) {
            try {
                val inputStream =
                    context.contentResolver.openInputStream(imageUri) ?: return@withContext null

                val fileName = "${UUID.randomUUID()}.jpg"
                val file = File(context.cacheDir, fileName)

                val outputStream = file.outputStream()

                inputStream.use { input ->
                    outputStream.use { output ->
                        input.copyTo(output)
                    }
                }

                Uri.fromFile(file)
            } catch (e: Exception) {
                Log.d("ProductImageRepo", "Error copying image: ${e.message}")
                null
            }
        }
    }
}