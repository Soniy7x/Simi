package io.simi.utils;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;

/**
 * -------------------------------
 * 		  AttributeParser
 * -------------------------------
 *
 * createTime: 2015-04-17
 * updateTime: 2015-04-20
 *
 */
public class AttributeParser {

    public static final String ANDROID_NAMESPACE = "http://schemas.android.com/apk/res/android";
    public static final String SIMI_NAMESPACE = "http://schemas.android.com/simi";
    public static final int FAILED = -1;
    public static final int TYPE_ID = 1;
    public static final int TYPE_VALUE = 2;

    public static int parserRadius(String namespace, AttributeSet attrs) {
        return attrs.getAttributeIntValue(namespace, "radius", 0);
    }

    public static boolean parserCircleMode(String namespace, AttributeSet attrs) {
        return attrs.getAttributeBooleanValue(namespace, "circleMode", false);
    }

    public static int parserSrc(String namespace, AttributeSet attrs) {
        int resId = attrs.getAttributeResourceValue(namespace, "src", FAILED);
        if (resId != FAILED) {
            return resId;
        }
        return -1;
    }

    public static String parserType(Context context, String namespace, AttributeSet attrs, String defaultText) {
        int resId = attrs.getAttributeResourceValue(namespace, "type", FAILED);
        if (resId != FAILED) {
            return context.getResources().getString(resId);
        }else {
            String text = attrs.getAttributeValue(namespace, "type");
            if (TextUtils.isEmpty(text)) {
                return defaultText;
            } else {
                return text;
            }
        }
    }

    public static int parseNavigatorColor(Context context, String namespace, AttributeSet attrs, int defaultColor) {
        int resId = attrs.getAttributeResourceValue(namespace, "navigatorColor", FAILED);
        if (resId != FAILED) {
            try {
                return  context.getResources().getColor(resId);
            }catch (Exception e) {
                return defaultColor;
            }
        }else {
            int color = attrs.getAttributeIntValue(namespace, "navigatorColor", FAILED);
            if (color != FAILED) {
                return color;
            }else {
                String colorString = attrs.getAttributeValue(namespace, "navigatorColor");
                if (TextUtils.isEmpty(colorString)) {
                    return defaultColor;
                }else {
                    return Color.parseColor(colorString);
                }
            }
        }
    }

    public static int parseContentColor(Context context, String namespace, AttributeSet attrs, int defaultColor) {
        int resId = attrs.getAttributeResourceValue(namespace, "contentColor", FAILED);
        if (resId != FAILED) {
            try {
                return  context.getResources().getColor(resId);
            }catch (Exception e) {
                return defaultColor;
            }
        }else {
            int color = attrs.getAttributeIntValue(namespace, "contentColor", FAILED);
            if (color != FAILED) {
                return color;
            }else {
                String colorString = attrs.getAttributeValue(namespace, "contentColor");
                if (TextUtils.isEmpty(colorString)) {
                    return defaultColor;
                }else {
                    return Color.parseColor(colorString);
                }
            }
        }
    }

    public static int parseTextColor(Context context, String namespace, AttributeSet attrs, int defaultColor) {
        int resId = attrs.getAttributeResourceValue(namespace, "textColor", FAILED);
        if (resId != FAILED) {
            try {
                return  context.getResources().getColor(resId);
            }catch (Exception e) {
                return defaultColor;
            }
        }else {
            int color = attrs.getAttributeIntValue(namespace, "textColor", FAILED);
            if (color != FAILED) {
                return color;
            }else {
                String colorString = attrs.getAttributeValue(namespace, "textColor");
                if (TextUtils.isEmpty(colorString)) {
                    return defaultColor;
                }else {
                    return Color.parseColor(colorString);
                }
            }
        }
    }

    public static int parseTextSelectColor(Context context, String namespace, AttributeSet attrs, int defaultColor) {
        int resId = attrs.getAttributeResourceValue(namespace, "textSelectColor", FAILED);
        if (resId != FAILED) {
            try {
                return  context.getResources().getColor(resId);
            }catch (Exception e) {
                return defaultColor;
            }
        }else {
            int color = attrs.getAttributeIntValue(namespace, "textSelectColor", FAILED);
            if (color != FAILED) {
                return color;
            }else {
                String colorString = attrs.getAttributeValue(namespace, "textSelectColor");
                if (TextUtils.isEmpty(colorString)) {
                    return defaultColor;
                }else {
                    return Color.parseColor(colorString);
                }
            }
        }
    }

    public static int parseIndicatorColor(Context context, String namespace, AttributeSet attrs, int defaultColor) {
        int resId = attrs.getAttributeResourceValue(namespace, "indicatorColor", FAILED);
        if (resId != FAILED) {
            try {
                return  context.getResources().getColor(resId);
            }catch (Exception e) {
                return defaultColor;
            }
        }else {
            int color = attrs.getAttributeIntValue(namespace, "indicatorColor", FAILED);
            if (color != FAILED) {
                return color;
            }else {
                String colorString = attrs.getAttributeValue(namespace, "indicatorColor");
                if (TextUtils.isEmpty(colorString)) {
                    return defaultColor;
                }else {
                    return Color.parseColor(colorString);
                }
            }
        }
    }

    public static int parseUnderLineColor(Context context, String namespace, AttributeSet attrs, int defaultColor) {
        int resId = attrs.getAttributeResourceValue(namespace, "underLineColor", FAILED);
        if (resId != FAILED) {
            try {
                return  context.getResources().getColor(resId);
            }catch (Exception e) {
                return defaultColor;
            }
        }else {
            int color = attrs.getAttributeIntValue(namespace, "underLineColor", FAILED);
            if (color != FAILED) {
                return color;
            }else {
                String colorString = attrs.getAttributeValue(namespace, "underLineColor");
                if (TextUtils.isEmpty(colorString)) {
                    return defaultColor;
                }else {
                    return Color.parseColor(colorString);
                }
            }
        }
    }

    public static String parserText(Context context, String namespace, AttributeSet attrs, String defaultText) {
        int resId = attrs.getAttributeResourceValue(namespace, "text", FAILED);
        if (resId != FAILED) {
            return context.getResources().getString(resId);
        }else {
            String text = attrs.getAttributeValue(namespace, "text");
            if (TextUtils.isEmpty(text)) {
                return defaultText;
            } else {
                return text;
            }
        }
    }

    public static int parseTextSize(String namespace, AttributeSet attrs, int defaultTextSize) {
        String textSize = attrs.getAttributeValue(namespace, "textSize");
        if (TextUtils.isEmpty(textSize) || !textSize.contains("sp")) {
            return defaultTextSize;
        }
        return Float.valueOf(textSize.replace("sp", "").trim()).intValue();
    }

    public static AttributeParserResult parseBackground(String namespace, AttributeSet attrs, int defaultColor) {
        int resId = attrs.getAttributeResourceValue(namespace, "background", FAILED);
        if (resId != FAILED) {
            return new AttributeParserResult(TYPE_ID, resId);
        }else {
            int color = attrs.getAttributeIntValue(namespace, "background", FAILED);
            if (color != FAILED) {
                return new AttributeParserResult(TYPE_VALUE, color);
            }else {
                String colorString = attrs.getAttributeValue(namespace, "background");
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
