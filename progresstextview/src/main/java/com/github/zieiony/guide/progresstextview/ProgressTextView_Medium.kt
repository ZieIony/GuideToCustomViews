package com.github.zieiony.guide.progresstextview

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import kotlin.math.max

// no extending
class ProgressTextView_Medium : View {

    private val bounds = Rect()
    private var _text: String = ""
    private var _progressColor: ColorStateList = DEFAULT_PROGRESSCOLOR
    private var _progress: Float = 0.0f
    private var _progressTextColor: ColorStateList = DEFAULT_PROGRESSTEXTCOLOR
    private var _textColor: ColorStateList = DEFAULT_PROGRESSTEXTCOLOR
    private var paint = TextPaint(TextPaint.ANTI_ALIAS_FLAG)

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs) {
        // only the two required constructors
        attrs?.let {
            val a = getContext().obtainStyledAttributes(attrs, R.styleable.ProgressTextView_Medium)

            text = a.getString(R.styleable.ProgressTextView_Medium_guide_text) ?: ""
            progress = a.getFloat(R.styleable.ProgressTextView_Medium_guide_progress, 0.0f)
            progressColor = a.getColorStateList(R.styleable.ProgressTextView_Medium_guide_progressColor)
                ?: DEFAULT_PROGRESSCOLOR
            progressTextColor = a.getColorStateList(R.styleable.ProgressTextView_Medium_guide_progressTextColor)
                ?: DEFAULT_PROGRESSTEXTCOLOR
            textColor = a.getColorStateList(R.styleable.ProgressTextView_Medium_guide_textColor)
                ?: DEFAULT_TEXTCOLOR
            textSize = a.getDimension(R.styleable.ProgressTextView_Medium_guide_textSize, DEFAULT_TEXTSIZE)

            a.recycle()
        }
    }

    // no new accessibility events
    override fun getAccessibilityClassName(): CharSequence {
        return ProgressTextView_Medium::class.java.name
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
            invalidate()
        }

    var text
        get() = _text
        set(value) {
            _text = value
            invalidate()
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

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        paint.getTextBounds(text, 0, text.length, bounds)

        // draw half of the view using clipping
        var saveCount = canvas.save()
        canvas.clipRect(0.0f, 0.0f, progress * width, height.toFloat())

        paint.color = progressColor.getColorForState(drawableState, progressColor.defaultColor)
        canvas.drawRect(0.0f, 0.0f, progress * width, height.toFloat(), paint)

        canvas.translate(paddingLeft.toFloat(), max(paddingTop.toFloat(), (height + bounds.height())/2.0f))
        paint.color = progressTextColor.getColorForState(drawableState, progressTextColor.defaultColor)
        canvas.drawText(text, 0.0f, 0.0f, paint)

        canvas.restoreToCount(saveCount)

        // draw the other half of the view
        saveCount = canvas.save()
        canvas.clipRect(progress * width, 0.0f, width.toFloat(), height.toFloat())

        canvas.translate(paddingLeft.toFloat(), max(paddingTop.toFloat(), (height + bounds.height())/2.0f))
        paint.color = textColor.getColorForState(drawableState, textColor.defaultColor)
        canvas.drawText(text, 0.0f, 0.0f, paint)

        canvas.restoreToCount(saveCount)
    }

    companion object {
        val DEFAULT_PROGRESSCOLOR = ColorStateList.valueOf(Color.BLUE)
        val DEFAULT_PROGRESSTEXTCOLOR = ColorStateList.valueOf(Color.WHITE)
        val DEFAULT_TEXTCOLOR = ColorStateList.valueOf(Color.BLACK)
        const val DEFAULT_TEXTSIZE = 10.0f
    }
}