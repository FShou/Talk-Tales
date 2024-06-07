package com.capstone.talktales.ui.storydetail

import android.os.Bundle
import android.view.View
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
import com.capstone.talktales.ui.utils.startShimmer

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

        storyId = intent.getStringExtra(EXTRA_STORY_ID).toString()

        getStoryDetail()


        viewModel.pageTitle.observe(this) {
            binding.pageTitle.text = it
        }

    }

    private fun getStoryDetail(){
        viewModel.getStoryDetail(storyId).observe(this) {
            handleStoryDetail(it)
        }
    }

    private fun handleStoryDetail(result: ResponseResult<DetailStoryResponse>) {
        when (result) {
            is ResponseResult.Error -> {
                showLoading(false)
                showError(true, result.msg)
            }

            is ResponseResult.Loading -> {
                showError(false)
                showLoading(true)
            }

            is ResponseResult.Success -> {
                showLoading(false)
                showGlossary(result.data.story)
                showTitleAndThumbnail(result.data.story)

            }
        }
    }

    private fun showError(isError: Boolean, msg: String? = null) {
        if (isError)
            with(binding) {
                errorLayout.visibility = View.VISIBLE
                tvError.text = msg
                btnRetry.setOnClickListener { getStoryDetail() }
            }
        else{
            binding.errorLayout.visibility = View.GONE
            binding.tvError.text = getString(R.string.error_message)
        }

    }


    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            with(binding) {
                storyBannerSkeleton.startShimmer()
                titleSkeleton.startShimmer()
                countrySkeleton.startShimmer()
                provinceSkeleton.startShimmer()
                contentSkeleton.startShimmer()
            }
        } else {
            with(binding) {
                storyBannerSkeleton.clearAnimation()
                titleSkeleton.clearAnimation()
                countrySkeleton.clearAnimation()
                provinceSkeleton.clearAnimation()
                contentSkeleton.clearAnimation()
                storyBannerSkeleton.visibility = View.GONE
                titleSkeleton.visibility = View.GONE
                countrySkeleton.visibility = View.GONE
                provinceSkeleton.visibility = View.GONE
                contentSkeleton.visibility = View.GONE
            }
        }

    }

    private fun showTitleAndThumbnail(story: Story) {
        with(binding) {
            storyBanner.load(story.imgUrl)
            title.visibility = View.VISIBLE
            country.visibility = View.VISIBLE
            province.visibility = View.VISIBLE
            content.visibility = View.VISIBLE
            title.text = story.title
            province.text = story.city
        }
    }

    private fun showGlossary(story: Story) {
        val glossaryFragment = GlossaryFragment.newInstance(story)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.content, glossaryFragment)
            .commit()
    }

    companion object {
        const val EXTRA_STORY_ID = "story-id"
    }
}