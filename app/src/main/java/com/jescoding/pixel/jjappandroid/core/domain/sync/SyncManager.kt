package com.jescoding.pixel.jjappandroid.core.domain.sync

interface SyncManager {
    suspend fun pushLocalChanges(): Result<Unit>
    suspend fun pullRemoteChanges(): Result<Unit>
    suspend fun fullSync(): Result<Unit>
}
