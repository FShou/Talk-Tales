package com.capstone.talktales.ui.login

import android.content.Intent
import android.graphics.drawable.Animatable2
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.capstone.talktales.data.model.User
import com.capstone.talktales.data.remote.response.LoginResponse
import com.capstone.talktales.data.remote.response.ResponseResult
import com.capstone.talktales.databinding.ActivityLoginBinding
import com.capstone.talktales.factory.AuthViewModelFactory
import com.capstone.talktales.ui.home.HomeActivity
import com.capstone.talktales.ui.register.RegisterActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val viewModel: LoginViewModel by viewModels {
        AuthViewModelFactory.getInstance(this)
    }

    private val validationWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            setLoginButtonEnabled()
        }
        override fun afterTextChanged(s: Editable) {}
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        setupView()
        setLoginButtonEnabled()
        with(binding) {
            register.setOnClickListener {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
                finish()
            }

            btnLogin.setOnClickListener {
                val email = binding.edLoginEmail.text.toString()
                val password = binding.edLoginPassword.text.toString()
                viewModel.login(email, password).observe(this@LoginActivity) {
                    handleResult(it)
                }
            }

            edLoginEmail.addTextChangedListener(validationWatcher)
            binding.edLoginPassword.addTextChangedListener(validationWatcher)
        }

    }


    private fun handleResult(result: ResponseResult<LoginResponse>) {
        when (result) {
            is ResponseResult.Loading -> {
                enableLoginForm(false)
                with(binding) {
                    progressBar.visibility = View.VISIBLE
                    edLoginEmail.error = null
                    edLoginPassword.error = null
                }
            }

            is ResponseResult.Error -> {
                enableLoginForm(true)
                binding.progressBar.visibility = View.GONE
                handleErrorMsg(result.msg)
            }

            is ResponseResult.Success -> {
                val user = User(
                    name = result.data.data.userItem.name,
                    email = result.data.data.userItem.email,
                    token = result.data.data.token
                )
                viewModel.saveLoginUser(user)
                hideLoginForm()
                showSuccessAnimation()
            }
        }
    }

    private fun handleErrorMsg(msg: String) {
        when {
            msg.contains("password") -> {
                binding.edLoginPassword.apply {
                    error = msg
                    requestFocus()
                    setSelection(text?.length ?: 0)
                }
            }

            msg.contains("email") || msg.contains("User") -> {
                binding.edLoginEmail.apply {
                    error = msg
                    requestFocus()
                    setSelection(text?.length ?: 0)
                }
            }
            msg.isNotEmpty() -> {
                showToast(msg)
            }
        }
        setLoginButtonEnabled()
    }


    private fun showSuccessAnimation() {
        with(binding) {
            checkAnim.visibility = View.VISIBLE
            loginSucces.visibility = View.VISIBLE

            val animatedCheck = checkAnim.drawable as AnimatedVectorDrawable
            animatedCheck.registerAnimationCallback(object : Animatable2.AnimationCallback() {
                override fun onAnimationEnd(drawable: Drawable?) {
                    super.onAnimationEnd(drawable)
                    startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                    finish()
                }
            })
            animatedCheck.start()
        }

    }

    private fun hideLoginForm() {
        with(binding) {
            loginform.visibility= View.GONE
        }
    }

    private fun enableLoginForm(isEnable: Boolean) {
        with(binding) {
            edLoginEmail.isEnabled = isEnable
            edLoginPassword.isEnabled = isEnable
            btnLogin.isEnabled = isEnable
            register.isEnabled = isEnable
        }
    }


    private fun showToast(msg: String) =
        Toast.makeText(this@LoginActivity, msg, Toast.LENGTH_SHORT).show()

    private fun setLoginButtonEnabled() {
        with(binding) {
            val result =
                (!edLoginEmail.text.isNullOrEmpty() && edLoginEmail.error.isNullOrEmpty())
                        && (!edLoginPassword.text.isNullOrEmpty() && edLoginPassword.error.isNullOrEmpty())

            btnLogin.isEnabled = result
        }

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