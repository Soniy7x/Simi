package io.simi.listener;

import android.view.View;
import android.widget.AdapterView;

/**
 * -------------------------------
 * 		  OnItemClickSafeListener
 * -------------------------------
 *
 * createTime: 2015-06-19
 * updateTime: 2015-06-19
 *
 */
public abstract class OnItemClickSafeListener implements AdapterView.OnItemClickListener{

    private long INTERVAL_MAX_TIME = 500;
    private long mLastClickTime = 0;

    public OnItemClickSafeListener(){}

    public OnItemClickSafeListener(long intervalTime) {
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (isSafe()) {
            onItemClickSafe(parent, view, position, id);
        }
    }

    public abstract void onItemClickSafe(AdapterView<?> parent, View view, int position, long id);
}
