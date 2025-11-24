package com.jescoding.pixel.jjappandroid.core.domain.providers

interface ResourceProvider {
    fun getString(resId: Int): String
    fun getDrawableUri(resId: Int): android.net.Uri
}