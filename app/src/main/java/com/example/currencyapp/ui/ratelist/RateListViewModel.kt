package com.example.currencyapp.ui.ratelist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyapp.R
import com.example.currencyapp.commons.Status
import com.example.currencyapp.commons.handleResult
import com.example.currencyapp.data.model.RatesResponse
import com.example.currencyapp.ui.ratelist.adapter.RateItemUIModel
import com.example.currencyapp.ui.ratelist.usecase.LoadRatesUseCase
import com.example.currencyapp.util.extensions.calculateCurrencyValues
import com.example.currencyapp.util.extensions.deepCopy
import com.example.currencyapp.util.extensions.updateFirstElement
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.math.BigDecimal

// This corresponds to 1 second, requests will be sent on each second
private const val REQUEST_FREQUENCY = 1000L
private const val BASE_CURRENCY = "EUR"
private val BASE_VALUE = BigDecimal.ONE

class RateListViewModel(
    private val loadRatesUseCase: LoadRatesUseCase
) : ViewModel() {

    private var currentList = emptyList<RateItemUIModel>()

    private val rates = MutableLiveData<Status<List<RateItemUIModel>, Int>>()
    fun getRates(): LiveData<Status<List<RateItemUIModel>, Int>> = rates

    // Requests are being sent always according to the base rate item's currency
    private var baseRateItem = RateItemUIModel(
        currency = BASE_CURRENCY,
        value = BASE_VALUE
    )

    private var multiplier = BASE_VALUE

    private var job: Job? = null

    fun onSelectedItemChanged(newBaseRateItem: RateItemUIModel) {
        cancelJob()

        updateBaseRateItem(newBaseRateItem)
        updateMultiplier(newBaseRateItem.value)

        job = repeatRequest()
    }

    fun onSelectedItemValueChanged(model: RateItemUIModel, newValue: String) {
        if (baseRateItem.currency == model.currency) {
            updateMultiplier(newValue.toBigDecimalOrNull() ?: BigDecimal.ZERO)
            updateBaseRateItem(RateItemUIModel(baseRateItem.currency, multiplier))
            val updatedList = currentList.deepCopy().updateFirstElement(baseRateItem)
            updatedList.calculateCurrencyValues(multiplier)
            setRatesList(updatedList)
        }
    }

    fun fetchData() {
        cancelJob()
        job = repeatRequest()
    }

    private fun cancelJob() {
        job?.cancel()
    }

    private fun repeatRequest(): Job {
        return viewModelScope.launch {
            while (isActive) {
                loadRatesUseCase.execute(baseRateItem.currency).handleResult(
                    onSuccess = { setRatesList(processResponse(it)) },
                    onFailure = { handleFailure() }
                )
                delay(REQUEST_FREQUENCY)
            }
        }
    }

    // Here, we map the response to the UI model, sorting it and
    // multiply the list values with the specified number
    private fun processResponse(response: RatesResponse?): List<RateItemUIModel> {
        if (response == null) return emptyList()

        val responseList = response.rates.map { RateItemUIModel(it) }.toMutableList()
        responseList.sortBy { it.currency }
        responseList.add(0, baseRateItem)
        currentList = responseList.map { it.copy() }.toMutableList()
        responseList.calculateCurrencyValues(multiplier)
        return responseList
    }

    private fun setRatesList(rateList: List<RateItemUIModel>) {
        rates.value = Status.Success(rateList)
    }

    private fun handleFailure() {
        rates.value = Status.Error(R.string.something_went_wrong_error)
    }

    private fun updateBaseRateItem(newBaseRateItem: RateItemUIModel) {
        baseRateItem = RateItemUIModel(newBaseRateItem.currency, newBaseRateItem.value)
    }

    private fun updateMultiplier(newMultiplier: BigDecimal) {
        multiplier = newMultiplier
    }
}