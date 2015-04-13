package io.simi.manager;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

/**
 * -------------------------------
 * 		 StatusBarManager
 * -------------------------------
 *
 * createTime: 2015-04-12
 * updateTime: 2015-04-12
 *
 */
public class StatusBarManager {

    private static StatusBarManager instance;
    private static int mScreenOrientation = Configuration.ORIENTATION_PORTRAIT;

    private View mStatusBarView;

    private StatusBarManager(Activity activity) {
        if (activity.getWindow().getAttributes().flags == WindowManager.LayoutParams.FLAG_FULLSCREEN) {
            return;
        }
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        ViewGroup mRootView = (ViewGroup) ((ViewGroup)activity.findViewById(android.R.id.content)).getChildAt(0);
        mRootView.setFitsSystemWindows(true);
        mRootView.setClipToPadding(false);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, getStatusBarHeight(activity.getResources()));
        params.gravity = Gravity.TOP;
        mStatusBarView = new View(activity);
        mStatusBarView.setLayoutParams(params);
        mStatusBarView.setBackgroundColor(0x99000000);
        ((ViewGroup)activity.getWindow().getDecorView()).addView(mStatusBarView);
    }

    public static StatusBarManager getInstance(Activity activity) {
        int currentOrientation = activity.getResources().getConfiguration().orientation;
        if (instance == null || (mScreenOrientation != currentOrientation)) {
            mScreenOrientation = currentOrientation;
            instance = new StatusBarManager(activity);
        }
        return instance;
    }

    public void setColor(int color) {
        mStatusBarView.setBackgroundColor(color);
    }

    public void setColor(Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mStatusBarView.setBackground(drawable);
        }else {
            mStatusBarView.setBackgroundDrawable(drawable);
        }
    }

    private int getStatusBarHeight(Resources res) {
        int resId = res.getIdentifier("status_bar_height", "dimen", "android");
        if (resId > 0) {
            return res.getDimensionPixelSize(resId);
        }
        return 0;
    }

}
