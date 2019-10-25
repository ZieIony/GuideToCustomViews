package com.github.zieiony.guide.invalidedittext

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class InvalidEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {

    private var _valid: Boolean = false
    var valid
        get() = _valid
        set(value) {
            if (_valid == value)
                return
            _valid = value
            refreshDrawableState()
        }

    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        if (!valid) {
            val drawableState = super.onCreateDrawableState(extraSpace + 1)
            drawableState[drawableState.size - 1] = R.attr.guide_state_invalid
            return drawableState
        }
        return super.onCreateDrawableState(extraSpace)
    }
}