package com.capstone.talktales.ui.register

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
import com.capstone.talktales.data.remote.response.RegisterResponse
import com.capstone.talktales.data.remote.response.ResponseResult
import com.capstone.talktales.databinding.ActivityRegisterBinding
import com.capstone.talktales.factory.AuthViewModelFactory
import com.capstone.talktales.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: RegisterViewModel by viewModels {
        AuthViewModelFactory.getInstance(this)
    }
    private val validationWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            setRegisterButtonEnabled()
        }
        override fun afterTextChanged(s: Editable?) {}
    }

    private val passwordWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (!isRepeatPasswordSame()) {
                binding.inputConfirmPassword.setError("Password is unmatched", null)
            }
            setRegisterButtonEnabled()
        }

        override fun afterTextChanged(s: Editable?) {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
        setupView()

        with(binding) {

            btnRegister.setOnClickListener {
                val name = binding.edRegisterName.text.toString()
                val email = binding.edRegisterEmail.text.toString()
                val password = binding.edRegisterPassword.text.toString()

                viewModel.register(name, email, password).observe(this@RegisterActivity) {
                    handleResult(it)
                }

            }

            Login.setOnClickListener {
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                finish()
            }

            edRegisterName.addTextChangedListener(validationWatcher)
            edRegisterEmail.addTextChangedListener(validationWatcher)
            edRegisterPassword.addTextChangedListener(passwordWatcher)
            inputConfirmPassword.addTextChangedListener(passwordWatcher)
        }

    }

    private fun handleResult(result: ResponseResult<RegisterResponse>) {
        when (result) {
            is ResponseResult.Loading -> {
                enableRegisterForm(false)
                binding.progressBar.visibility = View.VISIBLE
            }

            is ResponseResult.Error -> {
                enableRegisterForm(true)
                binding.progressBar.visibility = View.GONE
                handleErrorMsg(result.msg)
            }

            is ResponseResult.Success -> {
                hideRegisterForm()
                showSuccessAnimation()
            }
        }
    }

    private fun handleErrorMsg(msg: String) {
        when {
            msg.contains("Email") -> {
                binding.edRegisterEmail.apply {
                    error = msg
                    requestFocus()
                    setSelection(text?.length ?: 0)
                }
            }
            msg.isNotEmpty() -> {
                showToast(msg)
            }
        }
        setRegisterButtonEnabled()
    }

    private fun showSuccessAnimation() {
        val animatedCheck = binding.checkAnim.drawable as AnimatedVectorDrawable
        animatedCheck.registerAnimationCallback(object : Animatable2.AnimationCallback() {
            override fun onAnimationEnd(drawable: Drawable?) {
                super.onAnimationEnd(drawable)
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                finish()
            }
        })
        with(binding) {
            checkAnim.visibility = View.VISIBLE
            registerSuccess.visibility = View.VISIBLE
        }
        animatedCheck.start()

    }

    private fun hideRegisterForm() {
        with(binding) {
            registerform.visibility = View.GONE
        }
    }

    private fun enableRegisterForm(isEnabled: Boolean) {
        with(binding) {
            edRegisterName.isEnabled = isEnabled
            edRegisterEmail.isEnabled = isEnabled
            edRegisterPassword.isEnabled = isEnabled
            inputConfirmPassword.isEnabled = isEnabled
            Login.isEnabled = isEnabled
            btnRegister.isEnabled = isEnabled
        }
    }

    private fun showToast(msg: String) =
        Toast.makeText(this@RegisterActivity, msg, Toast.LENGTH_LONG).show()


    private fun isRepeatPasswordSame() =
        binding.edRegisterPassword.text.toString() == binding.inputConfirmPassword.text.toString()

    private fun setRegisterButtonEnabled() {
        with(binding) {
            val result =
                (!edRegisterName.text.isNullOrEmpty() && edRegisterName.error.isNullOrEmpty()) &&
                        (!edRegisterEmail.text.isNullOrEmpty() && edRegisterEmail.error.isNullOrEmpty()) &&
                        (!edRegisterPassword.text.isNullOrEmpty() && edRegisterPassword.error.isNullOrEmpty()) &&
                        (!inputConfirmPassword.text.isNullOrEmpty() && inputConfirmPassword.error.isNullOrEmpty())

            btnRegister.isEnabled = result
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