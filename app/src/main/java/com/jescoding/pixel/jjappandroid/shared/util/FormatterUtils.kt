package com.jescoding.pixel.jjappandroid.shared.util

import android.icu.text.NumberFormat

fun createNumberFormatter(): NumberFormat {
    return NumberFormat.getInstance().apply {
        maximumFractionDigits = 2
        minimumFractionDigits = 0
    }
}
