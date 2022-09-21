package com.mcleroy.oparetatestbed.ui.loans.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.mcleroy.oparetatestbed.data.entity.PaymentPlanEntity
import com.mcleroy.oparetatestbed.data.viewModel.AgentLoanViewModel
import com.mcleroy.oparetatestbed.databinding.FragmentLoanApplicationTwoBinding
import com.mcleroy.oparetatestbed.utils.Number
import com.mcleroy.oparetatestbed.utils.Utils
import timber.log.Timber

class LoanApplicationFragmentTwo : BaseLoanApplicationFragment() {
    private lateinit var viewModel: AgentLoanViewModel
    private lateinit var binding: FragmentLoanApplicationTwoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(AgentLoanViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLoanApplicationTwoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.proceedBtn.setOnClickListener { confirmLoan() }
        updateUI()
    }

    private fun updateUI() {
        val loanRequest = fragmentCallbacks!!.loanRequest ?: return
        binding.loanAmountText.text = Utils.formatAmount(
            loanRequest.requestedAmount,
            loanRequest.currency
        )
        binding.durationText.text = String.format(
            "%s %s",
            loanRequest.requestedTerm,
            loanRequest.termUnit
        )
        val paymentPlan: PaymentPlanEntity? =
            if (loanRequest.paymentPlans.isNotEmpty()) loanRequest.paymentPlans[0] else null
        binding.weeklyAmountText.text = if (paymentPlan != null) Utils.formatAmount(
            paymentPlan.total,
            paymentPlan.currency
        ) else "Unavailable"
        binding.weeklyPaymentText.text = "${loanRequest.paymentPlans.size}"
        binding.interestRateText.text = String.format(
            "%s%% per %s",
            Number.formatInterest(loanRequest.interestRate),
            loanRequest.interestRateIntervalUnit
        )
        binding.applicationDateText.text = loanRequest.createdAt?.toString()
    }

    private fun confirmLoan() {
        val loanRequest = fragmentCallbacks!!.loanRequest ?: return
        viewModel.confirmLoan(loanRequest.uuid).observe(viewLifecycleOwner) { resource ->
            binding.progressView.visibility =
                if (resource.isLoading) View.VISIBLE else View.GONE
            if (resource.isLoading) return@observe
            if (resource.ex != null) {
                Utils.makeToast(requireContext(), resource.errorMessage ?: "")
                Timber.e("Error: %s", resource.errorMessage)
            } else {
                fragmentCallbacks!!.next()
            }
        }
    }
}