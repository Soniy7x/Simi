package io.simi.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import io.simi.graphics.RectangleDrawable;
import io.simi.utils.AttributeParser;

/**
 * -------------------------------
 * 		    CardView
 * -------------------------------
 *
 * createTime: 2015-04-21
 * updateTime: 2015-04-21
 *
 */
public class CardView extends RelativeLayout{

    public CardView(Context context) {
        this(context, null);
    }

    public CardView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (attrs != null) {
            AttributeParser.AttributeParserResult result = AttributeParser.parseBackground(AttributeParser.ANDROID_NAMESPACE, attrs, 0xFFFFFFFF);
            RectangleDrawable drawable = new RectangleDrawable(getContext(), 2, 2);
            if (result.type == AttributeParser.TYPE_ID) {
                try {
                    drawable.setColor(getResources().getColor(result.intValue));
                }catch (Exception e) {

                }
            }else {
                drawable.setColor(result.intValue);
            }
            setBackgroundDrawable(drawable);
        }
    }
}
