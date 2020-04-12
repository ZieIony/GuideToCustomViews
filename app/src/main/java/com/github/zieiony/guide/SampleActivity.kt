package com.github.zieiony.guide

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import carbon.widget.Toolbar

abstract class SampleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        javaClass.getAnnotation(ActivityAnnotation::class.java)?.let {
            setContentView(it.layout)
            findViewById<Toolbar>(R.id.toolbar)?.title = it.title
        }
    }
}