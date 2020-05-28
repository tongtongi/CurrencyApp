package com.example.currencyapp

import com.example.currencyapp.ui.ratelist.adapter.RateItemUIModel
import com.example.currencyapp.util.extensions.calculateCurrencyValues
import com.example.currencyapp.util.extensions.updateFirstElement
import org.junit.Assert
import org.junit.Test
import java.math.BigDecimal

class ListExtensionTest {

    @Test
    fun `calculateCurrencyValues() should multiply each value (except the first one) with the given multiplier`() {
        val multiplier = BigDecimal("2")

        val list = listOf(
            RateItemUIModel(currency = "EUR", value = BigDecimal("1")),
            RateItemUIModel(currency = "AUD", value = BigDecimal("1.61")),
            RateItemUIModel(currency = "BRL", value = BigDecimal("4.23")),
            RateItemUIModel(currency = "USD", value = BigDecimal("1.13"))
        )

        val expectedList = listOf(
            RateItemUIModel(currency = "EUR", value = BigDecimal("1")),
            RateItemUIModel(currency = "AUD", value = BigDecimal("3.22")),
            RateItemUIModel(currency = "BRL", value = BigDecimal("8.46")),
            RateItemUIModel(currency = "USD", value = BigDecimal("2.26"))
        )

        list.calculateCurrencyValues(multiplier)

        Assert.assertEquals(list, expectedList)
    }

    @Test
    fun `updateFirstElement() should change the first element of the list with the given element`() {
        val newBaseItem = RateItemUIModel(currency = "EUR", value = BigDecimal("3.49"))

        val list = listOf(
            RateItemUIModel(currency = "EUR", value = BigDecimal("1")),
            RateItemUIModel(currency = "AUD", value = BigDecimal("1.61")),
            RateItemUIModel(currency = "BRL", value = BigDecimal("4.23")),
            RateItemUIModel(currency = "USD", value = BigDecimal("1.13"))
        )

        val updatedList = list.updateFirstElement(newBaseItem)

        val expectedList = listOf(
            RateItemUIModel(currency = "EUR", value = BigDecimal("3.49")),
            RateItemUIModel(currency = "AUD", value = BigDecimal("1.61")),
            RateItemUIModel(currency = "BRL", value = BigDecimal("4.23")),
            RateItemUIModel(currency = "USD", value = BigDecimal("1.13"))
        )

        Assert.assertEquals(updatedList, expectedList)
    }
}