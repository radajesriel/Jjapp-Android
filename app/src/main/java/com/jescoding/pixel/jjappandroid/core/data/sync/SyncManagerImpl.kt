package com.jescoding.pixel.jjappandroid.core.data.sync

import android.util.Log
import androidx.core.net.toUri
import com.jescoding.pixel.jjappandroid.core.data.local.real.dao.DashboardDao
import com.jescoding.pixel.jjappandroid.core.data.remote.datasource.RemoteDashboardDataSource
import com.jescoding.pixel.jjappandroid.core.data.remote.dto.toDto
import com.jescoding.pixel.jjappandroid.core.data.remote.dto.toEntity
import com.jescoding.pixel.jjappandroid.core.domain.auth.AuthManager
import com.jescoding.pixel.jjappandroid.core.domain.providers.DispatcherProvider
import com.jescoding.pixel.jjappandroid.core.domain.sync.ImageSyncManager
import com.jescoding.pixel.jjappandroid.core.domain.sync.SyncManager
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SyncManagerImpl @Inject constructor(
    private val dao: DashboardDao,
    private val remoteDataSource: RemoteDashboardDataSource,
    private val authManager: AuthManager,
    private val syncPreferences: SyncPreferences,
    private val dispatcherProvider: DispatcherProvider,
    private val imageSyncManager: ImageSyncManager
) : SyncManager {

    override suspend fun pushLocalChanges(): Result<Unit> = withContext(dispatcherProvider.io) {
        runCatching {
            val userId = authManager.getCurrentUserId() ?: return@runCatching
            val pendingItems = dao.getItemsPendingSync()

            for (item in pendingItems) {
                when (item.syncStatus) {
                    SyncStatus.PENDING_CREATE, SyncStatus.PENDING_UPDATE -> {
                        var dto = item.toDto(userId)

                        // Upload image if it's a local file
                        val itemUri = item.itemUri
                        if (itemUri != null && isLocalUri(itemUri)) {
                            imageSyncManager.uploadImage(itemUri.toUri(), item.itemSku)
                                .onSuccess { remotePath ->
                                    dto = dto.copy(itemUri = remotePath)
                                }
                                .onFailure {
                                    Log.d(TAG, "Failed to upload image for ${item.itemSku}: ${it.message}")
                                }
                        }

                        remoteDataSource.upsertItem(dto).onSuccess {
                            dao.updateSyncStatus(item.itemSku, SyncStatus.SYNCED)
                        }.onFailure {
                            Log.d(TAG, "Failed to push item ${item.itemSku}: ${it.message}")
                        }
                    }

                    SyncStatus.PENDING_DELETE -> {
                        remoteDataSource.softDeleteItem(item.itemSku, userId).onSuccess {
                            dao.deleteItemBySku(item.itemSku)
                        }.onFailure {
                            Log.d(TAG, "Failed to delete item ${item.itemSku}: ${it.message}")
                        }
                    }
                }
            }
        }
    }

    override suspend fun pullRemoteChanges(): Result<Unit> = withContext(dispatcherProvider.io) {
        runCatching {
            val userId = authManager.getCurrentUserId() ?: return@runCatching
            val lastSync = syncPreferences.lastSyncTimestamp

            val remoteItems = remoteDataSource.getItemsModifiedAfter(lastSync, userId)
                .getOrElse {
                    Log.d(TAG, "Failed to pull remote changes: ${it.message}")
                    return@runCatching
                }

            for (remoteItem in remoteItems) {
                if (remoteItem.isDeleted) {
                    dao.deleteItemBySku(remoteItem.itemSku)
                    continue
                }

                val localItem = dao.getItemBySku(remoteItem.itemSku)
                var remoteEntity = remoteItem.toEntity()

                if (localItem == null ||
                    localItem.syncStatus == SyncStatus.SYNCED ||
                    remoteEntity.updatedAt > localItem.updatedAt
                ) {
                    // Download image if remote has one and it's a remote path
                    val remoteImageUri = remoteItem.itemUri
                    if (remoteImageUri != null && !isLocalUri(remoteImageUri)) {
                        imageSyncManager.downloadImage(remoteImageUri, remoteItem.itemSku)
                            .onSuccess { localUri ->
                                remoteEntity = remoteEntity.copy(itemUri = localUri.toString())
                            }
                            .onFailure {
                                Log.d(TAG, "Failed to download image for ${remoteItem.itemSku}: ${it.message}")
                            }
                    }
                    dao.insertItem(remoteEntity)
                }
                // If local is newer and has pending changes, skip (local wins)
            }

            syncPreferences.lastSyncTimestamp = Clock.System.now().toString()
        }
    }

    override suspend fun fullSync(): Result<Unit> {
        if (!authManager.isAuthenticated()) {
            authManager.signInAnonymously().onFailure {
                return Result.failure(it)
            }
        }

        // Push first to avoid overwriting local changes
        pushLocalChanges()
        return pullRemoteChanges()
    }

    private fun isLocalUri(uri: String): Boolean {
        return uri.startsWith("file://") || uri.startsWith("/") || uri.startsWith("content://")
    }

    companion object {
        private const val TAG = "SyncManager"
    }
}
