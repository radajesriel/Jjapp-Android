package com.jescoding.pixel.jjappandroid.core.domain.auth

interface AuthManager {
    suspend fun signInAnonymously(): Result<Unit>
    fun getCurrentUserId(): String?
    fun isAuthenticated(): Boolean
}
