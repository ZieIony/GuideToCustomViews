package com.github.zieiony.guide.landscapedrawable

import android.animation.ArgbEvaluator
import android.graphics.*
import android.graphics.drawable.Drawable
import com.github.zieiony.guide.landscapedrawable.MathUtils.randomForce
import java.util.*


class LandscapeDrawable(
    var starCount: Int,
    var starSize: Float,
    var starColor: Int,
    var cloudCount: Int,
    var cloudSize: Float,
    var cloudColor: Int,
    var windStrength: Float,
    var maxWind: Float,
    var sunSize: Float,
    var sunColor: Int,
    var skyColor: Int,
    var skyHeight: Float,
    var fogColor: Int,
    var landscapeColor: Int,
    var planesCount: Int,
    var landscapeHeight: Float,
    var treeHeight: Float
) : Drawable() {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val random = Random()
    private val argbEvaluator = ArgbEvaluator()

    private var landscapes = mutableListOf<Land>()

    private val stars = Stars(starCount, starSize, starColor, random, paint)

    private var sunX = 0f
    private var sunY = 0f
    private var wind = 0f

    private val clouds = Sky(cloudCount, cloudSize, cloudColor, 8, 0f, random, paint)

    private fun init() {
        landscapes.clear()
        for (i in 0 until planesCount) {
            val currTreeHeight = MathUtils.lerp(treeHeight, treeHeight / 2, i / (planesCount - 1f))
            val height = landscapeHeight * (i + 1) / (planesCount + 1f)
            val fluctuation = height / 2
            val color1 = argbEvaluator.evaluate(i.toFloat() / planesCount, landscapeColor, fogColor) as Int
            val color2 = argbEvaluator.evaluate((i + 1).toFloat() / planesCount, landscapeColor, fogColor) as Int
            val land = Land(color1, color2, fluctuation, currTreeHeight, 0.8f, random, paint)
            land.y = bounds.height() - height - currTreeHeight
            land.setSize(bounds.width().toFloat(), height + currTreeHeight)
            landscapes.add(0, land)
        }

        stars.setSize(bounds.width().toFloat(), skyHeight)
        clouds.setSize(bounds.width().toFloat(), skyHeight)
        sunX = random.nextFloat() * (bounds.width() - sunSize * 2) + sunSize
        sunY = random.nextFloat() * (skyHeight - sunSize) + sunSize
    }

    override fun setBounds(left: Int, top: Int, right: Int, bottom: Int) {
        super.setBounds(left, top, right, bottom)
        init()
    }

    override fun draw(canvas: Canvas) {
        wind += randomForce(windStrength)
        wind = MathUtils.constrain(wind, 0f, maxWind)

        paint.shader = LinearGradient(0f, bounds.height() - landscapes[0].height - landscapes[0].fluctuation, 0f, 0f, fogColor, skyColor, Shader.TileMode.CLAMP)
        canvas.drawPaint(paint)
        paint.shader = null

        stars.draw(canvas)
        paint.color = sunColor
        canvas.drawCircle(sunX, sunY, sunSize, paint)

        for (l in landscapes) {
            l.wind = wind / 3
            l.draw(canvas)
        }

        clouds.wind = wind
        clouds.draw(canvas)

        if (isVisible)
            invalidateSelf()
    }

    override fun setAlpha(alpha: Int) {
        // not supported
    }

    override fun getOpacity() = PixelFormat.OPAQUE

    override fun setColorFilter(colorFilter: ColorFilter?) {
        // not supported
    }

}