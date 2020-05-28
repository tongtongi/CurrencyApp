package com.example.currencyapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.currencyapp.commons.Event
import com.example.currencyapp.ui.splash.SplashViewModel
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SplashViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var viewModel: SplashViewModel

    @Before
    fun setUp() {
        viewModel = SplashViewModel()
    }

    @Test
    fun `onSplashAnimationEnd() should navigate to rate list screen when splash animation ends`() {
        viewModel.onSplashAnimationEnd()

        assertEquals(
            Event(true).peekContent(),
            viewModel.getNavigateToNextScreen().value?.peekContent()
        )
    }
}