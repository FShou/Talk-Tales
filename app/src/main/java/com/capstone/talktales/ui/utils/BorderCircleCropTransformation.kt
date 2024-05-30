package com.capstone.talktales.ui.utils


import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import androidx.annotation.ColorInt
import androidx.core.graphics.applyCanvas
import androidx.core.graphics.createBitmap
import coil.size.Size
import coil.transform.Transformation

class BorderedCircleCropTransformation(
    private val borderWidthPx: Float = 0f,
    @ColorInt private val borderColor: Int = Color.TRANSPARENT,
) : Transformation {

    override val cacheKey: String = javaClass.name

    override suspend fun transform(input: Bitmap, size: Size): Bitmap {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)

        val minSize = minOf(input.width, input.height)
        val radius = minSize / 2f

        val output = createBitmap(minSize, minSize, input.config)
        output.applyCanvas {
            drawCircle(radius, radius, radius, paint)
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
            drawBitmap(input, radius - input.width / 2f, radius - input.height / 2f, paint)

            //border color
            drawCircle(radius,
                radius,
                radius,
                paint.apply {
                    style = Paint.Style.STROKE
                    paint.strokeWidth = borderWidthPx
                    paint.color = borderColor
                })
        }
        return output
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BorderedCircleCropTransformation

        if (borderWidthPx != other.borderWidthPx) return false
        if (borderColor != other.borderColor) return false
        if (cacheKey != other.cacheKey) return false

        return true
    }

    override fun hashCode(): Int {
        var result = borderWidthPx.hashCode()
        result = 31 * result + borderColor
        result = 31 * result + cacheKey.hashCode()
        return result
    }
}