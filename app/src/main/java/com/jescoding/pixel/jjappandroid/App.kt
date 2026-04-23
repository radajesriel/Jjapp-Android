package com.jescoding.pixel.jjappandroid

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.work.Configuration
import com.jescoding.pixel.jjappandroid.core.data.sync.SyncWorkScheduler
import com.jescoding.pixel.jjappandroid.core.domain.auth.AuthManager
import com.jescoding.pixel.jjappandroid.core.domain.providers.NetworkMonitor
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class App : Application(), Configuration.Provider {

    @Inject
    lateinit var authManager: AuthManager

    @Inject
    lateinit var syncWorkScheduler: SyncWorkScheduler

    @Inject
    lateinit var networkMonitor: NetworkMonitor

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()

        applicationScope.launch {
            authManager.signInAnonymously()
        }

        syncWorkScheduler.schedulePeriodicSync()

        // Trigger sync on app foreground
        ProcessLifecycleOwner.get().lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onStart(owner: LifecycleOwner) {
                syncWorkScheduler.requestImmediateSync()
            }
        })

        // Trigger sync on network recovery
        applicationScope.launch {
            networkMonitor.isOnline
                .distinctUntilChanged()
                .filter { it }
                .collect {
                    syncWorkScheduler.requestImmediateSync()
                }
        }
    }
}