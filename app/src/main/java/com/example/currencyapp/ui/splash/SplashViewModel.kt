package com.example.currencyapp.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.currencyapp.commons.Event

class SplashViewModel : ViewModel() {

    private val navigateToNextScreen = MutableLiveData<Event<Boolean>>()
    fun getNavigateToNextScreen(): LiveData<Event<Boolean>> = navigateToNextScreen

    fun onSplashAnimationEnd() {
        navigateToNextScreen.value = Event(true)
    }
}