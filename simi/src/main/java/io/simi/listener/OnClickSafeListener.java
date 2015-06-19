package io.simi.listener;

import android.view.View;

/**
 * -------------------------------
 * 		  OnClickSafeListener
 * -------------------------------
 *
 * createTime: 2015-06-19
 * updateTime: 2015-06-19
 *
 */
public abstract class OnClickSafeListener implements View.OnClickListener{

    private long INTERVAL_MAX_TIME = 500;
    private long mLastClickTime = 0;

    public OnClickSafeListener(){}

    public OnClickSafeListener(long intervalTime) {
        INTERVAL_MAX_TIME = intervalTime;
    }

    private boolean isSafe() {
        long mCurrentTime = System.currentTimeMillis();
        long mIntervalTime = mCurrentTime - mLastClickTime;
        if (mIntervalTime > 0 && mIntervalTime < INTERVAL_MAX_TIME) {
            return false;
        }
        mLastClickTime = mCurrentTime;
        return true;
    }

    @Override
    public void onClick(View v) {
        if (isSafe()) {
            onClickSafe(v);
        }
    }

    public abstract void onClickSafe(View v);
}
