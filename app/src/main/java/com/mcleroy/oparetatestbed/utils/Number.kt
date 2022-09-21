package com.mcleroy.oparetatestbed.utils

import java.text.DecimalFormat

object Number {
    fun formatInterest(interest: String): String {
        return try {
            DecimalFormat("#.#").format(interest.toDouble())
        } catch (e: NumberFormatException) {
            ""
        }
    }
}