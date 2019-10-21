package com.github.zieiony.guide.progresstextview

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.widget.TextView

// no extending
class ProgressTextView_Bad : TextView {

    private var _progressColor: ColorStateList = DEFAULT_PROGRESSCOLOR
    private var _progress: Float = 0.0f
    private var _progressTextColor: ColorStateList = DEFAULT_PROGRESSTEXTCOLOR

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs) {
        // only the two required constructors
        attrs?.let {
            val a = getContext().obtainStyledAttributes(attrs, R.styleable.ProgressTextView_Bad)

            progress = a.getFloat(R.styleable.ProgressTextView_Bad_guide_progress, 0.0f)
            progressColor = a.getColorStateList(R.styleable.ProgressTextView_Bad_guide_progressColor)
                ?: DEFAULT_PROGRESSCOLOR
            progressTextColor = a.getColorStateList(R.styleable.ProgressTextView_Bad_guide_progressTextColor)
                ?: DEFAULT_PROGRESSTEXTCOLOR

            a.recycle()
        }
    }

    // no new accessibility events
    override fun getAccessibilityClassName(): CharSequence {
        return ProgressTextView_Bad::class.java.name
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

    var progressTextColor
        get() = _progressTextColor
        set(value) {
            _progressTextColor = value
            invalidate()
        }

    override fun draw(canvas: Canvas) {
        // save current values
        val textColor = textColors
        val backgroundColor = background

        // set progress colors. This triggers invalidate
        setTextColor(progressTextColor)
        background = ColorDrawable(progressColor.getColorForState(drawableState, progressColor.defaultColor))

        // draw half of the view using clipping
        var saveCount = canvas.save()
        canvas.clipRect(0.0f, 0.0f, progress * width, height.toFloat())
        super.draw(canvas)
        canvas.restoreToCount(saveCount)

        // restore original values
        setTextColor(textColor)
        background = backgroundColor

        // draw the other half of the view
        saveCount = canvas.save()
        canvas.clipRect(progress * width, 0.0f, width.toFloat(), height.toFloat())
        super.draw(canvas)
        canvas.restoreToCount(saveCount)
    }

    companion object {
        val DEFAULT_PROGRESSCOLOR = ColorStateList.valueOf(Color.WHITE)
        val DEFAULT_PROGRESSTEXTCOLOR = ColorStateList.valueOf(Color.BLACK)
    }
}