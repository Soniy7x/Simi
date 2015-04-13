package io.simi.manager;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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

    private View mStatusBarView;

    public StatusBarManager(Activity activity) {
        Window mWindow = activity.getWindow();
        if (mWindow.getAttributes().flags == WindowManager.LayoutParams.FLAG_FULLSCREEN) {
            WindowManager.LayoutParams attrs = mWindow.getAttributes();
            attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            mWindow.setAttributes(attrs);
            mWindow.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        mWindow.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        mWindow.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        ViewGroup mRootView = (ViewGroup) ((ViewGroup)activity.findViewById(android.R.id.content)).getChildAt(0);
        mRootView.setFitsSystemWindows(true);
        mRootView.setClipToPadding(false);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, getStatusBarHeight(activity.getResources()));
        params.gravity = Gravity.TOP;
        mStatusBarView = new View(activity);
        mStatusBarView.setLayoutParams(params);
        mStatusBarView.setBackgroundColor(0x99000000);
        ((ViewGroup)mWindow.getDecorView()).addView(mStatusBarView);
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
