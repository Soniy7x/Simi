package io.simi.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewGroup;

import io.simi.graphics.RectangleDrawable;
import io.simi.utils.AttributeParser;
import io.simi.utils.Unit;

/**
 * -------------------------------
 * 		  RectangleButton
 * -------------------------------
 *
 * createTime: 2015-04-16
 * updateTime: 2015-04-17
 *
 */
public class RectangleButton extends RippleView {

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int color = 0xFF1E88E5;
    private int size = 14;
    private String text;
    private Paint.FontMetrics fontMetrics;

    {
        paint.setColor(0xFFFFFFFF);
        paint.setFakeBoldText(true);
        paint.setTextAlign(Paint.Align.CENTER);
    }

    public RectangleButton(Context context) {
        this(context, null);
    }

    public RectangleButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RectangleButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (attrs != null) {
            text = AttributeParser.parserText(getContext(), attrs, "");
            size = AttributeParser.parseTextSize(attrs, size);
            AttributeParser.AttributeParserResult result = AttributeParser.parseBackground(attrs, color);
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
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setTextSize(int size) {
        this.size = size;
    }

    public int getBackgroundColor() {
        return color;
    }

    @Override
    public void setBackgroundColor(int color) {
        this.color = color;
        super.setBackgroundDrawable(new RectangleDrawable(getContext(), 2 , 2, color));
    }

    @Override
    public void setBackgroundResource(int resId) {}

    @Override
    public void setBackground(Drawable background) {}

    @Override
    public void setBackgroundDrawable(Drawable background) {}

    @Override
    protected float getShadowSize() {
        return Unit.dp2pxReturnFloat(getContext(), 2);
    }

    @Override
    protected float getRadiusSize() {
        return Unit.dp2pxReturnFloat(getContext(), 2);
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
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (TextUtils.isEmpty(text)) {
            return;
        }
        paint.setTextSize(Unit.dp2pxReturnFloat(getContext(), size));
        fontMetrics = paint.getFontMetrics();
        canvas.drawText(text, mWidth / 2, ((mHeight - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top), paint);
    }
}
