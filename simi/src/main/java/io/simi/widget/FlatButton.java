package io.simi.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;

import io.simi.graphics.RectangleDrawable;
import io.simi.utils.AttributeParser;
import io.simi.utils.Unit;

/**
 * -------------------------------
 * 		   FlatButton
 * -------------------------------
 *
 * createTime: 2015-04-15
 * updateTime: 2015-04-20
 *
 */
public class FlatButton extends RippleView {

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private String text;
    private int textSize;
    private int color = 0xFF1E88E5;
    private Paint.FontMetrics fontMetrics;

    {
        paint.setColor(color);
        paint.setFakeBoldText(true);
        paint.setTextAlign(Paint.Align.CENTER);
    }

    public FlatButton(Context context) {
        this(context, null);
    }

    public FlatButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlatButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (attrs != null) {
            text = AttributeParser.parserText(getContext(), AttributeParser.ANDROID_NAMESPACE, attrs, "");
            textSize = AttributeParser.parseTextSize(AttributeParser.ANDROID_NAMESPACE, attrs, 14);
            AttributeParser.AttributeParserResult result = AttributeParser.parseBackground(AttributeParser.ANDROID_NAMESPACE, attrs, color);
            if (result.type == AttributeParser.TYPE_ID) {
                try {
                    setBackgroundColor(getResources().getColor(result.intValue));
                }catch (Exception e) {
                    setBackgroundColor(color);
                }
            }else {
                setBackgroundColor(result.intValue);
            }
        }else {
            setBackgroundColor(color);
        }
        Drawable background = new RectangleDrawable(getContext(), 2);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            super.setBackground(background);
        }else {
            super.setBackgroundDrawable(background);
        }
    }

    @Override
    protected float getRadiusSize() {
        return Unit.dp2pxReturnFloat(getContext(), 2);
    }

    @Override
    public void setBackgroundColor(int color) {
        this.color = color;
        invalidate();
    }

    public void setText(String text) {
        this.text = text;
        invalidate();
    }

    public String getText() {
        return text;
    }

    public int getBackgroundColor() {
        return color;
    }

    public void setTextSize(int size) {
        this.textSize = size;
        invalidate();
    }

    @Override
    public void setLayoutParams(ViewGroup.LayoutParams params) {
        if (params.width == -2) {
            params.width = Unit.dp2px(getContext(), 96);
        }
        if (params.height == -2) {
            params.height = Unit.dp2px(getContext(), 48);
        }
        super.setLayoutParams(params);
    }

    @Override
    public void setBackground(Drawable background) {}

    @Override
    public void setBackgroundDrawable(Drawable background) {}

    @Override
    public void setBackgroundResource(int resId) {}

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (TextUtils.isEmpty(text)) {
            return;
        }
        paint.setColor(color);
        paint.setTextSize(Unit.dp2pxReturnFloat(getContext(), textSize));
        fontMetrics = paint.getFontMetrics();
        canvas.drawText(text, mWidth / 2, ((mHeight - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top), paint);
    }
}
