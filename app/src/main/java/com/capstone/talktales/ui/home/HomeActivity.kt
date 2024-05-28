package com.capstone.talktales.ui.home

import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.capstone.talktales.R
import com.capstone.talktales.data.remote.response.ResponseResult
import com.capstone.talktales.data.remote.response.StoriesResponse
import com.capstone.talktales.databinding.ActivityHomeBinding
import com.capstone.talktales.factory.UserViewModelFactory
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


    // Todo: Get Real Uri
    val imgUri = Uri.parse("android.resource://com.capstone.talktales/drawable/timun_mas")
    val carouselList = listOf(
        imgUri,
        imgUri,
        imgUri
    )

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

        // TODO: move this after get API
        with(binding.carousel) {
            adapter = CarouselAdapter(carouselList)
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    restartPageChangeCoroutine()
                }
            })
        }
        TabLayoutMediator(binding.tabLayout, binding.carousel) { _, _ -> }.attach()

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
            setCurrentItemWithCustomDuration(nextItem, SLIDE_DURATION)
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

    companion object {
        private const val SLIDE_DELAY = 3000L
        private const val SLIDE_DURATION = 400
    }
}