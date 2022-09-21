package com.mcleroy.oparetatestbed.ui.loans.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.mcleroy.oparetatestbed.R
import com.mcleroy.oparetatestbed.data.entity.LoanDurationEntity
import com.mcleroy.oparetatestbed.data.viewModel.AgentLoanViewModel
import com.mcleroy.oparetatestbed.databinding.FragmentLoanApplicationOneBinding
import com.mcleroy.oparetatestbed.utils.Utils
import timber.log.Timber
import java.util.*

class LoanApplicationFragmentOne : BaseLoanApplicationFragment() {
    private lateinit var viewModel: AgentLoanViewModel
    private lateinit var binding: FragmentLoanApplicationOneBinding


    private var loanDurations: MutableList<LoanDurationEntity> = ArrayList<LoanDurationEntity>()
    private val loanDurationMap: MutableMap<String, LoanDurationEntity> =
        HashMap<String, LoanDurationEntity>()
    private var completeKycDialog: AlertDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(AgentLoanViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLoanApplicationOneBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<View>(R.id.proceed_btn).setOnClickListener { v: View? -> requestLoan() }
        binding.paybackPeriodText.setOnClickListener { v: View ->
            val imm =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(v.windowToken, 0)
        }
        getLoanDurations()
    }

    private fun getLoanDurations() {
        viewModel.getLoanDurations().observe(viewLifecycleOwner) { listResource ->
            binding.progressView.visibility =
                if (listResource.isLoading) View.VISIBLE else View.GONE
            if (listResource.ex != null) Utils.makeToast(
                requireContext(),
                listResource.errorMessage!!
            ) else if (listResource.data != null) {
                setupPaybackPeriods(listResource.data)
            }
        }
    }

    private fun setupPaybackPeriods(loanDurations: MutableList<LoanDurationEntity>) {
        this.loanDurations = loanDurations
        loanDurations.sortWith { o1, o2 -> o1.duration - o2.duration }
        // For sorting because neither TreeMap nor HashMap seems to sort correctly
        try {
            val sortedKeys: MutableList<String> = ArrayList()
            for (loanDuration in loanDurations) {
                sortedKeys.add(
                    java.lang.String.format(
                        "%s %s",
                        loanDuration.getDuration(),
                        loanDuration.getDurationUnit()
                    )
                )
                loanDurationMap[java.lang.String.format(
                    "%s %s",
                    loanDuration.getDuration(),
                    loanDuration.getDurationUnit()
                )] = loanDuration
            }
            val adapter: ArrayAdapter<String> =
                ArrayAdapter<String>(requireContext(), R.layout.dropdown_item, sortedKeys)
            binding.paybackPeriodText.setAdapter<ArrayAdapter<String>>(adapter)
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    private fun requestLoan() {
        var amount: Double? = null
        amount = try {
            binding.loanAmountText.getText().toString().toDouble()
        } catch (e: NumberFormatException) {
            Timber.e(e)
            Utils.makeToast(requireContext(), "Invalid amount")
            return
        }
        val loanDuration: LoanDurationEntity? =
            loanDurationMap[binding.paybackPeriodText.getText().toString()]
        if (loanDuration == null) {
            Utils.makeToast(requireContext(), "Please select payback period")
            return
        }
        viewModel.requestLoan(
            "UGX",
            amount!!.toInt(),
            loanDuration.getDuration(),
            loanDuration.getDurationUnit()
        ).observe(
            viewLifecycleOwner
        ) { resource ->
            binding.progressView.visibility = if (resource.isLoading) View.VISIBLE else View.GONE
            if (resource.data != null) {
                Timber.d("Loan entity: %s", Gson().toJson(resource.data))
                fragmentCallbacks!!.loanRequest = resource.data
                fragmentCallbacks!!.next()
            } else if (resource.ex != null) {
                val errorMessage: String = resource.errorMessage ?: ""
                if (errorMessage.lowercase(Locale.getDefault()).contains(
                        "Incomplete agent profile".lowercase(
                            Locale.getDefault()
                        )
                    )
                ) {
                    showIncompleteKycDialog()
                } else {
                    Utils.makeToast(requireContext(), errorMessage)
                    Timber.e("Error: %s", resource.errorMessage)
                }
            }
        }
    }

    private fun showIncompleteKycDialog() {
        if (completeKycDialog != null) completeKycDialog!!.dismiss()
        completeKycDialog = AlertDialog.Builder(requireContext())
            .setTitle(R.string.complete_your_profile)
            .setMessage(R.string.incomplete_kyc_loan_request_message)
            .setNegativeButton(R.string.later) { _, _ -> if (fragmentCallbacks != null) fragmentCallbacks!!.finish() }
            .setPositiveButton(R.string.complete_now) { _, _ -> if (fragmentCallbacks != null) fragmentCallbacks!!.redirectToKyc() }
            .show()
    }
}