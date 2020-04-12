package com.github.zieiony.guide.moodtoggle

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View


class MoodToggle @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    data class Mood(val name: String, val image: Drawable)

    private val moods = mutableListOf<Mood>()

    var currentMood: Mood
        private set

    init {
        moods.add(Mood("satisfied", resources.getDrawable(R.drawable.ic_sentiment_satisfied_black_24dp)))
        moods.add(Mood("neutral", resources.getDrawable(R.drawable.ic_sentiment_neutral_black_24dp)))
        moods.add(Mood("dissatisfied", resources.getDrawable(R.drawable.ic_sentiment_dissatisfied_black_24dp)))
        currentMood = moods[0]
        contentDescription = "mood ${currentMood.name}"
        isClickable = true
    }

    override fun getAccessibilityClassName() = MoodToggle::class.java.simpleName

    override fun performClick(): Boolean {
        currentMood = moods[(moods.indexOf(currentMood) + 1) % moods.size]
        contentDescription = "mood ${currentMood.name}"
        super.performClick()
        invalidate()
        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        currentMood.image.setBounds(0, 0, width, height)
        currentMood.image.draw(canvas)
    }
}