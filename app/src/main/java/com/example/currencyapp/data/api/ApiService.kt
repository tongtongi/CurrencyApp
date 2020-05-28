package com.example.currencyapp.data.api

import com.example.currencyapp.data.model.RatesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("latest")
    suspend fun getCurrencies(@Query("base") baseCurrency: String): Response<RatesResponse>
}
