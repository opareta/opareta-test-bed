package com.mcleroy.oparetatestbed.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mcleroy.oparetatestbed.databinding.ActivityMainBinding
import com.mcleroy.oparetatestbed.ui.loans.LoanApplicationActivity


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.requestLoanBtn.setOnClickListener {
            startActivity(LoanApplicationActivity.getStartIntent(this))
        }
    }
}