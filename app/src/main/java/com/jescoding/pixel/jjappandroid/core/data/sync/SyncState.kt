package com.jescoding.pixel.jjappandroid.core.data.sync

data class SyncState(
    val isSyncing: Boolean = false,
    val lastSyncTimestamp: Long? = null,
    val pendingChangesCount: Int = 0,
    val error: String? = null
)
