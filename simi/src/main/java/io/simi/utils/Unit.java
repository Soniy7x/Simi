package io.simi.utils;

import android.content.Context;
import android.view.View;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * -------------------------------
 * 		        Unit
 * -------------------------------
 *
 * createTime: 2015-04-12
 * updateTime: 2015-04-12
 *
 */
public final class Unit {

    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

    public static int dp2px(Context context, float dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density + 0.5f);
    }

    public static float dp2pxReturnFloat(Context context, float dp) {
        return dp * context.getResources().getDisplayMetrics().density + 0.5f;
    }

    public static int generateViewId() {
        for (; ; ) {
            final int result = sNextGeneratedId.get();
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF) newValue = 1;
            if (sNextGeneratedId.compareAndSet(result, newValue)) {
                return result;
            }
        }
    }
}
