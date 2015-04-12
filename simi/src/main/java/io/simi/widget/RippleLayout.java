package io.simi.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;

/**
 * -------------------------------
 * 		   RippleLayout
 * -------------------------------
 *
 * createTime: 2015-04-12
 * updateTime: 2015-04-12
 *
 */
public class RippleLayout extends LinearLayout implements Runnable {

    private static final int INVALIDATE_DURATION = 80;

    private float mRippleCenterX;
    private float mRippleCenterY;
    private int mActiveViewWidth;
    private int mRippleRange;
    private int mRippleRadius;
    private int mRippleRadiusGap;
    private int mMaxRadius;
    private boolean mIsPressed;
    private boolean mShouldDoAnimation;
    private View mActiveView;

    private int[] mLocation = new int[2];
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private DispatchUpTouchEventRunnable mDispatchUpTouchEventRunnable = new DispatchUpTouchEventRunnable();

    public RippleLayout(Context context) {
        super(context);
        init();
    }

    public RippleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RippleLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        setWillNotDraw(false);
        mPaint.setColor(0x1B000000);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        this.getLocationOnScreen(mLocation);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        if (mActiveView == null || !mShouldDoAnimation || mActiveViewWidth <= 0)
            return;

        if (mRippleRadius > mRippleRange / 2)
            mRippleRadius += mRippleRadiusGap * 4;
        else
            mRippleRadius += mRippleRadiusGap;

        int[] location = new int[2];
        this.getLocationOnScreen(mLocation);
        mActiveView.getLocationOnScreen(location);

        int top = location[1] - mLocation[1];
        int left = location[0] - mLocation[0];
        int right = left + mActiveView.getMeasuredWidth();
        int bottom = top + mActiveView.getMeasuredHeight();

        canvas.save();
        canvas.clipRect(left, top, right, bottom);
        canvas.drawCircle(mRippleCenterX, mRippleCenterY, mRippleRadius, mPaint);
        canvas.restore();

        if (mRippleRadius <= mMaxRadius)
            postInvalidateDelayed(INVALIDATE_DURATION, left, top, right, bottom);
        else if (!mIsPressed) {
            mShouldDoAnimation = false;
            postInvalidateDelayed(INVALIDATE_DURATION, left, top, right, bottom);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int x = (int) event.getRawX();
        int y = (int) event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                View activeView = getActiveView(this, x, y);
                if (activeView != null && activeView.isEnabled()) {
                    mActiveView = activeView;
                    prepareRippleForActiveView(event, activeView);
                    postInvalidateDelayed(INVALIDATE_DURATION);
                }
                break;
            case MotionEvent.ACTION_UP:
                mIsPressed = false;
                postInvalidateDelayed(INVALIDATE_DURATION);
                mDispatchUpTouchEventRunnable.event = event;
                postDelayed(mDispatchUpTouchEventRunnable, 40);
                break;
            case MotionEvent.ACTION_CANCEL:
                mIsPressed = false;
                postInvalidateDelayed(INVALIDATE_DURATION);
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    public View getActiveView(View view, int x, int y) {
        ArrayList<View> children = view.getTouchables();
        for (View child : children){
            if (isTouchPointInView(child, x, y)) {
                return child;
            }
        }
        return null;
    }

    public boolean isTouchPointInView(View child, int x, int y) {
        int[] location = new int[2];
        child.getLocationOnScreen(location);

        int left = location[0];
        int top = location[1];
        int right = left + child.getMeasuredWidth();
        int bottom = top + child.getMeasuredHeight();

        return (child.isClickable() && y >= top && y <= bottom && x >= left && x <= right);
    }

    public void prepareRippleForActiveView(MotionEvent event, View view) {
        mRippleCenterX = event.getX();
        mRippleCenterY = event.getY();
        mActiveViewWidth = view.getMeasuredWidth();
        mRippleRange = Math.min(mActiveViewWidth, view.getMeasuredHeight());
        mRippleRadius = 0;
        mRippleRadiusGap = mRippleRange / 8;
        mIsPressed = true;
        mShouldDoAnimation = true;
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int mTransformCenterX = (int) mRippleCenterX - location[0] + mLocation[0];
        mMaxRadius = Math.max(mTransformCenterX, mActiveViewWidth - mTransformCenterX);
    }

    @Override
    public void run() {
        super.performClick();
    }

    @Override
    public boolean performClick() {
        postDelayed(this, INVALIDATE_DURATION);
        return true;
    }

    private class DispatchUpTouchEventRunnable implements Runnable {
        public MotionEvent event;

        @Override
        public void run() {
            if ((!mActiveView.isEnabled() || !mActiveView.isClickable()) && isTouchPointInView(mActiveView, (int) event.getRawX(), (int) event.getRawX())) {
                mActiveView.performClick();
            }
        }
    }
}
