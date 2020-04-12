package com.github.zieiony.guide

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import carbon.component.DataBindingComponent
import carbon.component.PaddingItem
import carbon.component.PaddingRow
import carbon.recycler.RowArrayAdapter
import carbon.recycler.RowFactory
import kotlinx.android.synthetic.main.activity_main.*
import java.io.Serializable

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
            SampleItem(ChartViewActivity::class.java),
            SampleItem(FlowLayoutActivity::class.java),
            SampleItem(InvalidEditTextActivity::class.java),
            SampleItem(MoodToggleActivity::class.java),
            PaddingItem(resources.getDimensionPixelSize(R.dimen.carbon_paddingHalf))
        )
    }
}

