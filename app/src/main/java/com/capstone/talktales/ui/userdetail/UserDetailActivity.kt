package com.capstone.talktales.ui.userdetail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import coil.load
import com.capstone.talktales.R
import com.capstone.talktales.databinding.ActivityUserDetailBinding
import com.capstone.talktales.factory.UserViewModelFactory
import com.capstone.talktales.ui.login.LoginActivity
import com.capstone.talktales.ui.utils.BorderedCircleCropTransformation
import com.capstone.talktales.ui.utils.dpToPx

class UserDetailActivity : AppCompatActivity() {


    private lateinit var binding: ActivityUserDetailBinding
    private val viewModel by viewModels<UserDetailViewModel> {
        UserViewModelFactory.getInstance(this)
    }

    private val imgUri: Uri =
        Uri.parse("android.resource://com.capstone.talktales/drawable/account")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        loadProfilePicture(imgUri)

        viewModel.getLoginUser().observe(this) {
            with(binding) {
                tvUsername.text = it.name
                tvUserEmail.text = it.email
            }
        }

        binding.btnLogout.setOnClickListener {
            viewModel.logout()
            startActivity(
                Intent(this, LoginActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
            )
        }


        binding.btnBack.setOnClickListener { finish() }
    }

    private fun loadProfilePicture(imgUri: Uri) {
        binding.profileImg.load(imgUri) {
            transformations(BorderedCircleCropTransformation(
                dpToPx(this@UserDetailActivity, 4),
                resources.getColor(R.color.orange)
            ))
        }
    }
}