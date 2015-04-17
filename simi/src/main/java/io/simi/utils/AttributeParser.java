package io.simi.utils;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;

/**
 * -------------------------------
 * 		  AttributeParser
 * -------------------------------
 *
 * createTime: 2015-04-17
 * updateTime: 2015-04-17
 *
 */
public class AttributeParser {

    public static final String ANDROID_NAMESPACE = "http://schemas.android.com/apk/res/android";
    public static final int FAILED = -1;
    public static final int TYPE_ID = 1;
    public static final int TYPE_VALUE = 2;

    public static String parserText(Context context, AttributeSet attrs, String defaultText) {
        int resId = attrs.getAttributeResourceValue(ANDROID_NAMESPACE, "text", FAILED);
        if (resId != FAILED) {
            return context.getResources().getString(resId);
        }else {
            String text = attrs.getAttributeValue(ANDROID_NAMESPACE, "text");
            if (TextUtils.isEmpty(text)) {
                return defaultText;
            } else {
                return text;
            }
        }
    }

    public static int parseTextSize(AttributeSet attrs, int defaultTextSize) {
        return attrs.getAttributeIntValue(ANDROID_NAMESPACE, "textSize", defaultTextSize);
    }

    public static AttributeParserResult parseBackground(AttributeSet attrs, int defaultColor) {
        int resId = attrs.getAttributeResourceValue(ANDROID_NAMESPACE, "background", FAILED);
        if (resId != FAILED) {
            return new AttributeParserResult(TYPE_ID, resId);
        }else {
            int color = attrs.getAttributeIntValue(ANDROID_NAMESPACE, "background", FAILED);
            if (color != FAILED) {
                return new AttributeParserResult(TYPE_VALUE, color);
            }else {
                String colorString = attrs.getAttributeValue(ANDROID_NAMESPACE, "background");
                if (TextUtils.isEmpty(colorString)) {
                    return new AttributeParserResult(TYPE_VALUE, defaultColor);
                }else {
                    return new AttributeParserResult(TYPE_VALUE, Color.parseColor(colorString));
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
