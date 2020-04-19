package com.github.zieiony.guide.landscapedrawable

import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.PixelFormat
import android.graphics.Rect
import android.graphics.drawable.Drawable


class ItemDrawable(val item: LandscapeItem) : Drawable() {

    override fun onBoundsChange(bounds: Rect) {
        super.onBoundsChange(bounds)
        item.setSize(bounds.width().toFloat(), bounds.height().toFloat())
    }

    override fun draw(canvas: Canvas) {
        item.onDraw(canvas)
    }

    override fun setAlpha(alpha: Int) {
        // not supported
    }

    override fun getOpacity() = PixelFormat.TRANSLUCENT

    override fun setColorFilter(colorFilter: ColorFilter?) {
        // not supported
    }
}