package io.simi.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.simi.graphics.Image;
import io.simi.utils.Constant;
import io.simi.utils.Unit;

/**
 * -------------------------------
 * 		   FlatButton
 * -------------------------------
 *
 * createTime: 2015-04-15
 * updateTime: 2015-04-15
 *
 */
public class FlatButton extends RippleLayout {

    private String text;
    private TextView textView;
    private int color = 0xFF1E88E5;
    private OnClickListener onClickListener;

    public FlatButton(Context context) {
        this(context, null);
    }

    public FlatButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlatButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        textView = new TextView(getContext());
        textView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        textView.setGravity(Gravity.CENTER);
        textView.getPaint().setFakeBoldText(true);
        addView(textView);
        if (attrs != null) {
            int textResource = attrs.getAttributeResourceValue(Constant.ANDROID_NAMESPACE, "text", -1);
            text = textResource != -1 ? getResources().getString(textResource) : attrs.getAttributeValue(Constant.ANDROID_NAMESPACE, "text");
            textView.setText(text == null ? "" : text);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, attrs.getAttributeIntValue(Constant.ANDROID_NAMESPACE, "textSize", 16));
            int backgroundResource = attrs.getAttributeResourceValue(Constant.ANDROID_NAMESPACE, "background", -1);
            if (backgroundResource != -1) {
                setBackgroundColor(getResources().getColor(backgroundResource));
            }else {
                int backgroundColor = attrs.getAttributeIntValue(Constant.ANDROID_NAMESPACE, "background", -1);
                if (backgroundColor != -1) {
                    setBackgroundColor(backgroundColor);
                }else {
                    textView.setTextColor(color);
                }
            }
        }
        super.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener != null) {
                    onClickListener.onClick(v);
                }
            }
        });
        Drawable background = Image.createDrawable(0x00000000, 8);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            super.setBackground(background);
        }else {
            super.setBackgroundDrawable(background);
        }
    }

    @Override
    public void setOnClickListener(OnClickListener listener) {
        onClickListener = listener;
    }

    @Override
    public void setBackgroundColor(int color) {
        this.color = color;
        textView.setTextColor(color);
    }

    public void setText(String text) {
        if (text != null) {
            this.text = text;
            textView.setText(this.text);
        }
    }

    public String getText() {
        return text;
    }

    public int getBackgroundColor() {
        return color;
    }

    public void setTextSize(int size) {
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    public TextView getTextView() {
        return  textView;
    }

    @Override
    public void setLayoutParams(ViewGroup.LayoutParams params) {
        int paddingHorizontal = Unit.dp2px(getContext(), 32);
        int paddingVertical = Unit.dp2px(getContext(), 16);
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        if (params.width == -2) {
            paddingLeft = paddingLeft == 0 ? paddingHorizontal : paddingLeft;
            paddingRight = paddingRight == 0 ? paddingHorizontal : paddingRight;
        }
        if (params.height == -2) {
            paddingTop = paddingTop == 0 ? paddingVertical : paddingTop;
            paddingBottom = paddingBottom == 0 ? paddingVertical : paddingBottom;
        }
        super.setLayoutParams(params);
        setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
    }

    @Override
    public void setBackground(Drawable background) {}

    @Override
    public void setBackgroundDrawable(Drawable background) {}

    @Override
    public void setBackgroundResource(int resId) {}
}
