package com.mcleroy.oparetatestbed.data.entity

import java.util.*

data class LoanRequestEntity(
    var uuid: String,
    var loanAmount: String?,
    val currency: String,
    val requestedAmount: String,
    val requestedTerm: String,
    val interestRate: String,
    val interestRateIntervalUnit: String,
    val eligible: Boolean,
    val message: String,
    val term: Int = 0,
    val interest: Int = 0,
    val termUnit: String,
    val createdAt: Date?,
    val status: String?,
    val contractText: String?,

    val principal: String?,
    val paymentPlans: MutableList<PaymentPlanEntity> = mutableListOf(),
    val pin: String?
)