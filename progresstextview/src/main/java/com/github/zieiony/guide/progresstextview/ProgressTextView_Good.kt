package com.github.zieiony.guide.progresstextview

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Color
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import kotlin.math.max
import kotlin.math.min


// no extending
class ProgressTextView_Good : View {

    private var _text: String = ""
    private var _progressColor: ColorStateList = DEFAULT_PROGRESSCOLOR
    private var _progress: Float = 0.0f
    private var _progressTextColor: ColorStateList = DEFAULT_PROGRESSTEXTCOLOR
    private var _textColor: ColorStateList = DEFAULT_PROGRESSTEXTCOLOR
    private var layout: StaticLayout? = null
    private var paint = TextPaint(TextPaint.ANTI_ALIAS_FLAG)

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs) {
        // only the two required constructors
        attrs?.let {
            val a = getContext().obtainStyledAttributes(attrs, R.styleable.ProgressTextView_Good)

            textSize = a.getDimension(R.styleable.ProgressTextView_Good_guide_textSize, DEFAULT_TEXTSIZE)
            text = a.getString(R.styleable.ProgressTextView_Good_guide_text) ?: ""
            progress = a.getFloat(R.styleable.ProgressTextView_Good_guide_progress, 0.0f)
            progressColor = a.getColorStateList(R.styleable.ProgressTextView_Good_guide_progressColor)
                ?: DEFAULT_PROGRESSCOLOR
            progressTextColor = a.getColorStateList(R.styleable.ProgressTextView_Good_guide_progressTextColor)
                ?: DEFAULT_PROGRESSTEXTCOLOR
            textColor = a.getColorStateList(R.styleable.ProgressTextView_Good_guide_textColor)
                ?: DEFAULT_TEXTCOLOR

            a.recycle()
        }
    }

    // no new accessibility events
    override fun getAccessibilityClassName(): CharSequence {
        return ProgressTextView_Good::class.java.name
    }

    var progressColor
        get() = _progressColor
        set(value) {
            _progressColor = value
            // call invalidate to apply changes
            invalidate()
        }

    fun setProgressColor(color: Int) {
        progressColor = ColorStateList.valueOf(color)
        invalidate()
    }

    var progress
        get() = _progress
        set(value) {
            _progress = value
            invalidate()
        }

    var textSize
        get() = paint.textSize
        set(value) {
            paint.textSize = value
            requestLayout()
        }

    var text
        get() = _text
        set(value) {
            _text = value
            requestLayout()
        }

    var progressTextColor
        get() = _progressTextColor
        set(value) {
            _progressTextColor = value
            invalidate()
        }

    var textColor
        get() = _textColor
        set(value) {
            _textColor = value
            invalidate()
        }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        layout = StaticLayout(text, paint, width, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // draw half of the view using clipping
        var saveCount = canvas.save()
        canvas.clipRect(0.0f, 0.0f, progress * width, height.toFloat())

        paint.color = progressColor.getColorForState(drawableState, progressColor.defaultColor)
        canvas.drawRect(0.0f, 0.0f, progress * width, height.toFloat(), paint)

        layout?.let {
            canvas.translate(paddingLeft.toFloat(), max(paddingTop.toFloat(), (height - it.height) / 2.0f))
            paint.color = progressTextColor.getColorForState(drawableState, progressTextColor.defaultColor)
            it.draw(canvas)
        }

        canvas.restoreToCount(saveCount)


        // draw the other half of the view
        saveCount = canvas.save()
        canvas.clipRect(progress * width, 0.0f, width.toFloat(), height.toFloat())

        layout?.let {
            canvas.translate(paddingLeft.toFloat(), max(paddingTop.toFloat(), (height - it.height) / 2.0f))
            paint.color = textColor.getColorForState(drawableState, textColor.defaultColor)
            it.draw(canvas)
        }

        canvas.restoreToCount(saveCount)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        var width = paddingLeft + paddingRight
        var height = paddingTop + paddingBottom

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize
        } else {
            layout = StaticLayout(text, paint, Integer.MAX_VALUE, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false)
            width += (0 until layout!!.lineCount).map { layout!!.getLineWidth(it) }.max()!!.toInt()

            width = max(width, suggestedMinimumWidth)
            if (widthMode == MeasureSpec.AT_MOST)
                width = min(widthSize, width)
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize
        } else {
            layout = StaticLayout(text, paint, width, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false)
            height += layout!!.height

            height = max(height, suggestedMinimumHeight)
            if (heightMode == MeasureSpec.AT_MOST)
                height = min(height, heightSize)
        }

        setMeasuredDimension(width, height)
    }

    companion object {
        val DEFAULT_PROGRESSCOLOR = ColorStateList.valueOf(Color.BLUE)
        val DEFAULT_PROGRESSTEXTCOLOR = ColorStateList.valueOf(Color.WHITE)
        val DEFAULT_TEXTCOLOR = ColorStateList.valueOf(Color.BLACK)
        const val DEFAULT_TEXTSIZE = 10.0f
    }
}