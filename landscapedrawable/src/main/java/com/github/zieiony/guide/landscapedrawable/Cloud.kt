package com.github.zieiony.guide.landscapedrawable

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RadialGradient
import android.graphics.Shader
import java.util.*
import kotlin.math.min
import kotlin.math.pow

class Cloud : LandscapeItem {

    private class Puff(val x: Float, val y: Float, val size: Float, val shader: RadialGradient)

    var z: Float = 0f

    private var _color = 0
    var color: Int
        get() = _color
        set(value) {
            _color = value
            puffs.clear()
        }

    private var _puffCount = 8
    var puffCount: Int
        get() = _puffCount
        set(value) {
            _puffCount = value
            puffs.clear()
        }

    private val random: Random
    private val puffs = ArrayList<Puff>()

    constructor(puffCount: Int, color: Int, random: Random, paint: Paint) : super(paint) {
        this.puffCount = puffCount
        this.color = color
        this.random = random
    }

    constructor(puffCount: Int, color: Int) : this(puffCount, color, Random(), Paint(Paint.ANTI_ALIAS_FLAG))

    override fun onSizeChanged() {
        puffs.clear()
    }

    fun init() {
        if (puffs.isEmpty()) {
            val maxPuffSize = min(width, height) / 2
            for (i in 0 until puffCount) {
                val size = random.nextFloat() * maxPuffSize / 2 + maxPuffSize / 2
                val x = nextPos() * (width - 2 * size) + size
                val y = (nextPos() * (height - 2 * size) + size) / 0.8f
                val shader = RadialGradient(x, y - size / 2, size, intArrayOf(-0x1, color), floatArrayOf(0.9f, 1f), Shader.TileMode.CLAMP)
                puffs.add(Puff(x, y, size, shader))
            }
            puffs.sortWith(Comparator { o1, o2 ->
                val dist1 = MathUtils.dist(width / 2, height / 2, o2.x, o2.y)
                val dist2 = MathUtils.dist(width / 2, height / 2, o1.x, o1.y)
                (dist1 - dist2).toInt()
            })
        }
    }

    private fun nextPos(): Float {
        return (((random.nextDouble() - 0.5).pow(3) + 0.125) * 4).toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        init()

        canvas.save()
        canvas.scale(1f, 0.8f)
        for (p in puffs) {
            paint.shader = p.shader
            canvas.drawCircle(p.x, p.y, p.size, paint)
        }
        canvas.restore()
    }
}