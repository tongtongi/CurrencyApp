package com.example.currencyapp.core

import android.app.Application
import com.example.currencyapp.data.repository.repoModule
import com.example.currencyapp.ui.ratelist.di.rateListModule
import com.example.currencyapp.ui.splash.di.splashModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class CurrencyApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@CurrencyApp)
            modules(
                listOf(
                    repoModule,
                    rateListModule,
                    splashModule
                )
            )
        }
    }
}