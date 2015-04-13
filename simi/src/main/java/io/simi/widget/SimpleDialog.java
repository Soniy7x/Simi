package io.simi.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import io.simi.graphics.Image;
import io.simi.listener.OnConfirmListener;
import io.simi.listener.OnDismissListener;
import io.simi.utils.Unit;

/**
 * -------------------------------
 * 		   SimpleDialog
 * -------------------------------
 *
 * createTime: 2015-04-13
 * updateTime: 2015-04-13
 *
 */
@SuppressLint("ValidFragment")
public class SimpleDialog extends DialogFragment {

    private Context mContext;
    private String titleText;
    private String contentText;
    private String cancelText;
    private String confirmText;
    private OnConfirmListener onConfirmListener;
    private OnDismissListener onDismissListener;

    private int DP;

    public SimpleDialog(Context context, String titleText, String contentText, String confirmText, OnConfirmListener onConfirmListener) {
        this.mContext = context;
        this.titleText = titleText;
        this.contentText = contentText;
        this.confirmText = confirmText;
        this.onConfirmListener = onConfirmListener;
        this.DP = Unit.dp2px(mContext, 1);
    }

    public void setCancelText(String cancelText) {
        this.cancelText = cancelText;
    }

    public void setOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (onDismissListener != null) {
            onDismissListener.onDismiss();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        return createView();
    }

    private View createView() {
        LinearLayout mRootView = new LinearLayout(mContext);
        mRootView.setMinimumWidth(300 * DP);
        mRootView.setOrientation(LinearLayout.VERTICAL);
        mRootView.setBackgroundDrawable(Image.createDrawable(0xFFFFFFFF, 8 * DP));
        mRootView.setPadding(0, 20 * DP, 0, 0);

        TextView mSimpleTitle = new TextView(mContext);
        mSimpleTitle.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        mSimpleTitle.setPadding(20 * DP, 0, 0, 0);
        mSimpleTitle.setText(titleText);
        mSimpleTitle.setTextColor(0xFF434343);
        mSimpleTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
        mSimpleTitle.getPaint().setFakeBoldText(true);

        TextView mSimpleContent = new TextView(mContext);
        mSimpleContent.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        mSimpleContent.setPadding(20 * DP, 16 * DP, 20 * DP, 0);
        mSimpleContent.setText(contentText);
        mSimpleContent.setTextColor(0xFF838383);
        mSimpleContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

        RippleLayout mRippleLayout = new RippleLayout(mContext);
        mRippleLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 52 * DP));
        mRippleLayout.setPadding(0, 4 * DP, 0, 0);
        mRippleLayout.setGravity(Gravity.RIGHT);
        mRippleLayout.setOrientation(LinearLayout.HORIZONTAL);

        TextView mSimpleCancel = new TextView(mContext);
        mSimpleCancel.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
        mSimpleCancel.setPadding(32 * DP, 0, 32 * DP, 0);
        mSimpleCancel.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        mSimpleCancel.setGravity(Gravity.CENTER);
        mSimpleCancel.setTextColor(0xFF434343);
        mSimpleCancel.setShadowLayer(0.2F, 0.2F, 0.2F, 0xFF434343);
        mSimpleCancel.getPaint().setFakeBoldText(true);
        if (TextUtils.isEmpty(cancelText)) {
            mSimpleCancel.setVisibility(View.GONE);
        }else {
            mSimpleCancel.setText(cancelText);
        }
        mSimpleCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dismiss();
                    }
                }, 320);
            }
        });

        TextView mSimpleConfirm = new TextView(mContext);
        mSimpleConfirm.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
        mSimpleConfirm.setPadding(32 * DP, 0, 32 * DP, 0);
        mSimpleConfirm.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        mSimpleConfirm.setText(confirmText);
        mSimpleConfirm.setGravity(Gravity.CENTER);
        mSimpleConfirm.setTextColor(0xFF009EFC);
        mSimpleConfirm.setShadowLayer(0.2F, 0.2F, 0.2F, 0xFF009EFC);
        mSimpleConfirm.getPaint().setFakeBoldText(true);
        mSimpleConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (onConfirmListener != null) {
                            onConfirmListener.onConfirm();
                        }
                        dismiss();
                    }
                }, 320);
            }
        });

        mRippleLayout.addView(mSimpleCancel);
        mRippleLayout.addView(mSimpleConfirm);
        mRootView.addView(mSimpleTitle);
        mRootView.addView(mSimpleContent);
        mRootView.addView(mRippleLayout);

        return mRootView;
    }

    public void show() {
        show(((Activity)mContext).getFragmentManager(), ((Activity) mContext).getLocalClassName());
    }
}
