package io.simi.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import io.simi.graphics.Image;
import io.simi.listener.OnDismissListener;
import io.simi.norm.FONT_AWESOME;
import io.simi.norm.LEVEL;
import io.simi.utils.FontAwesome;
import io.simi.utils.Unit;

/**
 * -------------------------------
 * 		   WarningDialog
 * -------------------------------
 *
 * createTime: 2015-04-12
 * updateTime: 2015-04-12
 *
 */
@SuppressLint("ValidFragment")
public class WarningDialog extends DialogFragment{

    private Context mContext;
    private FONT_AWESOME icon;
    private String titleText;
    private String contentText;
    private String buttonText;
    private OnDismissListener onDismissListener;

    private int color;
    private int DP;

    public WarningDialog(Context context, LEVEL type, String titleText, String contentText, String buttonText) {
        this.mContext = context;
        this.titleText = titleText;
        this.contentText = contentText;
        this.buttonText = buttonText;
        this.DP = Unit.dp2px(mContext, 1);
        switch (type) {
            case VERBOSE:
                this.icon = FONT_AWESOME.FA_ELLIPSIS_H;
                this.color = 0xFF009EFC;
                break;
            case INFO:
                this.icon = FONT_AWESOME.FA_CHECK;
                this.color = 0xFF00B27D;
                break;
            case WARNING:
                this.icon = FONT_AWESOME.FA_EXCLAMATION;
                this.color = 0xFFFD9E06;
                break;
            case ERROR:
                this.icon = FONT_AWESOME.FA_CLOSE;
                this.color = 0xFFF3456D;
                break;
            default:
                this.color = 0xFF434343;
                break;
        }
    }

    public WarningDialog(Context context, LEVEL type, String titleText, String contentText, String buttonText, OnDismissListener onDismissListener) {
        this(context, type, titleText, contentText, buttonText);
        this.onDismissListener = onDismissListener;
    }

    public void setCustomerType(FONT_AWESOME icon, int color) {
        this.icon = icon;
        this.color = color;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(false);
        return createView();
    }

    private View createView() {
        RelativeLayout rootView = new RelativeLayout(mContext);
        rootView.setBackgroundDrawable(Image.createDrawable(0xFFFFFFFF, 8 * DP));

        int iconId = Unit.generateViewId();
        int titleId = Unit.generateViewId();
        int contentId = Unit.generateViewId();

        LayoutParams params;
        params = new LayoutParams(72 * DP, 72 * DP);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        params.setMargins(0, 42 * DP, 0, 0);
        TextView mWarningIcon = new TextView(mContext);
        mWarningIcon.setId(iconId);
        mWarningIcon.setLayoutParams(params);
        mWarningIcon.setGravity(Gravity.CENTER);
        mWarningIcon.setTextColor(0xFFFFFFFF);
        mWarningIcon.setTextSize(TypedValue.COMPLEX_UNIT_SP, 42);
        mWarningIcon.setBackgroundDrawable(Image.createDrawable(color, 36 * DP));
        if (icon != null) {
            FontAwesome.getInstance(mContext).setText(mWarningIcon, icon);
        }

        params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.BELOW, iconId);
        params.setMargins(0, 22 * DP, 0, 0);
        TextView mWarningTitle = new TextView(mContext);
        mWarningTitle.setId(titleId);
        mWarningTitle.setLayoutParams(params);
        mWarningTitle.setGravity(Gravity.CENTER);
        mWarningTitle.setTextColor(color);
        mWarningTitle.setText(titleText);
        mWarningTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        mWarningTitle.getPaint().setFakeBoldText(true);

        params = new LayoutParams(240 * DP, LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.BELOW, titleId);
        params.setMargins(0, 16 * DP, 0, 0);
        TextView mWarningContent = new TextView(mContext);
        mWarningContent.setPadding(32 * DP, 0, 32 * DP, 0);
        mWarningContent.setId(contentId);
        mWarningContent.setLayoutParams(params);
        mWarningContent.setGravity(Gravity.CENTER);
        mWarningContent.setTextColor(0xFF838383);
        mWarningContent.setText(contentText);
        mWarningContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

        params = new LayoutParams(240 * DP, 10 * DP);
        params.addRule(RelativeLayout.BELOW, contentId);
        params.setMargins(0, 32 * DP, 0, 0);
        View mWarningView = new View(mContext);
        mWarningView.setLayoutParams(params);
        mWarningView.setBackgroundColor(color);

        params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.BELOW, contentId);
        params.setMargins(0, 32 * DP, 0, 0);
        RippleLayout mWarningRevelLayout = new RippleLayout(mContext);
        mWarningRevelLayout.setLayoutParams(params);

        TextView mWarningButton = new TextView(mContext);
        mWarningButton.setLayoutParams(new LinearLayout.LayoutParams(240 * DP, 64 * DP));
        mWarningButton.setGravity(Gravity.CENTER);
        mWarningButton.setTextColor(0xFFFFFFFF);
        mWarningButton.setText(buttonText);
        mWarningButton.setBackgroundDrawable(Image.createDrawable(color, 2 * DP));
        mWarningButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        mWarningButton.getPaint().setFakeBoldText(true);
        mWarningButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (onDismissListener != null) {
                            onDismissListener.onDismiss();
                        }
                        dismiss();
                    }
                }, 320);
            }
        });

        mWarningRevelLayout.addView(mWarningButton);
        rootView.addView(mWarningIcon);
        rootView.addView(mWarningTitle);
        rootView.addView(mWarningContent);
        rootView.addView(mWarningView);
        rootView.addView(mWarningRevelLayout);

        return rootView;
    }

    public void show() {
        show(((Activity)mContext).getFragmentManager(), ((Activity) mContext).getLocalClassName());
    }

}
