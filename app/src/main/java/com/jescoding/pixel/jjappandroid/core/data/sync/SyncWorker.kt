package com.jescoding.pixel.jjappandroid.core.data.sync

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.jescoding.pixel.jjappandroid.core.domain.sync.SyncManager
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val syncManager: SyncManager
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return syncManager.fullSync().fold(
            onSuccess = {
                Log.d(TAG, "Sync completed successfully")
                Result.success()
            },
            onFailure = {
                Log.d(TAG, "Sync failed: ${it.message}")
                if (runAttemptCount < MAX_RETRIES) Result.retry() else Result.failure()
            }
        )
    }

    companion object {
        const val TAG = "SyncWorker"
        const val WORK_NAME_PERIODIC = "periodic_sync"
        const val WORK_NAME_ONE_SHOT = "one_shot_sync"
        private const val MAX_RETRIES = 3
    }
}
