package com.jescoding.pixel.jjappandroid.core.data.remote.auth

import com.jescoding.pixel.jjappandroid.core.domain.auth.AuthManager
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthManagerImpl @Inject constructor(
    private val supabaseClient: SupabaseClient
) : AuthManager {

    override suspend fun signInAnonymously(): Result<Unit> {
        return runCatching {
            val currentSession = supabaseClient.auth.currentSessionOrNull()
            if (currentSession != null) return Result.success(Unit)
            supabaseClient.auth.signInAnonymously()
        }
    }

    override fun getCurrentUserId(): String? {
        return supabaseClient.auth.currentUserOrNull()?.id
    }

    override fun isAuthenticated(): Boolean {
        return supabaseClient.auth.currentUserOrNull() != null
    }
}
