package io.simi.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewGroup;

import io.simi.graphics.Image;
import io.simi.utils.Constant;
import io.simi.utils.Unit;

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
            int textResource = attrs.getAttributeResourceValue(Constant.ANDROID_NAMESPACE, "text", -1);
            text = textResource != -1 ? getResources().getString(textResource) : attrs.getAttributeValue(Constant.ANDROID_NAMESPACE, "text");
            size = attrs.getAttributeIntValue(Constant.ANDROID_NAMESPACE, "textSize", 14);
            int backgroundResource = attrs.getAttributeResourceValue(Constant.ANDROID_NAMESPACE, "background", -1);
            if (backgroundResource != -1) {
                setBackgroundColor(getResources().getColor(backgroundResource));
            }else {
                int backgroundColor = attrs.getAttributeIntValue(Constant.ANDROID_NAMESPACE, "background", -1);
                if (backgroundColor != -1) {
                    setBackgroundColor(backgroundColor);
                }else {
                    setBackgroundColor(color);
                }
            }
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
        super.setBackgroundDrawable(Image.createDrawable(color, 8));
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
        canvas.drawText(text, width / 2, ((height - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top), paint);
    }
}
