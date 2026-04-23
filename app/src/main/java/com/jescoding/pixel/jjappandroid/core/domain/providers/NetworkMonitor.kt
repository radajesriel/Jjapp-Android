package com.jescoding.pixel.jjappandroid.core.domain.providers

import kotlinx.coroutines.flow.Flow

interface NetworkMonitor {
    val isOnline: Flow<Boolean>
    val currentlyOnline: Boolean
}
