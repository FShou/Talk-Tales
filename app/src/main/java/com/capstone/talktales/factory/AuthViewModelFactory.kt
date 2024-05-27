package com.capstone.talktales.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.talktales.data.repo.AuthRepository
import com.capstone.talktales.di.Injection
import com.capstone.talktales.ui.login.LoginViewModel
import com.capstone.talktales.ui.splash.SplashViewModel

class AuthViewModelFactory private constructor(
private val authRepository: AuthRepository
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(SplashViewModel::class.java) -> return SplashViewModel(
                authRepository
            ) as T

            modelClass.isAssignableFrom(LoginViewModel::class.java) -> return LoginViewModel(
                authRepository
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: AuthViewModelFactory? = null
        fun getInstance(context: Context): AuthViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: AuthViewModelFactory(Injection.provideAuthRepository(context))
            }.also { instance = it }
    }
}