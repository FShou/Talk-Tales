package com.capstone.talktales.ui.home

import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.RoundedCornersTransformation
import com.capstone.talktales.R
import com.capstone.talktales.data.remote.response.ResponseResult
import com.capstone.talktales.data.remote.response.StoriesResponse
import com.capstone.talktales.data.remote.response.Story
import com.capstone.talktales.databinding.ActivityHomeBinding
import com.capstone.talktales.factory.UserViewModelFactory
import com.capstone.talktales.ui.utils.BorderedCircleCropTransformation
import com.capstone.talktales.ui.utils.applyMarginAndScalePageTransformer
import com.capstone.talktales.ui.utils.dpToPx
import com.capstone.talktales.ui.utils.onPageSelected
import com.capstone.talktales.ui.utils.setCurrentItemWithSmoothScroll
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    private val viewModel by viewModels<HomeViewModel> {
        UserViewModelFactory.getInstance(this)
    }

    private var pageChangeJob: Job? = null
    private lateinit var profilePicture: ImageView

    val imgUri =
        Uri.parse("android.resource://com.capstone.talktales/drawable/banner_timun") // Todo: Get from api


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
        setSupportActionBar(binding.toolBar)

        viewModel
            .getStories()
            .observe(this) { handleStoriesResponse(it) }

        binding.tutorialBanner
            .load(imgUri) {
            // todo: get proper img
            transformations(
                RoundedCornersTransformation(16f)
            )
        }

    }

    private fun handleStoriesResponse(responseResult: ResponseResult<StoriesResponse>) {
        when (responseResult) {
            is ResponseResult.Error -> handleStoryError(responseResult.msg)
            is ResponseResult.Loading -> handleStoryLoading()
            is ResponseResult.Success -> {
                handleStorySuccess(responseResult.data)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.home, menu)

        val menuItem = menu.findItem(R.id.profilePic)

        profilePicture = menuItem.actionView as ImageView
        profilePicture.load(imgUri) {
            transformations(
                BorderedCircleCropTransformation(
                    dpToPx(this@HomeActivity, 2),
                    resources.getColor(R.color.orange)
                )
            )
        }

        profilePicture.setOnClickListener {
            // TODO: Intent to detail User
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onDestroy() {
        super.onDestroy()
        pageChangeJob?.cancel()
        pageChangeJob = null
    }

    override fun onPause() {
        pageChangeJob?.cancel()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        if (binding.carousel.adapter != null)
            restartPageChangeCoroutine()
    }

    private fun restartPageChangeCoroutine() {
        pageChangeJob?.cancel()
        pageChangeJob = CoroutineScope(Dispatchers.Main).launch {
            delay(SLIDE_DELAY)
            slideToNextPage()
        }
    }

    private fun slideToNextPage() {
        with(binding.carousel) {
            val nextItem = (currentItem + 1) % adapter!!.itemCount
            setCurrentItemWithSmoothScroll(nextItem)
        }
    }

    private fun setupView() {
        enableEdgeToEdge()

        binding = ActivityHomeBinding.inflate(layoutInflater)

        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }


    private fun handleStorySuccess(data: StoriesResponse) {
        // todo: hide loading
        // Todo: hide error

        showStories(data.listStory)
        // Todo: get from real API Endpoint
        val imgList = data.listStory
            .map { Uri.parse(it.imgUrl) }

        showCarousel(imgList)
    }

    private fun showCarousel(imgList: List<Uri>) {
        with(binding.carousel) {
            adapter = CarouselAdapter(imgList)
            onPageSelected { restartPageChangeCoroutine() }
            applyMarginAndScalePageTransformer()
            TabLayoutMediator(binding.tabLayout, binding.carousel) { _, _ -> }.attach()
        }
    }

    private fun showStories(listStory: List<Story>) {
        with(binding.rvStory) {
            layoutManager = LinearLayoutManager(this@HomeActivity)
            adapter = StoryAdapter(listStory)
        }
    }

    private fun handleStoryLoading() {
//        TODO("Not yet implemented")
    }

    private fun handleStoryError(msg: String) {
//        TODO("Not yet implemented")
    }

    companion object {
        private const val SLIDE_DELAY = 2000L
    }
}