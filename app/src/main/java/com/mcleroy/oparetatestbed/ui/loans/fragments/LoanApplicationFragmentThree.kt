package com.mcleroy.oparetatestbed.ui.loans.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mcleroy.oparetatestbed.R
import com.mcleroy.oparetatestbed.databinding.FragmentLoanApplicationThreeBinding

class LoanApplicationFragmentThree : BaseLoanApplicationFragment() {

    private lateinit var binding: FragmentLoanApplicationThreeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLoanApplicationThreeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.proceedBtn.setOnClickListener {  }
        view.findViewById<View>(R.id.proceed_btn)
            .setOnClickListener { v: View? -> fragmentCallbacks!!.next() }
    }
}