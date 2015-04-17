package io.simi.test;

import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;

public class AttributeParser {

    public static final String ANDROID_NAMESPACE = "http://schemas.android.com/apk/res/android";

    public static AttributeParserResult parseBackground(AttributeSet attrs, int defaultColor) {
        int resId = attrs.getAttributeResourceValue(Constant.ANDROID_NAMESPACE, "background", -1);
        if (resId != -1) {
            return new AttributeParserResult(1, resId);
        }else {
            int color = attrs.getAttributeIntValue(Constant.ANDROID_NAMESPACE, "background", -1);
            if (color != -1) {
                return new AttributeParserResult(2, color);
            }else {
                String colorString = attrs.getAttributeValue(Constant.ANDROID_NAMESPACE, "background");
                if (TextUtils.isEmpty(colorString)) {
                    return new AttributeParserResult(2, defaultColor);
                }else {
                    return new AttributeParserResult(2, Color.parseColor(colorString));
                }
            }
        }
    }

    public static class AttributeParserResult {
        public int type;
        public int intValue;
        public String strValue;

        public AttributeParserResult(int type, int value){
            this.type = type;
            this.intValue = value;
        }

        public AttributeParserResult(int type, String value){
            this.type = type;
            this.strValue = value;
        }
    }
}
