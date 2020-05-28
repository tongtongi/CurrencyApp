package com.example.currencyapp.data.repository

import com.example.currencyapp.data.api.ApiHelper
import com.example.currencyapp.data.api.RetrofitBuilder
import com.example.currencyapp.data.model.RatesResponse
import com.example.currencyapp.commons.ErrorModel
import com.example.currencyapp.commons.Result
import org.koin.dsl.module
import retrofit2.Response

val repoModule = module {
    single { MainRepository(ApiHelper(RetrofitBuilder.apiService)) }
}

class MainRepository(private val apiHelper: ApiHelper) {
    suspend fun getCurrencies(baseCurrency: String): Result<Response<RatesResponse>> {
        return try {
            val response = apiHelper.getCurrencies(baseCurrency)
            Result.Success(response)
        } catch (e: Exception) {
            Result.Failure(ErrorModel.DEFAULT)
        }
    }
}