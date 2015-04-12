package io.simi.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver.OnPreDrawListener;

import io.simi.norm.FONT_AWESOME;

/**
 * -------------------------------
 * 			FontAwesome
 * -------------------------------
 *
 * createTime: 2015-04-12
 * updateTime: 2015-04-12
 *
 */
public class FontAwesome {

    private static final String TAG = "Simi - Font Awesome";

    private static FontAwesome instance;
    private static Typeface typeface;
    private static Context context;

    private FontAwesome(Context context){
        FontAwesome.context = context;
    }

    public static FontAwesome getInstance(Context context){
        if (instance == null) {
            instance = new FontAwesome(context);
            typeface = Typeface.createFromAsset(context.getAssets(), "fonts/fontawesome.ttf");
        }
        return instance;
    }

    public void setText(View view, FONT_AWESOME text){
        try {
            view.getClass().getMethod("setTypeface", Typeface.class).invoke(view, typeface);
            view.getClass().getMethod("setText", CharSequence.class).invoke(view, Html.fromHtml(text.toString()));
        } catch (Exception e) {
            Log.w(TAG, "此view不存在setText或setTypeface方法");
        }
    }

    public void setDrawable(View view, FONT_AWESOME icon){
        try {
            view.getClass().getMethod("setImageDrawable", Drawable.class).invoke(view, new FontAwesomeDrawable(context, view, icon));
        } catch (Exception e) {
            Log.w(TAG, "此view不存在setImageDrawable方法");
        }
    }

    public void setDrawable(View view, FONT_AWESOME icon, int color){
        try {
            view.getClass().getMethod("setImageDrawable", Drawable.class).invoke(view, new FontAwesomeDrawable(context, view, icon));
        } catch (Exception e) {
            Log.w(TAG, "此view不存在setImageDrawable方法");
        }
    }

    class FontAwesomeDrawable extends Drawable{

        private FontMetricsInt mFontMetrics;
        private TextPaint mTextPaint;
        private String text;
        private int width;
        private int height;

        public FontAwesomeDrawable(Context context, final View view, FONT_AWESOME text, int color) {
            this.text = Html.fromHtml(text.toString()).toString();
            mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
            mTextPaint.setTypeface(typeface);
            mTextPaint.setDither(true);
            mTextPaint.setColor(color);
            mTextPaint.setTextAlign(Paint.Align.CENTER);
            mTextPaint.measureText(text.toString());
            view.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    width = view.getWidth();
                    height = view.getHeight();
                    mTextPaint.setTextSize(Math.min(width, height));
                    view.getViewTreeObserver().removeOnPreDrawListener(this);
                    return false;
                }
            });
        }

        public FontAwesomeDrawable(Context context, final View view, FONT_AWESOME text) {
            this(context, view, text, Color.BLACK);
        }

        @Override
        public void draw(Canvas canvas) {
            mFontMetrics = mTextPaint.getFontMetricsInt();
            int baseline = (mFontMetrics.bottom - mFontMetrics.top + height) / 2 - mFontMetrics.bottom;
            canvas.drawText(text, width/2, baseline, mTextPaint);
        }

        @Override
        public void setAlpha(int alpha) {
            mTextPaint.setAlpha(alpha);
        }

        @Override
        public void setColorFilter(ColorFilter cf) {
            mTextPaint.setColorFilter(cf);
        }

        public void setColor(int color) {
            mTextPaint.setColor(color);
        }

        @Override
        public int getOpacity() {
            return 0;
        }

    }

}
