package io.simi.graphics;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import io.simi.utils.Unit;

/**
 * -------------------------------
 * 			RectangleDrawable
 * -------------------------------
 *
 * createTime: 2015-04-15
 * updateTime: 2015-04-17
 *
 */
public class RectangleDrawable extends Drawable{

    private final static float SHADOW_MULTIPLIER = 1.5f;
    private final static int DEFAULT_BACKGROUND_COLOR = 0xFFFFFFFF;

    private final RectF mBounds = new RectF();
    private final RectF mRectBounds = new RectF();

    private int mShadowStartColor = 0x37000000;
    private int mShadowEndColor = 0x07000000;
    private float mRadius = 0F;
    private int mShadowInsetSize = 1;
    private float mShadowOutsetSize = 0F;
    private float mShadowLastOutsetSize = -1F;
    private float mVerticalOffset;
    private Path mShadowPath;
    private Paint mRectPaint;
    private Paint mCornerShadowPaint;
    private Paint mEdgeShadowPaint;

    public RectangleDrawable(Context context) {
        this(context, 0);
    }

    public RectangleDrawable(Context context, float radius) {
        this(context, radius, 0);
    }

    public RectangleDrawable(Context context, float radius, float shadowSize) {
        this(context, radius, shadowSize, 0);
    }

    public RectangleDrawable(Context context, float radius, float shadowSize, int backgroundColor) {
        DisplayMetrics mDisplayMetrics = context.getResources().getDisplayMetrics();
        this.mRadius = Unit.dp2pxReturnFloat(context, (int) (radius + .5f));
        this.mShadowOutsetSize = Unit.dp2pxReturnFloat(context, shadowSize);
        this.mShadowInsetSize  = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mShadowInsetSize, mDisplayMetrics);
        this.mRectPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        this.mRectPaint.setColor(backgroundColor == 0 ? DEFAULT_BACKGROUND_COLOR : backgroundColor);
        this.mCornerShadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        this.mCornerShadowPaint.setStyle(Paint.Style.FILL);
        this.mEdgeShadowPaint = new Paint(mCornerShadowPaint);
        this.mEdgeShadowPaint.setAntiAlias(false);
        resetShadowOutsetSize();
    }

    private void resetShadowOutsetSize() {
        if (mShadowLastOutsetSize == mShadowOutsetSize || (int)mShadowOutsetSize <= 0) {
            return;
        }
        mShadowLastOutsetSize = mShadowOutsetSize;
        mVerticalOffset = mShadowLastOutsetSize * SHADOW_MULTIPLIER;
        mShadowOutsetSize = (int)(mShadowOutsetSize * SHADOW_MULTIPLIER + mShadowInsetSize + 0.5F);
    }

    @Override
    public void draw(Canvas canvas) {
        Rect bounds = getBounds();
        if (mRadius > 0) {
            RectF innerBounds = new RectF(-mRadius, -mRadius, mRadius, mRadius);
            RectF outerBounds = new RectF(innerBounds);
            outerBounds.inset(-mShadowOutsetSize, -mShadowOutsetSize);
            mBounds.set(bounds.left + mShadowLastOutsetSize, bounds.top + mVerticalOffset, bounds.right - mShadowLastOutsetSize, bounds.bottom - mVerticalOffset);
            if (mShadowPath == null) {
                mShadowPath = new Path();
            } else {
                mShadowPath.reset();
            }
            mShadowPath.setFillType(Path.FillType.EVEN_ODD);
            mShadowPath.moveTo(-mRadius, 0);
            mShadowPath.rLineTo(-mShadowOutsetSize, 0);
            mShadowPath.arcTo(outerBounds, 180f, 90f, false);
            mShadowPath.arcTo(innerBounds, 270f, -90f, false);
            mShadowPath.close();
            float startRatio = mRadius / (mRadius + mShadowOutsetSize);
            mCornerShadowPaint.setShader(new RadialGradient(0, 0, mRadius + mShadowOutsetSize, new int[]{mShadowStartColor, mShadowStartColor, mShadowEndColor}, new float[]{0f, startRatio, 1f}, Shader.TileMode.CLAMP));
            mEdgeShadowPaint.setShader(new LinearGradient(0, -mRadius + mShadowOutsetSize, 0, -mRadius - mShadowOutsetSize, new int[]{mShadowStartColor, mShadowStartColor, mShadowEndColor}, new float[]{0f, .5f, 1f}, Shader.TileMode.CLAMP));
        }else {
            mBounds.set(bounds);
        }
        canvas.translate(0, mShadowLastOutsetSize / 2);
        if ((int)mShadowOutsetSize > 0) {
            final float edgeShadowTop = -mRadius - mShadowOutsetSize;
            final float inset = mRadius + mShadowInsetSize + mShadowLastOutsetSize / 2;
            final boolean drawHorizontalEdges = mBounds.width() - 2 * inset > 0;
            final boolean drawVerticalEdges = mBounds.height() - 2 * inset > 0;
            canvas.save();
            canvas.translate(mBounds.left + inset, mBounds.top + inset);
            canvas.drawPath(mShadowPath, mCornerShadowPaint);
            if (drawHorizontalEdges) {
                canvas.drawRect(0, edgeShadowTop, mBounds.width() - 2 * inset, -mRadius, mEdgeShadowPaint);
            }
            canvas.restore();
            canvas.save();
            canvas.translate(mBounds.right - inset, mBounds.bottom - inset);
            canvas.rotate(180f);
            canvas.drawPath(mShadowPath, mCornerShadowPaint);
            if (drawHorizontalEdges) {
                canvas.drawRect(0, edgeShadowTop, mBounds.width() - 2 * inset, -mRadius, mEdgeShadowPaint);
            }
            canvas.restore();
            canvas.save();
            canvas.translate(mBounds.left + inset, mBounds.bottom - inset);
            canvas.rotate(270f);
            canvas.drawPath(mShadowPath, mCornerShadowPaint);
            if (drawVerticalEdges) {
                canvas.drawRect(0, edgeShadowTop, mBounds.height() - 2 * inset, -mRadius, mEdgeShadowPaint);
            }
            canvas.restore();
            canvas.save();
            canvas.translate(mBounds.right - inset, mBounds.top + inset);
            canvas.rotate(90f);
            canvas.drawPath(mShadowPath, mCornerShadowPaint);
            if (drawVerticalEdges) {
                canvas.drawRect(0, edgeShadowTop, mBounds.height() - 2 * inset, -mRadius, mEdgeShadowPaint);
            }
            canvas.restore();
        }
        canvas.translate(0, -mShadowLastOutsetSize / 2);
        final float twoRadius = mRadius * 2;
        final float innerWidth = mBounds.width() - twoRadius - 1;
        final float innerHeight = mBounds.height() - twoRadius - 1;
        if (mRadius >= 1f) {
            mRadius += .5f;
            mRectBounds.set(-mRadius, -mRadius, mRadius, mRadius);
            canvas.save();
            canvas.translate(mBounds.left + mRadius, mBounds.top + mRadius);
            canvas.drawArc(mRectBounds, 180, 90, true, mRectPaint);
            canvas.translate(innerWidth, 0);
            canvas.rotate(90);
            canvas.drawArc(mRectBounds, 180, 90, true, mRectPaint);
            canvas.translate(innerHeight, 0);
            canvas.rotate(90);
            canvas.drawArc(mRectBounds, 180, 90, true, mRectPaint);
            canvas.translate(innerWidth, 0);
            canvas.rotate(90);
            canvas.drawArc(mRectBounds, 180, 90, true, mRectPaint);
            canvas.restore();
            canvas.drawRect(mBounds.left + mRadius - 1f, mBounds.top, mBounds.right - mRadius + 1f, mBounds.top + mRadius, mRectPaint);
            canvas.drawRect(mBounds.left + mRadius - 1f, mBounds.bottom - mRadius + 1f, mBounds.right - mRadius + 1f, mBounds.bottom, mRectPaint);
            mRadius -= .5f;
        }
        canvas.drawRect(mBounds.left, mBounds.top + Math.max(0, mRadius), mBounds.right, mBounds.bottom - mRadius, mRectPaint);
    }
    
    public void setColor(int color) {
        mRectPaint.setColor(color);
    }

    public void setRadius(float radius) {
        this.mRadius = radius;
    }

    public void setShadowSize(float size) {
        this.mShadowOutsetSize = size;
        resetShadowOutsetSize();
    }

    @Override
    public void setAlpha(int alpha) {
        mRectPaint.setAlpha(alpha);
        mCornerShadowPaint.setAlpha(alpha);
        mEdgeShadowPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        mRectPaint.setColorFilter(colorFilter);
        mCornerShadowPaint.setColorFilter(colorFilter);
        mEdgeShadowPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
