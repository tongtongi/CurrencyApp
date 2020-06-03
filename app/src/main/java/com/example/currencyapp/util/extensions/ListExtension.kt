package com.example.currencyapp.util.extensions

import com.example.currencyapp.ui.ratelist.adapter.RateItemUIModel
import java.math.BigDecimal

/**
 * Will multiply the value of each currency item in the given list with the specified multiplier.
 * (Except the first element)
 *
 * For example,
 * listOf(RateItemUIModel("EUR", 2), RateItemUIModel("USD", 3)) will return
 * listOf(RateItemUIModel("EUR", 2), RateItemUIModel("USD", 6)) when multiplier is 2.
 */
fun List<RateItemUIModel>.calculateCurrencyValues(multiplier: BigDecimal) {
    this.forEachIndexed { index, it ->
        if (index != 0) it.value = it.value.multiply(multiplier)
    }
}

/**
 * Will replace the first element of the given list with the given parameter
 *
 * For example,
 * listOf(RateItemUIModel("EUR", 2), RateItemUIModel("USD", 3)) will return
 * listOf(RateItemUIModel("EUR", 3.49), RateItemUIModel("USD", 3)) when the given
 * parameter is RateItemUIModel("EUR", 3.49).
 */
fun List<RateItemUIModel>.updateFirstElement(baseRateItem: RateItemUIModel): List<RateItemUIModel> {
    val temp = this.toMutableList()
    temp.removeAt(0)
    temp.add(0, baseRateItem)
    return temp.toList()
}

fun List<RateItemUIModel>.deepCopy() = this.map { it.copy() }