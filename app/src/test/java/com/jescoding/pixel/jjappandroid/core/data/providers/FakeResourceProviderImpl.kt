package com.jescoding.pixel.jjappandroid.core.data.providers

import android.net.Uri
import com.jescoding.pixel.jjappandroid.core.domain.providers.ResourceProvider

class FakeResourceProviderImpl : ResourceProvider {
     companion object {
        const val ADD_HEADER_ID = 1
        const val SAVE_BUTTON_ID = 2
        const val EDIT_HEADER_ID = 3
        const val UPDATE_BUTTON_ID = 4
    }

    override fun getString(resId: Int): String {
        return when (resId) {
            com.jescoding.pixel.jjappandroid.R.string.text_label_add_product -> this.getString(
                ADD_HEADER_ID
            )

            com.jescoding.pixel.jjappandroid.R.string.button_label_save_product -> this.getString(
                SAVE_BUTTON_ID
            )

            com.jescoding.pixel.jjappandroid.R.string.text_label_edit_product -> this.getString(
                EDIT_HEADER_ID
            )

            com.jescoding.pixel.jjappandroid.R.string.button_label_update_product -> this.getString(
                UPDATE_BUTTON_ID
            )

            else -> ""
        }
    }

    override fun getDrawableUri(resId: Int): Uri {
        TODO("Not yet implemented")
    }
}