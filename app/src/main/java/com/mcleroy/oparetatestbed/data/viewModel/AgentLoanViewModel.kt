package com.mcleroy.oparetatestbed.data.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.mcleroy.oparetatestbed.data.api.ServerResponse
import com.mcleroy.oparetatestbed.data.entity.LoanDurationEntity
import com.mcleroy.oparetatestbed.data.entity.LoanEntity
import com.mcleroy.oparetatestbed.data.entity.LoanRequestEntity
import com.mcleroy.oparetatestbed.data.repository.LoanRepository

class AgentLoanViewModel() : ViewModel() {
   
    private val loanRepository = LoanRepository.get()

    val loanRequests: LiveData<ServerResponse.Resource<MutableList<LoanRequestEntity>>>
        get() = loanRepository.getLoanRequests()

    fun requestLoan(
        currency: String,
        amount: Int,
        tenor: Int,
        termUnit: String
    ): LiveData<ServerResponse.Resource<LoanRequestEntity>> {
        val body = HashMap<String, Any>()
        body["currency"] = currency
        body["requestedAmount"] = amount
        body["requestedTerm"] = tenor
        body["requestedTermUnit"] = termUnit
        return loanRepository.requestLoan(body)
    }

    fun confirmLoan(uuid: String): LiveData<ServerResponse.Resource<LoanRequestEntity>> {
        val body = HashMap<String, Any>()
        body["status"] = "pending"
        return loanRepository.updateLoanRequest(uuid, body)
    }

    fun cancelLoanRequest(
        uuid: String,
        feedback: String
    ): LiveData<ServerResponse.Resource<LoanRequestEntity>> {
        val body = HashMap<String, Any>()
        body["status"] = "rejected"
        body["agentFeedback"] = feedback
        return loanRepository.updateLoanRequest(uuid, body)
    }

    fun submitPin(
        agentUUID: String,
        pin: String
    ): LiveData<ServerResponse.Resource<Map<String, Boolean>>> {
        val body = HashMap<String, String>()
        body["pin"] = pin
        return loanRepository.validatePin(agentUUID, body)
    }

    fun submitSignaturePhotoUrl(
        uuid: String,
        signaturePhotoUrl: String,
        pin: String
    ): LiveData<ServerResponse.Resource<LoanRequestEntity>> {
        val body = HashMap<String, Any>()
        body["status"] = "agent-signed"
        body["signaturePhotoUrl"] = signaturePhotoUrl
        body["pin"] = pin
        return loanRepository.updateLoanRequest(uuid, body)
    }

    fun getLoans(): LiveData<ServerResponse.Resource<MutableList<LoanEntity>>> {
        return loanRepository.getLoans()
    }

    fun getLoanDurations(): LiveData<ServerResponse.Resource<MutableList<LoanDurationEntity>>> {
        return loanRepository.getLoanDurations()
    }

    fun getLoanRequest(uuid: String): LiveData<ServerResponse.Resource<LoanRequestEntity>> {
        return loanRepository.getLoanRequest(uuid)
    }
}