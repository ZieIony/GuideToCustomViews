package com.github.zieiony.guide;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.github.zieiony.guide.graphview.GraphView;

import tk.zielony.randomdata.RandomData;
import tk.zielony.randomdata.common.FloatGenerator;
import tk.zielony.randomdata.food.StringFruitGenerator;

@ActivityAnnotation(layout = R.layout.activity_graphview, title = "GraphView")
public class GraphViewActivity extends SampleActivity {

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RandomData randomData = new RandomData();
        randomData.addGenerator(new StringFruitGenerator());
        randomData.addGenerator(new FloatGenerator(0, 100).withMatcher(field -> field.getName().equals("value")));

        GraphView.Item[] items = randomData.generateArray(GraphView.Item.class, 10);

        final GraphView graphView1 = findViewById(R.id.graphView1);
        graphView1.setItems(items);
        graphView1.setItemSpacing(4);
    }
}
