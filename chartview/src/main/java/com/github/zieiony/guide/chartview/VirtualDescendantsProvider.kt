package com.github.zieiony.guide.chartview

import android.graphics.Rect
import android.os.Bundle
import android.text.TextUtils
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import androidx.core.view.ViewCompat
import androidx.core.view.accessibility.AccessibilityEventCompat
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import androidx.core.view.accessibility.AccessibilityNodeProviderCompat
import androidx.core.view.accessibility.AccessibilityRecordCompat

internal class VirtualDescendantsProvider(private val chart: ChartView3) : AccessibilityNodeProviderCompat() {

    private var hostInfo: AccessibilityNodeInfoCompat? = null

    private var focusedItem: ChartView3.Item? = null

    override fun createAccessibilityNodeInfo(virtualViewId: Int): AccessibilityNodeInfoCompat? {
        return if (virtualViewId == HOST_VIEW_ID) {
            createHostAccessibilityNodeInfo().also {
                hostInfo = it
            }
        } else {
            createChildAccessibilityNodeInfo(virtualViewId)
        }
    }

    private fun createHostAccessibilityNodeInfo(): AccessibilityNodeInfoCompat {
        val info = AccessibilityNodeInfoCompat.obtain(chart)
        ViewCompat.onInitializeAccessibilityNodeInfo(chart, info)

        info.isImportantForAccessibility = false
        info.isFocused = focusedItem == null
        info.isAccessibilityFocused = focusedItem == null
        chart.items?.let {
            for (i in it.indices)
                info.addChild(chart, i)
        }
        return info
    }

    private fun createChildAccessibilityNodeInfo(virtualViewId: Int): AccessibilityNodeInfoCompat? {
        val items = chart.items
        if (items == null || virtualViewId < 0 || virtualViewId >= items.size)
            return null

        val viewportWidth = chart.width - chart.paddingLeft - chart.paddingRight.toFloat()
        val itemWidth = viewportWidth / items.size
        val left = (chart.paddingLeft + virtualViewId * itemWidth + chart.itemSpacing * virtualViewId).toInt()
        val right = (chart.paddingLeft + (virtualViewId + 1) * itemWidth + chart.itemSpacing * virtualViewId).toInt()
        val item = items[virtualViewId]

        val info = AccessibilityNodeInfoCompat.obtain()
        ViewCompat.onInitializeAccessibilityNodeInfo(chart, info)

        info.addAction(AccessibilityNodeInfo.ACTION_SELECT)
        info.addAction(AccessibilityNodeInfoCompat.ACTION_ACCESSIBILITY_FOCUS)
        info.addAction(AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS)
        info.isEnabled = true
        info.isFocusable = true
        info.isAccessibilityFocused = focusedItem === item
        info.isFocused = focusedItem === item
        info.isSelected = focusedItem === item
        info.isImportantForAccessibility = true
        info.packageName = chart.context.packageName
        info.className = chart.javaClass.name
        info.setSource(chart, virtualViewId)
        info.contentDescription = item.name
        info.setBoundsInParent(Rect(left, chart.paddingTop, right, chart.height - chart.paddingBottom))
        info.setParent(chart)
        info.isVisibleToUser = true
        info.text = item.name
        info.drawingOrder = 2 + virtualViewId
        val viewId = chart.resources.getResourceName(chart.id)
        info.viewIdResourceName = viewId
        return info
    }

    override fun findAccessibilityNodeInfosByText(searched: String, virtualViewId: Int): List<AccessibilityNodeInfoCompat> {
        val items = chart.items
        if (TextUtils.isEmpty(searched) || items == null)
            return emptyList()

        val searchedLowerCase = searched.toLowerCase()
        val result = mutableListOf<AccessibilityNodeInfoCompat>()
        if (virtualViewId == HOST_VIEW_ID) {
            for (i in items.indices) {
                val item = items[i]
                val textToLowerCase = item.name.toLowerCase()
                if (textToLowerCase.contains(searchedLowerCase))
                    result.add(createAccessibilityNodeInfo(i)!!)
            }
        } else {
            val item = items[virtualViewId]
            val textToLowerCase = item.name.toLowerCase()
            if (textToLowerCase.contains(searchedLowerCase))
                result.add(createAccessibilityNodeInfo(virtualViewId)!!)
        }
        return result
    }

    override fun findFocus(focus: Int): AccessibilityNodeInfoCompat? {
        return hostInfo
    }

    override fun performAction(virtualViewId: Int, action: Int, arguments: Bundle?): Boolean {
        if (virtualViewId == HOST_VIEW_ID)
            return ViewCompat.performAccessibilityAction(chart, action, arguments)

        val items = chart.items
        if (items == null || virtualViewId < 0 || virtualViewId >= items.size)
            return false

        when (action) {
            AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS -> {
                focusedItem = items[virtualViewId]
                val event = AccessibilityEvent.obtain(AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUSED)
                AccessibilityRecordCompat.setSource(event, chart, virtualViewId)
                chart.parent.requestSendAccessibilityEvent(chart, event)
                chart.invalidate()
                return true
            }
            AccessibilityNodeInfo.ACTION_CLEAR_ACCESSIBILITY_FOCUS -> {
                focusedItem = null
                chart.invalidate()
                return true
            }
            AccessibilityNodeInfo.ACTION_CLICK -> {
                chart.selectedItem = items[virtualViewId]
                val event = AccessibilityEvent.obtain(AccessibilityEventCompat.CONTENT_CHANGE_TYPE_UNDEFINED)
                AccessibilityRecordCompat.setSource(event, chart, virtualViewId)
                chart.parent.requestSendAccessibilityEvent(chart, event)
                chart.invalidate()
                return true
            }
            else -> return false
        }
    }

}