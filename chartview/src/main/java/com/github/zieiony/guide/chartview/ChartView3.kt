package com.github.zieiony.guide.chartview

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.view.ViewCompat
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import androidx.customview.widget.ExploreByTouchHelper
import com.github.zieiony.guide.chartview.ChartView3
import java.util.*
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min

class ChartView3 : View {
    class Item {
        lateinit var name: String
        var value = 0f

        constructor()
        constructor(name: String, value: Float) {
            this.name = name
            this.value = value
        }

    }

    private inner class AccessHelper : ExploreByTouchHelper(this) {
        override fun getVirtualViewAt(x: Float, y: Float): Int {
            if (x < paddingLeft || y < paddingTop || x > width - paddingRight || y > height - paddingBottom)
                return HOST_ID
            items?.let { items ->
                val viewportWidth = width - paddingLeft - paddingRight.toFloat()
                val itemWidth = viewportWidth / items.size
                return max(0, min(floor((x - paddingLeft) / itemWidth).toInt(), items.size - 1))
            }
            return HOST_ID
        }

        override fun getVisibleVirtualViews(virtualViewIds: MutableList<Int>) {
            items?.let {
                for (i in it.indices)
                    virtualViewIds.add(i)
            }
        }

        override fun onPopulateNodeForVirtualView(virtualViewId: Int, node: AccessibilityNodeInfoCompat) {
            items?.let { items ->
                node.className = accessibilityClassName
                node.contentDescription = "item ${virtualViewId + 1} - ${items[virtualViewId].name}, ${items[virtualViewId].value}"
                node.isClickable = true

                val viewportWidth = width - paddingLeft - paddingRight.toFloat()
                val viewportHeight = height - paddingTop - paddingBottom.toFloat()
                val itemWidth = viewportWidth / items.size
                var maxItemHeight = 0f

                for (item in items)
                    maxItemHeight = max(maxItemHeight, item.value)

                val rect = Rect(
                    (paddingLeft + virtualViewId * itemWidth + itemSpacing / 2).toInt(),
                    (height - paddingBottom - viewportHeight / maxItemHeight * items[virtualViewId].value).toInt(),
                    (paddingLeft + (virtualViewId + 1) * itemWidth - itemSpacing / 2).toInt(),
                    (height - paddingBottom.toFloat()).toInt()
                )

                node.setBoundsInParent(rect)
            }
        }

        override fun onPerformActionForVirtualView(virtualViewId: Int, action: Int, arguments: Bundle?): Boolean {
            items?.let { items ->
                if (virtualViewId >= 0 && virtualViewId < items.size)
                    selectedItem = items[virtualViewId]
                return true
            }
            return false
        }
    }

    var items: Array<Item>? = null
        private set
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    var itemSpacing = 0f
    var itemColor: ColorStateList? = null
    var selectedItem: Item? = null

    private val accessHelper = AccessHelper()

    constructor(context: Context?) : super(context) {
        initChartView(null, 0)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initChartView(attrs, 0)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initChartView(attrs, defStyleAttr)
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        initChartView(attrs, defStyleAttr)
    }

    private fun initChartView(attrs: AttributeSet?, defStyleAttr: Int) {
        if (attrs == null)
            return

        val a = context.obtainStyledAttributes(attrs, R.styleable.ChartView2)

        itemSpacing = a.getDimension(R.styleable.ChartView2_guide_itemSpacing, 0f)
        itemColor = a.getColorStateList(R.styleable.ChartView2_guide_itemColor)

        a.recycle()

        ViewCompat.setAccessibilityDelegate(this, accessHelper)
    }

    fun setItems(items: Array<Item?>) {
        this.items = Arrays.copyOf(items, items.size)
    }

    fun setItemColor(itemColor: Int) {
        this.itemColor = ColorStateList.valueOf(itemColor)
    }

    private var prevX = 0f

    override fun onTouchEvent(event: MotionEvent): Boolean {
        prevX = event.x
        return super.onTouchEvent(event)
    }

    override fun performClick(): Boolean {
        val items = this.items
        if (items == null) {
            selectedItem = null
            return performClick()
        }
        val viewportWidth = width - paddingLeft - paddingRight.toFloat()
        val itemWidth = viewportWidth / items.size
        selectedItem = items[max(0, min(floor((prevX - paddingLeft) / itemWidth).toInt(), items.size - 1))]
        postInvalidate()
        super.performClick()
        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val items = this.items
        val itemColor = this.itemColor
        if (items == null || itemColor == null)
            return

        val viewportWidth = width - paddingLeft - paddingRight.toFloat()
        val viewportHeight = height - paddingTop - paddingBottom.toFloat()
        val itemWidth = viewportWidth / items.size
        var maxItemHeight = 0f

        for (item in items)
            maxItemHeight = max(maxItemHeight, item.value)
        for (i in items.indices) {
            val item = items[i]
            paint.color = itemColor.getColorForState(getDrawableStateSelected(selectedItem === item), itemColor.defaultColor)
            canvas.drawRect(
                paddingLeft + i * itemWidth + itemSpacing / 2,
                height - paddingBottom - viewportHeight / maxItemHeight * item.value,
                paddingLeft + (i + 1) * itemWidth - itemSpacing / 2,
                height - paddingBottom.toFloat(),
                paint
            )
        }
    }

    private fun getDrawableStateSelected(selected: Boolean): IntArray {
        return if (!selected) {
            drawableState
        } else {
            val newState = Arrays.copyOf(drawableState, drawableState.size + 1)
            newState[newState.size - 1] = android.R.attr.state_selected
            newState
        }
    }

    override fun dispatchHoverEvent(event: MotionEvent): Boolean {
        return accessHelper.dispatchHoverEvent(event) || super.dispatchHoverEvent(event)
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        return accessHelper.dispatchKeyEvent(event) || super.dispatchKeyEvent(event)
    }

    override fun onFocusChanged(gainFocus: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect)
        accessHelper.onFocusChanged(gainFocus, direction, previouslyFocusedRect)
    }

    override fun getAccessibilityClassName() = ChartView3::class.java.name

}