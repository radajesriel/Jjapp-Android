package com.jescoding.pixel.jjappandroid.core.utils

object ValidationUtils {
    fun getValidatedCurrency(input: String): String {
        if (input.isEmpty()) return ""

        // Filter out non-digit and non-decimal characters, allowing only one decimal point.
        val filtered = input.filterIndexed { index, char ->
            char.isDigit() || (char == '.' && input.indexOf('.') == index)
        }

        // Ensure no more than two decimal places.
        val parts = filtered.split('.')
        return if (parts.size > 1 && parts[1].length > 2) {
            "${parts[0]}.${parts[1].substring(0, 2)}"
        } else {
            filtered
        }
    }

    fun getIntegerOnly(input: String): String {
        return input.filter { it.isDigit() }
    }

}