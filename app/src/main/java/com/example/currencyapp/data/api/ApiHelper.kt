package com.example.currencyapp.data.api

class ApiHelper(private val apiService: ApiService) {
    suspend fun getCurrencies(baseCurrency: String) = apiService.getCurrencies(baseCurrency)
}