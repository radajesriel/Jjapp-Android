package com.jescoding.pixel.jjappandroid.core.data.sync

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SyncPreferences @Inject constructor(
    @ApplicationContext context: Context
) {
    private val prefs = context.getSharedPreferences("sync_prefs", Context.MODE_PRIVATE)

    var lastSyncTimestamp: String
        get() = prefs.getString(KEY_LAST_SYNC, "1970-01-01T00:00:00Z") ?: "1970-01-01T00:00:00Z"
        set(value) = prefs.edit().putString(KEY_LAST_SYNC, value).apply()

    companion object {
        private const val KEY_LAST_SYNC = "last_sync_timestamp"
    }
}
