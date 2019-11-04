package com.github.zieiony.guide.chartview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.Arrays;

public class ChartView extends View {
    public static class Item {
        String name;
        float value;

        public Item() {
        }

        public Item(String name, float value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public float getValue() {
            return value;
        }

        public void setValue(float value) {
            this.value = value;
        }
    }

    private Item[] items;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float spacing = 0;

    public ChartView(Context context) {
        super(context);
    }

    public ChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setItems(Item[] items) {
        this.items = Arrays.copyOf(items, items.length);
    }

    public void setItemSpacing(float spacing) {
        this.spacing = spacing;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (items == null)
            return;

        paint.setColor(Color.RED);

        float viewportWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        float viewportHeight = getHeight() - getPaddingTop() - getPaddingBottom();
        float itemWidth = viewportWidth / items.length;
        float maxItemHeight = 0;
        for (Item item : items)
            maxItemHeight = Math.max(maxItemHeight, item.value);

        for (int i = 0; i < items.length; i++) {
            Item item = items[i];
            canvas.drawRect(
                    getPaddingLeft() + i * itemWidth + spacing / 2,
                    getHeight() - getPaddingBottom() - viewportHeight / maxItemHeight * item.value,
                    getPaddingLeft() + (i + 1) * itemWidth - spacing / 2,
                    getHeight() - getPaddingBottom(),
                    paint);
        }
    }
}
