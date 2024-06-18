package com.capstone.talktales.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.capstone.talktales.R
import com.capstone.talktales.data.model.User
import com.capstone.talktales.factory.AuthViewModelFactory
import com.capstone.talktales.ui.home.HomeActivity
import com.capstone.talktales.ui.login.LoginActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private val viewModel: SplashViewModel by viewModels {
        AuthViewModelFactory.getInstance(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        setupView()
        supportActionBar?.hide()

        Handler(Looper.getMainLooper()).postDelayed({
            viewModel.getLoginUser().observe(this) { user ->
                if ((user as User).token.isNotBlank()) {
                    val intent = Intent(this, HomeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()

                } else {
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
            }

        }, DELAY_TIME_IN_MILLISECONDS)

    }

    companion object {
        // Konstanta dengan nama yang deskriptif
        const val DELAY_TIME_IN_MILLISECONDS = 3000L
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }
}