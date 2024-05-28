package com.capstone.talktales.ui.home

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2

fun ViewPager2.setCurrentItemWithCustomDuration(item: Int, duration: Int) {
    val recyclerView = getChildAt(0) as RecyclerView
    val layoutManager = recyclerView.layoutManager ?: return

    val smoothScroller = CustomLinearSmoothScroller(recyclerView.context, duration)
    smoothScroller.targetPosition = item
    layoutManager.startSmoothScroll(smoothScroller)
}

class CustomLinearSmoothScroller(context: Context?, private val duration: Int) : LinearSmoothScroller(context) {
    override fun calculateTimeForScrolling(dx: Int): Int = duration
}
