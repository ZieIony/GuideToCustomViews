package com.github.zieiony.guide

import android.app.Activity
import java.io.Serializable

open class SampleItem : Serializable {
    var activityClass: Class<out Activity>? = null
        private set
    var name: String? = null
        private set
    var icon = 0
        private set

    @JvmOverloads
    constructor(activityClass: Class<out Activity>, icon: Int = 0) : super(
    ) {
        this.activityClass = activityClass
        this.name = activityClass.getAnnotation(ActivityAnnotation::class.java)?.title
        this.icon = icon
    }
}