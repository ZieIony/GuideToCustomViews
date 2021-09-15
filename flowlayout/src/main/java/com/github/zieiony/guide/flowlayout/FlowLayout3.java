package com.github.zieiony.guide.flowlayout;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * FlowLayout layouts its children from left to right, top to bottom.
 */
public class FlowLayout3 extends android.widget.FrameLayout {

    private int gravity;

    public FlowLayout3(Context context) {
        super(context, null, R.attr.guide_flowLayoutStyle);
        initFlowLayout(null, R.attr.guide_flowLayoutStyle);
    }

    public FlowLayout3(Context context, AttributeSet attrs) {
        super(context, attrs, R.attr.guide_flowLayoutStyle);
        initFlowLayout(attrs, R.attr.guide_flowLayoutStyle);
    }

    public FlowLayout3(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initFlowLayout(attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FlowLayout3(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initFlowLayout(attrs, defStyleAttr);
    }

    private void initFlowLayout(AttributeSet attrs, int defStyleAttr) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.FlowLayout3, defStyleAttr, 0);

        gravity = a.getInt(R.styleable.FlowLayout3_android_gravity, Gravity.START);

        a.recycle();
    }

    public int getGravity() {
        return gravity;
    }

    public void setGravity(int gravity) {
        if (this.gravity != gravity) {
            this.gravity = gravity;
            requestLayout();
        }
    }

    private void layoutFlowingViews(int width) {
        int gravity = GravityCompat.getAbsoluteGravity(this.gravity, ViewCompat.getLayoutDirection(this));
        if ((gravity & Gravity.RIGHT) == Gravity.RIGHT) {
            layoutFlowingViewsRight(width);
        } else {
            layoutFlowingViewsLeft(width);
        }
    }

    private void relayoutLine(List<View> currentLine) {
        if (currentLine.size() < 2)
            return;

        int maxY = Integer.MIN_VALUE, minY = currentLine.get(0).getTop() - ((LayoutParams) currentLine.get(0).getLayoutParams()).topMargin;
        for (View view : currentLine) {
            LayoutParams params = (LayoutParams) view.getLayoutParams();
            maxY = Math.max(maxY, view.getBottom() + params.bottomMargin);
        }
        for (View view : currentLine) {
            LayoutParams params = (LayoutParams) view.getLayoutParams();
            if ((params.gravity & Gravity.TOP) == Gravity.TOP) {
                view.layout(view.getLeft(), minY + params.topMargin, view.getRight(), minY + view.getHeight() + params.topMargin);
            } else if ((params.gravity & Gravity.BOTTOM) == Gravity.BOTTOM) {
                view.layout(view.getLeft(), maxY - view.getHeight() - params.bottomMargin, view.getRight(), maxY - params.bottomMargin);
            } else if ((params.gravity & Gravity.CENTER_VERTICAL) == Gravity.CENTER_VERTICAL) {
                int top = Math.max((maxY + minY) / 2 - view.getHeight() / 2, minY + params.topMargin);
                int bottom = top + view.getHeight();
                view.layout(view.getLeft(), top, view.getRight(), bottom);
            }
        }
    }

    private void layoutFlowingViewsRight(int width) {
        int currentX = width - getPaddingRight();
        int currentY = getPaddingTop();
        int nextY = getPaddingTop();
        List<View> currentLine = new ArrayList<>();
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            LayoutParams params = (LayoutParams) child.getLayoutParams();
            if (child.getVisibility() != GONE) {
                if (currentX != width - getPaddingRight() && currentX - params.leftMargin - child.getMeasuredWidth() - params.rightMargin < getPaddingLeft()) {
                    currentX = width - getPaddingRight();
                    currentY = nextY;
                    relayoutLine(currentLine);
                    currentLine.clear();
                }

                currentLine.add(0, child);
                int left = params.fill ? getPaddingLeft() + params.leftMargin : currentX - params.rightMargin - child.getMeasuredWidth();
                child.layout(left, currentY + params.topMargin, currentX - params.rightMargin, currentY + params.topMargin + child.getMeasuredHeight());
                currentX -= params.leftMargin + child.getMeasuredWidth() + params.rightMargin;
                nextY = Math.max(nextY, currentY + params.topMargin + child.getMeasuredHeight() + params.bottomMargin);

                if (params.fill) {
                    currentX = width - getPaddingRight();
                    currentY = nextY;
                    relayoutLine(currentLine);
                    currentLine.clear();
                }
            }
        }
        relayoutLine(currentLine);
    }

    private void layoutFlowingViewsLeft(int width) {
        int currentX = getPaddingLeft();
        int currentY = getPaddingTop();
        int nextY = getPaddingTop();
        List<View> currentLine = new ArrayList<>();
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            LayoutParams params = (LayoutParams) child.getLayoutParams();
            if (child.getVisibility() != GONE) {
                if (currentX != getPaddingLeft() && currentX + params.leftMargin + child.getMeasuredWidth() + params.rightMargin > width - getPaddingRight()) {
                    currentX = getPaddingLeft();
                    currentY = nextY;
                    relayoutLine(currentLine);
                    currentLine.clear();
                }

                currentLine.add(child);
                int right = params.fill ? width - getPaddingRight() - params.rightMargin : currentX + params.leftMargin + child.getMeasuredWidth();
                child.layout(currentX + params.leftMargin, currentY + params.topMargin, right, currentY + params.topMargin + child.getMeasuredHeight());
                currentX += params.leftMargin + child.getMeasuredWidth() + params.rightMargin;
                nextY = Math.max(nextY, currentY + params.topMargin + child.getMeasuredHeight() + params.bottomMargin);

                if (params.fill) {
                    currentX = getPaddingLeft();
                    currentY = nextY;
                    relayoutLine(currentLine);
                    currentLine.clear();
                }
            }
        }
        relayoutLine(currentLine);
    }

    private int measureWidth() {
        int currentX = getPaddingLeft();
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            LayoutParams params = (LayoutParams) child.getLayoutParams();
            if (child.getVisibility() != GONE) {
                currentX += params.leftMargin + child.getMeasuredWidth() + params.rightMargin;
            }
        }

        return currentX + getPaddingRight();
    }

    private int measureHeight(int width) {
        int currentX = getPaddingLeft();
        int currentY = getPaddingTop();
        int nextY = getPaddingTop();
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            LayoutParams params = (LayoutParams) child.getLayoutParams();
            if (child.getVisibility() != GONE) {
                if (currentX != getPaddingLeft() && currentX + params.leftMargin + child.getMeasuredWidth() + params.rightMargin > width - getPaddingRight()) {
                    currentX = getPaddingLeft();
                    currentY = nextY;
                }

                currentX += params.leftMargin + child.getMeasuredWidth() + params.rightMargin;
                nextY = Math.max(nextY, currentY + params.topMargin + child.getMeasuredHeight() + params.bottomMargin);

                if (params.fill) {
                    currentX = getPaddingLeft();
                    currentY = nextY;
                }
            }
        }
        return nextY + getPaddingBottom();
    }

    protected void measureChildren(int widthMeasureSpec, int heightMeasureSpec) {
        for (int i = 0; i < getChildCount(); ++i) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE)
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        layoutFlowingViews(getWidth());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width;
        int height;

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            width = measureWidth();

            width = Math.max(width, getSuggestedMinimumWidth());
            if (widthMode == MeasureSpec.AT_MOST)
                width = Math.min(widthSize, width);
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = measureHeight(width);

            height = Math.max(height, getSuggestedMinimumHeight());
            if (heightMode == MeasureSpec.AT_MOST)
                height = Math.min(height, heightSize);
        }

        setMeasuredDimension(width, height);
    }

    // -------------------------------
    // layout params
    // -------------------------------

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(super.generateDefaultLayoutParams());
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    public static class LayoutParams extends android.widget.FrameLayout.LayoutParams {
        private boolean fill = false;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);

            TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.FlowLayout3_Layout);

            fill = a.getBoolean(R.styleable.FlowLayout3_Layout_guide_layout_fill, false);

            a.recycle();
        }

        public LayoutParams(int w, int h) {
            super(w, h);
        }

        /**
         * {@inheritDoc}
         */
        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        /**
         * {@inheritDoc}
         */
        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(android.widget.FrameLayout.LayoutParams source) {
            super((MarginLayoutParams) source);
        }

        public LayoutParams(LayoutParams source) {
            super((MarginLayoutParams) source);
            fill = source.fill;
        }
    }
}
