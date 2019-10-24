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
public class FlowLayout extends android.widget.FrameLayout {

    private int gravity;

    public FlowLayout(Context context) {
        super(context, null, R.attr.guide_flowLayoutStyle);
        initFlowLayout(null, R.attr.guide_flowLayoutStyle);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs, R.attr.guide_flowLayoutStyle);
        initFlowLayout(attrs, R.attr.guide_flowLayoutStyle);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initFlowLayout(attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initFlowLayout(attrs, defStyleAttr);
    }

    private void initFlowLayout(AttributeSet attrs, int defStyleAttr) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.FlowLayout, defStyleAttr, 0);

        gravity = a.getInt(R.styleable.FlowLayout_android_gravity, Gravity.START);

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

    private int layoutFlowingViews(int width) {
        int gravity = GravityCompat.getAbsoluteGravity(this.gravity, ViewCompat.getLayoutDirection(this));
        if ((gravity & Gravity.RIGHT) == Gravity.RIGHT) {
            return layoutFlowingViewsRight(width);
        } else {
            return layoutFlowingViewsLeft(width);
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

    private int layoutFlowingViewsRight(int width) {
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
                child.layout(currentX - params.rightMargin - child.getMeasuredWidth(), currentY + params.topMargin, currentX - params.rightMargin, currentY + params.topMargin + child.getMeasuredHeight());
                currentX -= params.leftMargin + child.getMeasuredWidth() + params.rightMargin;
                nextY = Math.max(nextY, currentY + params.topMargin + child.getMeasuredHeight() + params.bottomMargin);
            }
        }
        relayoutLine(currentLine);
        return nextY + getPaddingBottom();
    }

    private int layoutFlowingViewsLeft(int width) {
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
                child.layout(currentX + params.leftMargin, currentY + params.topMargin, currentX + params.leftMargin + child.getMeasuredWidth(), currentY + params.topMargin + child.getMeasuredHeight());
                currentX += params.leftMargin + child.getMeasuredWidth() + params.rightMargin;
                nextY = Math.max(nextY, currentY + params.topMargin + child.getMeasuredHeight() + params.bottomMargin);
            }
        }
        relayoutLine(currentLine);
        return nextY + getPaddingBottom();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        layoutFlowingViews(getWidth());
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
        private RuntimeException delayedException;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
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
        public LayoutParams(ViewGroup.MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(android.widget.FrameLayout.LayoutParams source) {
            super((MarginLayoutParams) source);
            gravity = source.gravity;
        }

        public LayoutParams(LayoutParams source) {
            super((MarginLayoutParams) source);
        }

        @Override
        protected void setBaseAttributes(TypedArray a, int widthAttr, int heightAttr) {
            try {
                super.setBaseAttributes(a, widthAttr, heightAttr);
            } catch (RuntimeException e) {
                delayedException = e;
            }
        }
    }
}
