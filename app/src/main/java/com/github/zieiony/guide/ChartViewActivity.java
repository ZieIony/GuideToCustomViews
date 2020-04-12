package com.github.zieiony.guide;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.github.zieiony.guide.chartview.ChartView;
import com.github.zieiony.guide.chartview.ChartView2;
import com.github.zieiony.guide.chartview.ChartView3;

import tk.zielony.randomdata.RandomData;
import tk.zielony.randomdata.common.FloatGenerator;
import tk.zielony.randomdata.food.StringFruitGenerator;

@ActivityAnnotation(layout = R.layout.activity_chartview, title = "ChartView")
public class ChartViewActivity extends SampleActivity {

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RandomData randomData = new RandomData();
        randomData.addGenerator(new StringFruitGenerator());
        randomData.addGenerator(new FloatGenerator(0, 100).withMatcher(field -> field.getName().equals("value")));

        ChartView.Item[] items = randomData.generateArray(ChartView.Item.class, 10);
        final ChartView graphView1 = findViewById(R.id.chartView1);
        graphView1.setItems(items);
        graphView1.setItemSpacing(4);

        ChartView2.Item[] items2 = randomData.generateArray(ChartView2.Item.class, 10);
        final ChartView2 chartView2 = findViewById(R.id.chartView2);
        chartView2.setItems(items2);

        ChartView3.Item[] items3 = randomData.generateArray(ChartView3.Item.class, 10);
        final ChartView3 chartView3 = findViewById(R.id.chartView3);
        chartView3.setItems(items3);
    }
}
