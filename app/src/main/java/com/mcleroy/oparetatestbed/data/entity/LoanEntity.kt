package com.mcleroy.oparetatestbed.data.entity

import java.util.*

data class LoanEntity(
    var uuid: String?,
    var loanId: String?,
    var status: String?,
    var loanAmount: Double?,
    var durationInMonths: String,
    var monthlyInterest: String,
    var endDate: Date? = null
) {

    fun endDateToCalender(): Calendar {
        val nonNullEndDate = if (endDate != null) endDate!! else Date()
        val calendar = Calendar.getInstance()
        calendar.time = nonNullEndDate
        return calendar
    }

    val isActive: Boolean
        get() = !status.equals("completed", ignoreCase = true)
}