package com.jescoding.pixel.jjappandroid.core.domain.sync

import android.net.Uri

interface ImageSyncManager {
    suspend fun uploadImage(localUri: Uri, itemSku: String): Result<String>
    suspend fun downloadImage(remotePath: String, itemSku: String): Result<Uri>
}
