package com.github.zieiony.guide.landscapedrawable

import android.graphics.*
import java.util.*

class Land : LandscapeItem {
    private var _color = 0
    var color: Int
        get() = _color
        set(value) {
            _color = value
            shader = null
        }

    private var _color2 = 0
    var color2: Int
        get() = _color2
        set(value) {
            _color2 = value
            shader = null
        }

    private var _fluctuation = 0.0f
    var fluctuation: Float
        get() = _fluctuation
        set(value) {
            _fluctuation = value
            path.reset()
            trees.clear()
            shader = null
        }

    private var _treeHeight = 0.0f
    var treeHeight: Float
        get() = _treeHeight
        set(value) {
            _treeHeight = value
            path.reset()
            trees.clear()
            shader = null
        }

    private var _treeRandom = 0.0f
    var treeRandom: Float
        get() = _treeRandom
        set(value) {
            _treeRandom = value
            path.reset()
            trees.clear()
        }

    var wind: Float = 0f

    private val random: Random
    private val path = Path()
    private val trees = ArrayList<Tree>()
    private var shader: LinearGradient? = null

    constructor(color: Int, color2: Int, fluctuation: Float, treeHeight: Float, treeRandom: Float, random: Random, paint: Paint) : super(paint) {
        this.color = color
        this.color2 = color2
        this.fluctuation = fluctuation
        this.treeHeight = treeHeight
        this.treeRandom = treeRandom
        this.random = random
    }

    constructor(color: Int, color2: Int, fluctuation: Float, treeHeight: Float = fluctuation, treeRandom: Float = 0.8f)
            : this(color, color2, fluctuation, treeHeight, treeRandom, Random(), Paint(Paint.ANTI_ALIAS_FLAG))

    override fun onSizeChanged() {
        path.reset()
        trees.clear()
        shader = null
    }

    fun init() {
        if (path.isEmpty) {
            path.moveTo(0f, height)
            path.lineTo(width, height)
            var prevX: Float = width
            var prevY = (treeHeight + Math.random() * fluctuation).toFloat()
            path.lineTo(prevX, prevY)

            val segments = (width / treeHeight / 3).toInt()
            val treeWidth = treeHeight * 2 / 3f
            for (i in 0..segments) {
                val x: Float = (width * (segments - i) / segments)
                val y = (treeHeight + Math.random() * fluctuation).toFloat()
                val x33 = MathUtils.lerp(prevX, x, 0.33f)
                val x67 = MathUtils.lerp(prevX, x, 0.67f)
                path.cubicTo(x33, prevY, x67, y, x, y)
                if (random.nextFloat() > treeRandom) {
                    val tree = Tree(wind, paint)
                    tree.x = prevX - treeWidth / 2
                    tree.y = prevY - treeHeight
                    tree.setSize(treeWidth, treeHeight)
                    trees.add(tree)
                }
                if (random.nextFloat() > treeRandom) {
                    val y33 = MathUtils.lerp(prevY, y, 0.33f)
                    val tree = Tree(wind, paint)
                    tree.x = x33 - treeWidth / 2
                    tree.y = y33 - treeHeight
                    tree.setSize(treeWidth, treeHeight)
                    trees.add(tree)
                }
                if (random.nextFloat() > treeRandom) {
                    val y67 = MathUtils.lerp(prevY, y, 0.67f)
                    val tree = Tree(wind, paint)
                    tree.x = x67 - treeWidth / 2
                    tree.y = y67 - treeHeight
                    tree.setSize(treeWidth, treeHeight)
                    trees.add(tree)
                }
                if (random.nextFloat() > treeRandom) {
                    val tree = Tree(wind, paint)
                    tree.x = x - treeWidth / 2
                    tree.y = y - treeHeight
                    tree.setSize(treeWidth, treeHeight)
                    trees.add(tree)
                }
                prevX = x
                prevY = y
            }
            path.close()
        }

        if (shader == null)
            shader = LinearGradient(0f, height, 0f, treeHeight, color, color2, Shader.TileMode.CLAMP)
    }

    override fun onDraw(canvas: Canvas) {
        init()

        if (wind > 0) {
            for (t in trees)
                t.wind = wind
        }

        paint.color = color2
        paint.shader = shader
        for (t in trees)
            t.draw(canvas)
        canvas.drawPath(path, paint)
        paint.shader = null
    }

}