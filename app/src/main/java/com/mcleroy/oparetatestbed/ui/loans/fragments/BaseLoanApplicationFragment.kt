package com.mcleroy.oparetatestbed.ui.loans.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment

abstract class BaseLoanApplicationFragment : Fragment() {
    protected var index = 0
    protected var fragmentCallbacks: LoanApplicationFragmentCallbacks? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val arguments = arguments
        index = savedInstanceState?.getInt(ARG_INDEX)
            ?: (arguments?.getInt(ARG_INDEX) ?: -1)
        fragmentCallbacks!!.setIndex(index)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(ARG_INDEX, index)
        super.onSaveInstanceState(outState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentCallbacks = try {
            context as LoanApplicationFragmentCallbacks
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement LoanApplicationFragmentCallbacks: $e")
        }
    }

    override fun onDetach() {
        super.onDetach()
        fragmentCallbacks = null
    }

    fun withStartArguments(index: Int): BaseLoanApplicationFragment {
        val bundle = Bundle()
        bundle.putInt(ARG_INDEX, index)
        return this
    }

    companion object {
        private const val ARG_INDEX = "index"
    }
}