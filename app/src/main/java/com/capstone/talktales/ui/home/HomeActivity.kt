package com.capstone.talktales.ui.home

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.Menu
import android.view.View
import android.widget.ImageView
import android.widget.Toast
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
import com.capstone.talktales.data.model.StoryItem
import com.capstone.talktales.data.model.Tutorial
import com.capstone.talktales.databinding.ActivityHomeBinding
import com.capstone.talktales.factory.UserViewModelFactory
import com.capstone.talktales.ui.tutorial.TutorialActivity
import com.capstone.talktales.ui.userdetail.UserDetailActivity
import com.capstone.talktales.ui.utils.BorderedCircleCropTransformation
import com.capstone.talktales.ui.utils.applyMarginAndScalePageTransformer
import com.capstone.talktales.ui.utils.dpToPx
import com.capstone.talktales.ui.utils.onPageSelected
import com.capstone.talktales.ui.utils.setCurrentItemWithSmoothScroll
import com.capstone.talktales.ui.utils.startShimmer
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
        setSupportActionBar(binding.toolBar)

        viewModel
            .getStories()
            .observe(this) { handleStoriesResponse(it) }

        showTutorial()


    }

    private fun showTutorial() {
        with(binding.tutorialBanner) {
            load(Tutorial.BANNER) {
                transformations(
                    RoundedCornersTransformation(16f)
                )
            }
            setOnClickListener {
                startActivity(
                    Intent(
                        this@HomeActivity,
                        TutorialActivity::class.java
                    )
                )
            }
        }
    }

    private fun handleStoriesResponse(responseResult: ResponseResult<StoriesResponse>) {
        when (responseResult) {
            is ResponseResult.Error -> handleStoryError(responseResult.msg)
            is ResponseResult.Loading -> {
                showStorySkeleton(true)
                showCarouselSkeleton(true)
            }

            is ResponseResult.Success -> {
                val data = responseResult.data.listStoryItem
                val carouselContent: ArrayList<Any> =
                    arrayListOf(*data.toTypedArray(), Tutorial.BANNER)
                showStorySkeleton(false)
                showCarouselSkeleton(false)
                showStories(data)
                showCarousel(carouselContent)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.home, menu)

        val menuItem = menu.findItem(R.id.profilePic)

        profilePicture = menuItem.actionView as ImageView
        profilePicture.load(profilePic) {
            transformations(
                BorderedCircleCropTransformation(
                    dpToPx(this@HomeActivity, 2),
                    resources.getColor(R.color.orange)
                )
            )
        }

        profilePicture.setOnClickListener {
            startActivity(Intent(this, UserDetailActivity::class.java))
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


    private fun showCarouselSkeleton(isShow: Boolean) {
        if (isShow) {
            binding.carouselSkeleton.startShimmer()
            binding.carouselSkeleton.visibility = View.VISIBLE
        } else {
            binding.carouselSkeleton.clearAnimation()
            binding.carouselSkeleton.visibility = View.GONE
        }
    }

    private fun showStorySkeleton(isShow: Boolean) {
        if (isShow) {
            binding.storySkeleton.startShimmer()
            binding.storySkeleton.visibility = View.VISIBLE

        } else {
            binding.storySkeleton.clearAnimation()
            binding.storySkeleton.visibility = View.GONE
        }

    }

    private fun showCarousel(carouselContent: List<Any>) {
        with(binding.carousel) {
            visibility = View.VISIBLE
            adapter = CarouselAdapter(carouselContent)
            onPageSelected { restartPageChangeCoroutine() }
            applyMarginAndScalePageTransformer()
            TabLayoutMediator(binding.tabLayout, binding.carousel) { _, _ -> }.attach()
        }
    }

    private fun showStories(listStoryItem: List<StoryItem>) {
        with(binding.rvStory) {
            visibility = View.VISIBLE
            layoutManager = LinearLayoutManager(this@HomeActivity)
            adapter = StoryAdapter(listStoryItem)
        }
    }


    private fun handleStoryError(msg: String) {
        Toast.makeText(this@HomeActivity, msg, Toast.LENGTH_LONG).show()
    }

    companion object {
        private const val SLIDE_DELAY = 2000L
        private val profilePic = "android.resource://com.capstone.talktales/drawable/account"

    }
}