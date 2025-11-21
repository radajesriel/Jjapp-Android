package com.jescoding.pixel.jjappandroid.core.data.providers

import android.content.Context
import com.jescoding.pixel.jjappandroid.core.domain.providers.ResourceProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ResourceProviderImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ResourceProvider {

    override fun getString(resId: Int): String {
        return context.getString(resId)
    }
}