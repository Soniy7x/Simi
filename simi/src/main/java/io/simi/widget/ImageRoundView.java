package io.simi.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import io.simi.utils.AttributeParser;
import io.simi.utils.Unit;

/**
 * -------------------------------
 * 		  ImageRoundView
 * -------------------------------
 *
 * createTime: 2015-04-20
 * updateTime: 2015-04-20
 *
 */
public class ImageRoundView extends View {

    private Bitmap bitmap;

    private int radius = 0;

    private boolean isCircleMode = false;
    private boolean isXfermode = false;
    private Path path = new Path();
    private Rect mRect = new Rect();

    private Paint mPaint;
    private PorterDuffXfermode mXfermode;
    private PaintFlagsDrawFilter mDrawFilter;

    {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mXfermode = new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY);
        mDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);
    }

    public ImageRoundView(Context context) {
        this(context, null);
    }

    public ImageRoundView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageRoundView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
        if (attrs == null) {
            return;
        }
        int resId = AttributeParser.parserSrc(AttributeParser.ANDROID_NAMESPACE, attrs);
        if (resId == -1) {
            return;
        }
        isCircleMode = AttributeParser.parserCircleMode(AttributeParser.SIMI_NAMESPACE, attrs);
        if (!isCircleMode) {
            radius = AttributeParser.parserRadius(AttributeParser.SIMI_NAMESPACE, attrs);
        }
        setImageBitmap(BitmapFactory.decodeResource(getResources(), resId));
    }

    public void setImageBitmap(Bitmap bitmap){
        this.bitmap = bitmap;
        try {
            setLayerType(LAYER_TYPE_SOFTWARE, null);
        } catch (Exception e) {
            isXfermode = true;
        }
        setCircleMode(true);
    }

    public void setImageResource(int resId) {
        setImageBitmap(BitmapFactory.decodeResource(getResources(), resId));
    }

    public void setImageDrawable(Drawable drawable) {
        setImageBitmap(((BitmapDrawable)drawable).getBitmap());
    }

    public void setCircleMode(boolean result) {
        this.isCircleMode = result;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public Drawable getDrawable() {
        return new BitmapDrawable(bitmap);
    }

    public int getRadius() {
        return radius;
    }

    public boolean isCircleMode() {
        return isCircleMode;
    }

    private Bitmap makeDst(int w, int h){
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(0xFFFFFFFF);
        if (isCircleMode) {
            canvas.drawOval(new RectF(0, 0, w, h), paint);
        }else {
            canvas.drawRoundRect(new RectF(0, 0, w, h), radius, radius, paint);
        }
        return bitmap;
    }

    @Override
    public void setLayoutParams(ViewGroup.LayoutParams params) {
        if (params.width == -2 || params.height == -2) {
            params.width = params.height = Unit.dp2px(getContext(), 72);
        }else {
            params.width = params.height = Math.min(params.width, params.height);
        }
        super.setLayoutParams(params);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (bitmap == null) {
            return;
        }
        if (isXfermode) {
            mRect.set(0, 0, getWidth(), getHeight());
            canvas.save();
            canvas.setDrawFilter(mDrawFilter);
            path.reset();
            canvas.clipPath(path);
            if (isCircleMode) {
                path.addCircle(getWidth() / 2, getWidth() / 2, getHeight() / 2, Path.Direction.CCW);
            }else {
                path.addRoundRect(new RectF(mRect), new float[]{4F, 4F}, Path.Direction.CCW);
            }
            canvas.clipPath(path, Region.Op.REPLACE);
            canvas.drawBitmap(bitmap, null, mRect, mPaint);
            canvas.restore();
        }else {
            mRect.set(0, 0, getWidth(), getHeight());
            canvas.save();
            canvas.setDrawFilter(mDrawFilter);
            canvas.drawBitmap(makeDst(getWidth(), getHeight()), 0, 0, mPaint);
            mPaint.setXfermode(mXfermode);
            canvas.drawBitmap(bitmap, null, mRect, mPaint);
            mPaint.setXfermode(null);
            canvas.restore();
        }
    }
}
