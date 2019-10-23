package com.github.zieiony.guide

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import carbon.component.DataBindingComponent
import carbon.component.PaddingItem
import carbon.component.PaddingRow
import carbon.recycler.RowArrayAdapter
import carbon.recycler.RowFactory
import carbon.widget.Toolbar
import kotlinx.android.synthetic.main.activity_main.*
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

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter: RowArrayAdapter<Serializable> = RowArrayAdapter(SampleItem::class.java, RowFactory { parent ->
            DataBindingComponent<SampleItem>(parent, R.layout.row_sample)
        })
        adapter.addFactory(PaddingItem::class.java) { PaddingRow(it) }
        adapter.setOnItemClickedListener(SampleItem::class.java, { view, item, position ->
            val activityClass = (item as SampleItem).activityClass
            val intent = Intent(view.context, activityClass)
            startActivity(intent)
        })

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        adapter.items = arrayOf(
            PaddingItem(resources.getDimensionPixelSize(R.dimen.carbon_paddingHalf)),
            SampleItem(ProgressTextViewActivity::class.java),
            SampleItem(GraphViewActivity::class.java),
            PaddingItem(resources.getDimensionPixelSize(R.dimen.carbon_paddingHalf))
        )
    }
}

abstract class SampleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        javaClass.getAnnotation(ActivityAnnotation::class.java)?.let {
            setContentView(it.layout)
            findViewById<Toolbar>(R.id.toolbar)?.title = it.title
        }
    }
}
