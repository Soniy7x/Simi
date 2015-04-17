package io.simi.test;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class RippleView extends View implements Runnable{

    private static final int INVALIDATE_DURATION = 40;

    private float mRippleCenterX;
    private float mRippleCenterY;
    private float mRippleRange;
    private float mRippleRadius;
    private float mRippleRadiusGap;
    private float mRippleRadiusMax;

    private float mShadowSize;
    private float mRadiusSize;
    private RectF mStrokeRect;
    private Path mStrokePath = new Path();

    protected float mWidth;
    protected float mHeight;

    private boolean isAnimated = false;
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private DispatchUpTouchEventRunnable mDispatchUpTouchEventRunnable = new DispatchUpTouchEventRunnable();

    {
        mPaint.setColor(0x1B000000);
    }

    public RippleView(Context context) {
        this(context, null);
    }

    public RippleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RippleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
    }

    protected float getShadowSize() {
        return 0;
    }

    protected float getRadiusSize() {
        return 0;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldWidth, int oldHeight) {
        super.onSizeChanged(w, h, oldWidth, oldHeight);
        mWidth = w;
        mHeight = h;
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

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return super.dispatchTouchEvent(event);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isAnimated = true;
                mRippleCenterX = event.getX();
                mRippleCenterY = event.getY();
                mRippleRange = Math.min(mWidth, getMeasuredHeight());
                mRippleRadius = 0;
                mRippleRadiusGap = mRippleRange / 8;
                if(mWidth > mHeight) {
                    if (mRippleCenterX < mWidth / 2) {
                        mRippleRadiusMax = mWidth - mRippleCenterX;
                    }else {
                        mRippleRadiusMax = mWidth / 2 + mRippleCenterX;
                    }
                }else {
                    if (mRippleCenterY < mHeight / 2) {
                        mRippleRadiusMax = mHeight - mRippleCenterY;
                    }else {
                        mRippleRadiusMax = mHeight / 2 + mRippleCenterY;
                    }
                }
                postInvalidateDelayed(INVALIDATE_DURATION);
                break;
            case MotionEvent.ACTION_UP:
                mDispatchUpTouchEventRunnable.event = event;
                postDelayed(mDispatchUpTouchEventRunnable, 40);
            case MotionEvent.ACTION_CANCEL:
                postInvalidateDelayed(INVALIDATE_DURATION);
                break;
        }
        return super.dispatchTouchEvent(event);
    }
    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (!isAnimated) {
            return;
        }
        if (mRippleRadius > mRippleRange / 2) {
            mRippleRadius += mRippleRadiusGap * 4;
        }else {
            mRippleRadius += mRippleRadiusGap;
        }
        mShadowSize = getShadowSize();
        mRadiusSize = getRadiusSize();
        float left = mShadowSize;
        float top = mShadowSize * 1.5F;
        float right = mWidth - mShadowSize;
        float bottom = mHeight - mShadowSize * 1.5F;
        mStrokePath.moveTo(mShadowSize + mRadiusSize, top);
        mStrokePath.lineTo(right - mRadiusSize, top);
        mStrokeRect = new RectF(right - mRadiusSize, top, right, mRadiusSize + top);
        mStrokePath.arcTo(mStrokeRect, 270, 90, false);
        mStrokePath.lineTo(right, bottom - mRadiusSize);
        mStrokeRect = new RectF(right - mRadiusSize, bottom - mRadiusSize, right, bottom);
        mStrokePath.arcTo(mStrokeRect, 0, 90, false);
        mStrokePath.lineTo(left + mRadiusSize, bottom);
        mStrokeRect = new RectF(left, bottom - mRadiusSize, left +mRadiusSize, bottom);
        mStrokePath.arcTo(mStrokeRect, 90, 90, false);
        mStrokePath.lineTo(left, top + mRadiusSize);
        mStrokeRect = new RectF(left, top, mRadiusSize + left,  mRadiusSize + top);
        mStrokePath.arcTo(mStrokeRect, 180, 90, false);
        canvas.save();
        canvas.clipPath(mStrokePath);
        //getInsetPadding(), getInsetPadding(), mWidth - getInsetPadding(), mHeight - getInsetPadding()
        canvas.drawCircle(mRippleCenterX, mRippleCenterY, mRippleRadius, mPaint);
        canvas.restore();
        if (mRippleRadius > mRippleRadiusMax) {
            isAnimated = false;
        }
        postInvalidateDelayed(INVALIDATE_DURATION, 0, 0, (int)mWidth, (int)mHeight);
    }

    private class DispatchUpTouchEventRunnable implements Runnable {
        public MotionEvent event;
        @Override
        public void run() {
            if (!isEnabled()) {
                performClick();
            }
        }
    }
}
