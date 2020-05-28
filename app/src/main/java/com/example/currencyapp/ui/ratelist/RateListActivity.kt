package com.example.currencyapp.ui.ratelist

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.currencyapp.R
import com.example.currencyapp.commons.Status
import com.example.currencyapp.ui.ratelist.adapter.RateItemUIModel
import com.example.currencyapp.ui.ratelist.adapter.RateListAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class RateListActivity : AppCompatActivity() {

    private val adapter: RateListAdapter by lazy {
        RateListAdapter(
            ::handleCurrencyRowClick,
            ::handleTextChanged
        )
    }
    private val viewModel: RateListViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initRecyclerView()
        setUpObservers()
        viewModel.fetchData()
    }

    private fun initRecyclerView() {
        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                super.onItemRangeMoved(fromPosition, toPosition, itemCount)
                recyclerView.scrollToPosition(0)
            }
        })
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun setUpObservers() {
        viewModel.getRates().observe(this, Observer { status ->
            when (status) {
                is Status.Success -> showRatesList(status.data)
                is Status.Error -> showErrorMessage(getString(status.error))
            }
        })
    }

    private fun handleCurrencyRowClick(row: RateItemUIModel) {
        viewModel.onSelectedItemChanged(row)
    }

    private fun handleTextChanged(model: RateItemUIModel, newValue: String) {
        viewModel.onSelectedItemValueChanged(model, newValue)
    }

    private fun showRatesList(response: List<RateItemUIModel>) {
        adapter.showRateList(response)
    }

    private fun showErrorMessage(errorMessage: String) {
        Snackbar.make(constraintLayout, errorMessage, Snackbar.LENGTH_LONG).show()
    }

    companion object {
        fun newIntent(activity: Activity) = Intent(activity, RateListActivity::class.java)
    }
}
