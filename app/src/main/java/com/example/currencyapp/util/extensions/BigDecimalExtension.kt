package com.example.currencyapp.util.extensions

import java.math.BigDecimal

fun BigDecimal.formatToTwoDecimal(digits: Int) =
    this.setScale(digits, BigDecimal.ROUND_DOWN).toString()