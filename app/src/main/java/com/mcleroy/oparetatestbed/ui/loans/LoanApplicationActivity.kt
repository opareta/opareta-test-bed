package com.mcleroy.oparetatestbed.ui.loans

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.mcleroy.oparetatestbed.R
import com.mcleroy.oparetatestbed.data.entity.LoanRequestEntity
import com.mcleroy.oparetatestbed.data.entity.TabFragmentItem
import com.mcleroy.oparetatestbed.data.viewModel.AgentLoanViewModel
import com.mcleroy.oparetatestbed.databinding.ActivityLoanApplicationBinding
import com.mcleroy.oparetatestbed.ui.loans.fragments.LoanApplicationFragmentCallbacks
import com.mcleroy.oparetatestbed.ui.loans.fragments.LoanApplicationFragmentOne
import com.mcleroy.oparetatestbed.ui.loans.fragments.LoanApplicationFragmentThree
import com.mcleroy.oparetatestbed.ui.loans.fragments.LoanApplicationFragmentTwo

class LoanApplicationActivity : AppCompatActivity(), LoanApplicationFragmentCallbacks {
    private lateinit var viewModel: AgentLoanViewModel
    private lateinit var binding: ActivityLoanApplicationBinding

    private val fragments: MutableList<TabFragmentItem> = mutableListOf()

    private var currentIndex = 0
    private var uuid: String? = null

    override var loanRequest: LoanRequestEntity? = null

    private var fragmentManager: FragmentManager? = null
    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoanApplicationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(AgentLoanViewModel::class.java)
        uuid = intent.getStringExtra(EXTRA_LOAN_REQUEST_UUID)
        fragmentManager = supportFragmentManager
        if (uuid == null) {
            // This is a new loan request
            addFragment(0, LoanApplicationFragmentOne().withStartArguments(0))
            addFragment(1, LoanApplicationFragmentTwo().withStartArguments(1))
            addFragment(2, LoanApplicationFragmentThree().withStartArguments(2))
            if (savedInstanceState == null) setIndex(0)
            //            addFragments(savedInstanceState, Arrays.asList(
//                    new LoanApplicationFragmentOne().withStartArguments(0),
//                    new LoanApplicationFragmentTwo().withStartArguments(1),
//                    new LoanApplicationFragmentThree().withStartArguments(2)
//            ));
        } else {
            // This is an old loan request
            getLoanRequestFromNetwork(savedInstanceState)
        }
    }

    private fun addFragment(index: Int, fragment: Fragment) {
        fragments.add(TabFragmentItem(index, fragment))
        binding.progressBar.setMax(fragments.size)
    }

    private fun getLoanRequestFromNetwork(savedInstanceState: Bundle?) {
        viewModel.getLoanRequest(uuid!!).observe(this) { loanRequestResource ->
            binding.progressBarTwo.visibility = if (loanRequestResource.isLoading) View.VISIBLE else View.GONE
            if (!loanRequestResource.isLoading && loanRequestResource.data != null) {
                loanRequest = loanRequestResource.data
                if (loanRequest!!.status.equals("requested")) {
                    addFragment(0, LoanApplicationFragmentTwo().withStartArguments(0))
                    addFragment(1, LoanApplicationFragmentThree().withStartArguments(1))
                } else if (loanRequest!!.status.equals("pending")) {
                    addFragment(0, LoanApplicationFragmentThree().withStartArguments(0))
                } else {
//                    addFragment(0, LoanApplicationFragmentFour().withStartArguments(0))
//                    //                    loanApplicationFragments.add(new LoanApplicationFragmentFour().withStartArguments(1));
//                    if (!loanRequest.status.equals("rejected")) {
//                        addFragment(1, LoanApplicationFragmentFive().withStartArguments(1))
//                        addFragment(2, LoanApplicationFragmentSix().withStartArguments(2))
//                        addFragment(3, LoanApplicationFragmentSeven().withStartArguments(3))
//                        addFragment(4, LoanApplicationFragmentEight().withStartArguments(4))
//                    }
                }
                if (savedInstanceState == null) setIndex(0)
                //                addFragments(savedInstanceState, loanApplicationFragments);
            }
        }
    }

    override fun setIndex(index: Int) {
        if (index < 0 || index > fragments.size - 1) return
        val addToBackStack = fragmentManager!!.findFragmentById(R.id.fragment_container) != null
        currentIndex = index
        val fragment: Fragment = fragments[index].fragment ?: return
        val transaction = fragmentManager!!.beginTransaction()
            .replace(R.id.fragment_container, fragment)
        //        if (addToBackStack)
//            transaction.addToBackStack(LoanApplicationActivity.class.getName());
        transaction.commit()
        binding.progressBar.setProgress(index + 1)
    }

    override operator fun next() {
        val nextIndex = currentIndex + 1
        if (nextIndex > fragments.size - 1) finish() else setIndex(nextIndex)
    }

    override fun previous() {}

    override fun restartApplication() {
        startActivity(getStartIntent(this, null))
        finish()
        overridePendingTransition(0, 0)
    }

    override fun cancelApplication() {

    }

    protected override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CANCEL_LOAN) {
            if (resultCode == Activity.RESULT_OK) finish()
        }
    }

    override fun onBackPressed() {
        if (currentIndex == 0) {
            super.onBackPressed()
        } else {
            val previousIndex = currentIndex - 1
            setIndex(previousIndex)
        }
    }

    override fun redirectToKyc() {
    }

    companion object {
        private const val EXTRA_LOAN_REQUEST_UUID = "loan_request_uuid"
        private const val REQUEST_CANCEL_LOAN = 1
        fun getStartIntent(context: Context, uuid: String? = null): Intent {
            val intent = Intent(context, LoanApplicationActivity::class.java)
            intent.putExtra(EXTRA_LOAN_REQUEST_UUID, uuid)
            return intent
        }
    }
}