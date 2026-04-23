package com.jescoding.pixel.jjappandroid.core.data.remote.storage

import android.content.Context
import android.net.Uri
import com.jescoding.pixel.jjappandroid.core.domain.auth.AuthManager
import com.jescoding.pixel.jjappandroid.core.domain.sync.ImageSyncManager
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.storage.storage
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageSyncManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val supabaseClient: SupabaseClient,
    private val authManager: AuthManager
) : ImageSyncManager {

    private val bucket get() = supabaseClient.storage["product-images"]

    override suspend fun uploadImage(localUri: Uri, itemSku: String): Result<String> {
        return runCatching {
            val userId = authManager.getCurrentUserId()
                ?: throw IllegalStateException("Not authenticated")

            val inputStream = context.contentResolver.openInputStream(localUri)
                ?: throw IllegalStateException("Cannot read image from $localUri")

            val bytes = inputStream.use { it.readBytes() }
            val remotePath = "$userId/$itemSku.jpg"

            bucket.upload(remotePath, bytes) { upsert = true }
            remotePath
        }
    }

    override suspend fun downloadImage(remotePath: String, itemSku: String): Result<Uri> {
        return runCatching {
            val bytes = bucket.downloadAuthenticated(remotePath)
            val file = File(context.filesDir, "product_images")
            file.mkdirs()
            val imageFile = File(file, "$itemSku.jpg")
            imageFile.writeBytes(bytes)
            Uri.fromFile(imageFile)
        }
    }
}
