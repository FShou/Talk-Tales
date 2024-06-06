package com.capstone.talktales.ui.storydetail

import android.nfc.NfcAdapter.EXTRA_ID
import android.os.Build
import android.os.Bundle
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import coil.load
import com.capstone.talktales.R
import com.capstone.talktales.data.model.Story
import com.capstone.talktales.data.remote.response.DetailStoryResponse
import com.capstone.talktales.data.remote.response.ResponseResult
import com.capstone.talktales.databinding.ActivityStoryDetailBinding
import com.capstone.talktales.factory.UserViewModelFactory

class StoryDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStoryDetailBinding
    private val viewModel by viewModels<StoryDetailViewModel> {
        UserViewModelFactory.getInstance(this)
    }
    private lateinit var storyId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityStoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

//        binding.storyBanner.load(story.imgUrl)
//        binding.title.text = story.title
//        binding.province.text = story.city

        storyId = intent.getStringExtra(EXTRA_STORY_ID).toString()

        viewModel.getStoryDetail(storyId).observe(this) {
            handleStoryDetail(it)
        }


//        binding.toolBar.setNavigationOnClickListener {
//            // Todo: Show Exit Alert
//            finish()
//        }

        this.onBackPressedDispatcher.addCallback {
            // Todo: Show Exit Alert

            finish()

        }
    }

    private fun handleStoryDetail(result: ResponseResult<DetailStoryResponse>) {
        when (result) {
            is ResponseResult.Error -> {
                // Todo: Handle Error
            }

            is ResponseResult.Loading -> {
                // Todo: Handle Loading
            }
            is ResponseResult.Success -> {
                handleStoryDetailSuccess(result.data)
            }
        }
    }

    private fun handleStoryDetailSuccess(data: DetailStoryResponse) {
       // todo: hide loading
        // Todo: hide error


    }

    companion object {
        const val EXTRA_STORY_ID = "story-id"
    }
}