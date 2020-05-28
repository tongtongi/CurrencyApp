package com.example.currencyapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.currencyapp.commons.ErrorModel
import com.example.currencyapp.commons.Result
import com.example.currencyapp.commons.Status
import com.example.currencyapp.data.model.RatesResponse
import com.example.currencyapp.ui.ratelist.RateListViewModel
import com.example.currencyapp.ui.ratelist.adapter.RateItemUIModel
import com.example.currencyapp.ui.ratelist.usecase.LoadRatesUseCase
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import java.math.BigDecimal

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class RateListViewModelTest {

    private val testDispatcher = TestCoroutineDispatcher()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var useCase: LoadRatesUseCase

    private lateinit var viewModel: RateListViewModel

    private var lastStatus: Status<List<RateItemUIModel>, Int>? = null

    @Before
    fun setUp() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(testDispatcher)


        viewModel = RateListViewModel(useCase)

        viewModel.getRates().observeForever {
            lastStatus = it
        }
    }

    @Test
    fun `fetchData() should set status to ERROR when request fails`() {
        // GIVEN
        val expectedError = R.string.something_went_wrong_error
        whenever(runBlocking { (useCase.execute(any())) }).thenReturn(Result.Failure(ErrorModel.DEFAULT))

        // WHEN
        viewModel.fetchData()

        // THEN
        Assert.assertEquals(
            (viewModel.getRates().value as Status.Error).error,
            expectedError
        )
    }

    @Test
    fun `fetchData() should set status to SUCCESS when request is successful`() {
        // Expected result is in alphabetic order
        val expectedResult = listOf(
            RateItemUIModel(currency = "EUR", value = BigDecimal("1")),
            RateItemUIModel(currency = "AUD", value = BigDecimal("1.61")),
            RateItemUIModel(currency = "BRL", value = BigDecimal("4.23")),
            RateItemUIModel(currency = "USD", value = BigDecimal("1.13"))
        )
        whenever(runBlocking { useCase.execute(any()) }).thenReturn(mockResponse())

        viewModel.fetchData()

        Assert.assertEquals((lastStatus as Status.Success).data, expectedResult)
    }

    @Test
    fun `onSelectedItemChanged() should update rate list according to newly selected item`() {
        val selectedItem = RateItemUIModel(currency = "CAD", value = BigDecimal("1.50"))
        whenever(runBlocking { useCase.execute(any()) }).thenReturn(mockResponse())
        viewModel.fetchData() // Now we have the initial response

        val expectedList = listOf(
            selectedItem,
            RateItemUIModel(
                currency = "AUD",
                value = BigDecimal("1.61").multiply(selectedItem.value)
            ),
            RateItemUIModel(
                currency = "BRL",
                value = BigDecimal("4.23").multiply(selectedItem.value)
            ),
            RateItemUIModel(
                currency = "USD",
                value = BigDecimal("1.13").multiply(selectedItem.value)
            )
        )

        viewModel.onSelectedItemChanged(selectedItem)

        Assert.assertEquals((lastStatus as Status.Success).data, expectedList)
    }

    @Test
    fun `onSelectedItemValueChanged() should update the currency values with the given multiplier`() {
        val baseRateItem = RateItemUIModel(currency = "EUR", value = BigDecimal("1"))

        whenever(runBlocking { useCase.execute(any()) }).thenReturn(mockResponse())
        viewModel.fetchData() // Now we have the initial response

        val expectedList = listOf(
            RateItemUIModel(currency = "EUR", value = BigDecimal("2")),
            RateItemUIModel(currency = "AUD", value = BigDecimal("3.22")),
            RateItemUIModel(currency = "BRL", value = BigDecimal("8.46")),
            RateItemUIModel(currency = "USD", value = BigDecimal("2.26"))
        )

        val newValue = "2" // Each currency value in the list will be multiplied with this value
        viewModel.onSelectedItemValueChanged(baseRateItem, newValue)

        Assert.assertEquals((lastStatus as Status.Success).data, expectedList)
    }

    private fun mockResponse(): Result<RatesResponse?> {
        val values = mapOf(
            "USD" to BigDecimal("1.13"),
            "BRL" to BigDecimal("4.23"),
            "AUD" to BigDecimal("1.61")
        )
        return Result.Success(RatesResponse(baseCurrency = "EUR", rates = values))
    }
}