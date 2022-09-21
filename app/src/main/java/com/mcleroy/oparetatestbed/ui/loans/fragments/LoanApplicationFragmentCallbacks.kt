package com.mcleroy.oparetatestbed.ui.loans.fragments

import com.mcleroy.oparetatestbed.data.entity.LoanRequestEntity

interface LoanApplicationFragmentCallbacks {
    fun redirectToKyc()
    fun setIndex(index: Int)
    operator fun next()
    fun previous()
    fun restartApplication()
    fun cancelApplication()
    fun finish()
    var loanRequest: LoanRequestEntity?
}