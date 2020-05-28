package com.example.currencyapp.ui.ratelist.usecase

import com.example.currencyapp.data.repository.MainRepository
import com.example.currencyapp.commons.ErrorModel
import com.example.currencyapp.commons.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoadRatesUseCase(private val mainRepository: MainRepository) {

    suspend fun execute(currency: String) =
        withContext(Dispatchers.IO) {
            val response = mainRepository.getCurrencies(currency)
            if (response is Result.Success) {
                Result.Success(response.value.body())
            } else {
                Result.Failure(ErrorModel.DEFAULT)
            }
        }
}