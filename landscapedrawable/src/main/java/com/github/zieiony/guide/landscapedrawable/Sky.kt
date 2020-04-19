package com.github.zieiony.guide.landscapedrawable

import android.graphics.Canvas
import android.graphics.Paint
import java.util.*

class Sky(
    val cloudCount: Int,
    val cloudSize: Float,
    val cloudColor: Int,
    val puffCount: Int,
    var wind: Float,
    val random: Random,
    paint: Paint
) : LandscapeItem(paint) {

    private val clouds: MutableList<Cloud> = ArrayList()
    private var time = System.currentTimeMillis()

    init {
        clouds.clear()
        for (i in 0 until cloudCount) {
            val cloud = Cloud(puffCount, cloudColor, random, paint)
            cloud.z = random.nextFloat() * 0.25f + 0.75f
            cloud.setSize(cloudSize * cloud.z, cloudSize / 2 * cloud.z)
            clouds.add(cloud)
        }
    }

    constructor(cloudCount: Int = 3, cloudSize: Float, cloudColor: Int, puffCount: Int, wind: Float = 0f)
            : this(cloudCount, cloudSize, cloudColor, puffCount, wind, Random(), Paint(Paint.ANTI_ALIAS_FLAG))

    override fun onSizeChanged() {
        val cloudHeight = cloudSize / 2
        for (cloud in clouds) {
            cloud.x = random.nextFloat() * (width + cloudSize) - cloudSize
            cloud.y = random.nextFloat() * (height - cloudHeight)
        }
    }

    override fun onDraw(canvas: Canvas) {
        val currentTime = System.currentTimeMillis()
        val dt = (currentTime - time) / 1000f
        for (cloud in clouds) {
            cloud.x += wind * dt * cloud.z
            if (cloud.x > width)
                cloud.x = -cloud.width
        }
        time = currentTime

        for (cloud in clouds)
            cloud.draw(canvas)
    }
}