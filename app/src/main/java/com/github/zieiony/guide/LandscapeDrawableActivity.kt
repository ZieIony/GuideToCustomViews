package com.github.zieiony.guide

import android.os.Bundle
import com.github.zieiony.guide.landscapedrawable.*
import kotlinx.android.synthetic.main.activity_landscapedrawable.*

@ActivityAnnotation(layout = R.layout.activity_landscapedrawable, title = "LandscapeDrawable")
class LandscapeDrawableActivity : SampleActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cloud.setImageDrawable(
            ItemDrawable(
                Cloud(8, color = resources.getColor(R.color.landscape_cloudColor))
            )
        )
        tree.setImageDrawable(
            ItemDrawable(Tree(wind = 1f))
        )
        sky.setImageDrawable(
            ItemDrawable(
                Sky(
                    4,
                    cloudSize = resources.getDimension(R.dimen.landscape_cloudSize),
                    cloudColor = resources.getColor(R.color.landscape_cloudColor),
                    puffCount = 8,
                    wind = resources.getDimension(R.dimen.landscape_windStrength)
                )
            )
        )
        stars.setImageDrawable(
            ItemDrawable(
                Stars(
                    40,
                    resources.getDimension(R.dimen.landscape_starSize),
                    resources.getColor(R.color.landscape_starColor)
                )
            )
        )
        land.setImageDrawable(
            ItemDrawable(
                Land(
                    resources.getColor(R.color.landscape_landscapeColor),
                    resources.getColor(R.color.landscape_fogColor),
                    resources.getDimension(R.dimen.landscape_landscapeHeight) / 4
                )
            )
        )
        landscape.background = LandscapeDrawable(
            40,
            resources.getDimension(R.dimen.landscape_starSize),
            resources.getColor(R.color.landscape_starColor),
            4,
            cloudSize = resources.getDimension(R.dimen.landscape_cloudSize),
           cloudColor =  resources.getColor(R.color.landscape_cloudColor),
            fogColor = resources.getColor(R.color.landscape_fogColor),
            landscapeColor = resources.getColor(R.color.landscape_landscapeColor),
            skyColor = resources.getColor(R.color.landscape_skyColor),
            sunColor = resources.getColor(R.color.landscape_sunColor),
            skyHeight = resources.getDimension(R.dimen.landscape_skyHeight),
            planesCount = 5,
            windStrength = resources.getDimension(R.dimen.landscape_windStrength),
            sunSize = resources.getDimension(R.dimen.landscape_sunSize),
            landscapeHeight = resources.getDimension(R.dimen.landscape_landscapeHeight),
            treeHeight = resources.getDimension(R.dimen.landscape_treeHeight),
            maxWind = resources.getDimension(R.dimen.landscape_maxWind)
        )
    }
}