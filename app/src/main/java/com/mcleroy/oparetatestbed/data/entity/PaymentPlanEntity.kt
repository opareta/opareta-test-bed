package com.mcleroy.oparetatestbed.data.entity

import java.util.*

data class PaymentPlanEntity(
    var status: String? = null,
    var currency: String? = null,
    var principal: String? = null,
    var interest: String? = null,
    var total: String? = null,
    var uuid: String? = null,
    var expectedAt: Date? = null,
    val formattedExpectedAt: String,
)