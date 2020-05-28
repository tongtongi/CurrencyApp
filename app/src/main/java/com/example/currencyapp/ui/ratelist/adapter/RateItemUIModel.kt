package com.example.currencyapp.ui.ratelist.adapter

import java.math.BigDecimal

data class RateItemUIModel(
    val currency: String,
    var value: BigDecimal
) {

    internal constructor(model: Map.Entry<String, BigDecimal>) : this(
        currency = model.key,
        value = model.value
    )
}