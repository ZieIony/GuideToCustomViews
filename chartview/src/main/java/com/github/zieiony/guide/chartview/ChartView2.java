package com.github.zieiony.guide.chartview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.Arrays;

public class ChartView2 extends View {
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
    private ColorStateList itemColor;
    private Item selectedItem;

    public ChartView2(Context context) {
        super(context);
        initChartView(null, 0);
    }

    public ChartView2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initChartView(attrs, 0);
    }

    public ChartView2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initChartView(attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ChartView2(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initChartView(attrs, defStyleAttr);
    }

    private void initChartView(@Nullable AttributeSet attrs, int defStyleAttr) {
        if (attrs == null)
            return;

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ChartView2);

        setItemSpacing(a.getDimension(R.styleable.ChartView2_guide_itemSpacing, 0));
        setItemColor(a.getColorStateList(R.styleable.ChartView2_guide_itemColor));

        a.recycle();
    }

    public void setItems(Item[] items) {
        this.items = Arrays.copyOf(items, items.length);
    }

    public void setItemSpacing(float spacing) {
        this.spacing = spacing;
    }

    public void setItemColor(ColorStateList itemColor) {
        this.itemColor = itemColor;
    }

    public void setItemColor(int itemColor) {
        this.itemColor = ColorStateList.valueOf(itemColor);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (items == null) {
            selectedItem = null;
            return false;
        }

        float viewportWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        float itemWidth = viewportWidth / items.length;

        selectedItem = items[(int) Math.max(0, Math.min(Math.floor((event.getX() - getPaddingLeft()) / itemWidth), items.length - 1))];
        postInvalidate();

        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (items == null || itemColor == null)
            return;

        float viewportWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        float viewportHeight = getHeight() - getPaddingTop() - getPaddingBottom();
        float itemWidth = viewportWidth / items.length;
        float maxItemHeight = 0;
        for (Item item : items)
            maxItemHeight = Math.max(maxItemHeight, item.value);

        for (int i = 0; i < items.length; i++) {
            Item item = items[i];
            paint.setColor(itemColor.getColorForState(getDrawableStateSelected(selectedItem == item), itemColor.getDefaultColor()));
            canvas.drawRect(
                    getPaddingLeft() + i * itemWidth + spacing / 2,
                    getHeight() - getPaddingBottom() - viewportHeight / maxItemHeight * item.value,
                    getPaddingLeft() + (i + 1) * itemWidth - spacing / 2,
                    getHeight() - getPaddingBottom(),
                    paint);
        }
    }

    private int[] getDrawableStateSelected(boolean selected) {
        if (!selected) {
            return getDrawableState();
        } else {
            int[] newState = Arrays.copyOf(getDrawableState(), getDrawableState().length + 1);
            newState[newState.length - 1] = android.R.attr.state_selected;
            return newState;
        }
    }
}
