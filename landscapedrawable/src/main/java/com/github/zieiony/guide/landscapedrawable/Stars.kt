package com.github.zieiony.guide.landscapedrawable

import android.graphics.Canvas
import android.graphics.Paint
import java.util.*


class Stars(
    var starCount: Int,
    var starSize: Float,
    var starColor: Int,
    val random: Random,
    paint: Paint
) : LandscapeItem(paint) {

    class Star(val x: Float, val y: Float, val z: Float)

    private var stars = mutableListOf<Star>()

    constructor(starCount: Int, starSize: Float, starColor: Int) : this(starCount, starSize, starColor, Random(), Paint(Paint.ANTI_ALIAS_FLAG))

    override fun onSizeChanged() {
        stars.clear()
        for (i in 0 until starCount) {
            val z = random.nextFloat()
            stars.add(Star(random.nextFloat() * width, random.nextFloat() * height, z))
        }
    }

    override fun onDraw(canvas: Canvas) {
        paint.color = starColor
        for (s in stars) {
            paint.alpha = (255f * s.z).toInt()
            canvas.drawCircle(s.x, s.y, s.z * starSize, paint)
        }
        paint.alpha = 255
    }
}