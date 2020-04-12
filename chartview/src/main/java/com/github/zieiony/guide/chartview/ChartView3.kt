package com.github.zieiony.guide.chartview

import android.app.Service
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityManager
import android.view.accessibility.AccessibilityNodeProvider
import androidx.annotation.RequiresApi
import androidx.core.view.accessibility.AccessibilityEventCompat
import androidx.core.view.accessibility.AccessibilityNodeProviderCompat
import androidx.core.view.accessibility.AccessibilityRecordCompat
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

    var items: Array<Item>? = null
        private set
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    var itemSpacing = 0f
    var itemColor: ColorStateList? = null
    var selectedItem: Item? = null

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

        isClickable = true
        accessibilityManager = context.getSystemService(Service.ACCESSIBILITY_SERVICE) as AccessibilityManager
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

    private lateinit var accessibilityManager: AccessibilityManager
    private val accessibilityNodeProvider: AccessibilityNodeProviderCompat = VirtualDescendantsProvider(this)
    private var prevHoveredItem: Item? = null

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    override fun getAccessibilityNodeProvider(): AccessibilityNodeProvider {
        return accessibilityNodeProvider.provider as AccessibilityNodeProvider
    }

    override fun getAccessibilityClassName() = ChartView3::class.java.name

    public override fun dispatchHoverEvent(event: MotionEvent): Boolean {
        val items = this.items ?: return super.dispatchHoverEvent(event)

        val viewportWidth = width - paddingLeft - paddingRight.toFloat()
        val itemWidth = viewportWidth / items.size
        val child = items[max(0, min(floor((event.x - paddingLeft) / itemWidth).toInt(), items.size - 1))]
        var handled = false
        val action = event.action
        when (action) {
            MotionEvent.ACTION_HOVER_ENTER -> {
                prevHoveredItem = child
                handled = handled or onHoverItem(child, event)
                event.action = action
            }
            MotionEvent.ACTION_HOVER_MOVE -> {
                if (child === prevHoveredItem) {
                    handled = handled or onHoverItem(child, event)
                    event.action = action
                } else {
                    val eventNoHistory = if (event.historySize > 0) MotionEvent.obtainNoHistory(event) else event
                    eventNoHistory.action = MotionEvent.ACTION_HOVER_EXIT
                    onHoverItem(prevHoveredItem!!, eventNoHistory)
                    eventNoHistory.action = MotionEvent.ACTION_HOVER_ENTER
                    onHoverItem(child, eventNoHistory)
                    prevHoveredItem = child
                    eventNoHistory.action = MotionEvent.ACTION_HOVER_MOVE
                    handled = handled or onHoverItem(child, eventNoHistory)
                    if (eventNoHistory != event) {
                        eventNoHistory.recycle()
                    } else {
                        event.action = action
                    }
                }
            }
            MotionEvent.ACTION_HOVER_EXIT -> {
                prevHoveredItem = null
                handled = handled or onHoverItem(child, event)
                event.action = action
            }
        }
        return handled
    }

    private fun onHoverItem(virtualView: Item, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_HOVER_ENTER -> {
                sendAccessibilityEventForItem(
                    virtualView,
                    AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUSED
                )
            }
            MotionEvent.ACTION_HOVER_EXIT -> {
            }
        }
        return true
    }

    private fun sendAccessibilityEventForItem(item: Item, eventType: Int) {
        if (accessibilityManager.isTouchExplorationEnabled) {
            val event = AccessibilityEvent.obtain(eventType)
            AccessibilityRecordCompat.setSource(event, this, items!!.indexOf(item))
            event.packageName = context.packageName
            event.className = javaClass.name
            event.text.add(item.name)
            parent.requestSendAccessibilityEvent(this, event) // send event clears source
            invalidate()
        }
    }
}