package com.capstone.talktales.ui.home

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.capstone.talktales.R
import com.capstone.talktales.data.remote.response.ResponseResult
import com.capstone.talktales.data.remote.response.StoriesResponse
import com.capstone.talktales.factory.UserViewModelFactory

class HomeActivity : AppCompatActivity() {

    private val viewModel by viewModels<HomeViewModel> {
        UserViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()

        viewModel.getStories().observe(this) {
            when (it) {
                is ResponseResult.Error -> hanzdleStoryError(it.msg)
                is ResponseResult.Loading -> handleStoryLoading()
                is ResponseResult.Success -> handleStorySuccess(it.data)
            }
        }
    }

    private fun setupView() {
        enableEdgeToEdge()

        setContentView(R.layout.activity_home)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

//        binding these nuts

    }

    private fun handleStorySuccess(data: StoriesResponse) {
//        TODO("Not yet implemented")
    }

    private fun handleStoryLoading() {
//        TODO("Not yet implemented")
    }

    private fun hanzdleStoryError(msg: String) {
//        TODO("Not yet implemented")
    }
}