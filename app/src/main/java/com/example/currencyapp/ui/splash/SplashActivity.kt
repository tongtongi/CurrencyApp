package com.example.currencyapp.ui.splash

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.currencyapp.R
import com.example.currencyapp.ui.ratelist.RateListActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val ANIMATION_DURATION = 3000L // means 3 seconds

class SplashActivity : AppCompatActivity() {

    private val viewModel: SplashViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        observeViewModel()
    }

    override fun onResume() {
        super.onResume()
        Handler().postDelayed({ viewModel.onSplashAnimationEnd() }, ANIMATION_DURATION)
    }

    private fun observeViewModel() {
        viewModel.getNavigateToNextScreen().observe(this, Observer {
            startActivity(RateListActivity.newIntent(this))
            finish()
        })
    }
}