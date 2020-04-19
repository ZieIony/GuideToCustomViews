package com.github.zieiony.guide.landscapedrawable

import android.graphics.Canvas
import android.graphics.Paint


abstract class LandscapeItem(val paint: Paint) {
    var x: Float = 0f
    var y: Float = 0f
    var width: Float = 0f
    var height: Float = 0f

    fun setSize(width: Float, height: Float) {
        this.width = width
        this.height = height
        onSizeChanged()
    }

    open fun onSizeChanged() {}

    fun draw(canvas: Canvas) {
        canvas.save()
        canvas.translate(x, y)
        onDraw(canvas)
        canvas.restore()
    }

    abstract fun onDraw(canvas: Canvas)
}