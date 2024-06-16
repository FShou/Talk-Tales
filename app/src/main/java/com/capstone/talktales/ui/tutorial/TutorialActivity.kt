package com.capstone.talktales.ui.tutorial

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.capstone.talktales.data.model.Tutorial
import com.capstone.talktales.databinding.ActivityTutorialBinding
import com.capstone.talktales.ui.utils.applyMarginAndScalePageTransformer
import com.capstone.talktales.ui.utils.setCurrentItemWithSmoothScroll
import com.google.android.material.tabs.TabLayoutMediator

class TutorialActivity : AppCompatActivity() {

    private val binding by lazy { ActivityTutorialBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        showTutorial()
        with(binding) {
            btnBack.setOnClickListener { finish() }
            btnNext.setOnClickListener { slideToNextPage() }
            btnPrev.setOnClickListener { slideToPrevPage() }
        }
    }

    private fun showTutorial() {
        with(binding.viewPager) {
            adapter = TutorialPageAdapter(Tutorial.tutorialPage)
            applyMarginAndScalePageTransformer()
            TabLayoutMediator(binding.tabLayout, binding.viewPager) { _, _ -> }.attach()
        }
    }

    private fun slideToNextPage() {
        with(binding.viewPager) {
            val nextItem = (currentItem + 1) % adapter!!.itemCount
            setCurrentItemWithSmoothScroll(nextItem, 300)
        }
    }

    private fun slideToPrevPage() {
        with(binding.viewPager) {
            val prevItem = if (currentItem - 1 < 0) adapter!!.itemCount - 1 else currentItem - 1
            setCurrentItemWithSmoothScroll(prevItem, 300)
        }
    }

}