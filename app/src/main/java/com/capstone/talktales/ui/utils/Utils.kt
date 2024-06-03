package com.capstone.talktales.ui.utils


import android.content.Context
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationSet
import android.view.animation.LinearInterpolator
import android.view.animation.OvershootInterpolator
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs

fun View.startShimmer() {
    val alphaAnimation = AlphaAnimation(0.3f, 1.0f)
    alphaAnimation.duration = 1000
    alphaAnimation.interpolator = LinearInterpolator()
    alphaAnimation.repeatCount = AnimationSet.INFINITE
    alphaAnimation.repeatMode = AnimationSet.REVERSE

    val animationSet = AnimationSet(true)
    animationSet.addAnimation(alphaAnimation)

    startAnimation(animationSet)
}

inline fun ViewPager2.onPageSelected(crossinline action: (Int) -> Unit) {
    this.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            action(position)
        }
    })
}

fun ViewPager2.setCurrentItemWithSmoothScroll(item: Int, duration: Int = 400) {
    val recyclerView = getChildAt(0) as RecyclerView
    val layoutManager = recyclerView.layoutManager ?: return

    val smoothScroller = object : LinearSmoothScroller(recyclerView.context) {
        override fun calculateTimeForScrolling(dx: Int): Int = duration
    }

    smoothScroller.targetPosition = item
    layoutManager.startSmoothScroll(smoothScroller)
}

fun createMarginAndScalePageTransformer(
    marginPx: Int,
    minScale: Float,
    maxScale: Float
): CompositePageTransformer =
    CompositePageTransformer().apply {
        addTransformer(MarginPageTransformer(marginPx))
        addTransformer { page, position ->
            val scaleFactor = 1 - abs(position)
            page.scaleY = minScale + scaleFactor * (maxScale - minScale)
        }
    }


fun ViewPager2.applyMarginAndScalePageTransformer(
    marginPx: Int = 24,
    minScale: Float = 0.85f,
    maxScale: Float = 1.0f
) {
    setPageTransformer(createMarginAndScalePageTransformer(marginPx, minScale, maxScale))
}


fun dpToPx(context: Context, dp: Int): Float {
    return dp * context.resources.displayMetrics.density
}
