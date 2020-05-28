package com.example.currencyapp.ui.ratelist.di

import com.example.currencyapp.ui.ratelist.RateListViewModel
import com.example.currencyapp.ui.ratelist.usecase.LoadRatesUseCase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val rateListModule = module {
    factory { LoadRatesUseCase(get()) }
    viewModel { RateListViewModel(get()) }
}