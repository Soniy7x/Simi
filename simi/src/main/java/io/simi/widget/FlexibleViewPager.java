package io.simi.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class FlexibleViewPager extends ViewPager {

    private boolean isFlexible = true;

    public FlexibleViewPager(Context context) {
        super(context);
    }

    public FlexibleViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setFlexible(boolean isFlexible) {
        this.isFlexible = isFlexible;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return isFlexible && super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return isFlexible && super.onInterceptTouchEvent(ev);
    }
}
