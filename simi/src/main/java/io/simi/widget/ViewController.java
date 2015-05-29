package io.simi.widget;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import io.simi.utils.AttributeParser;
import io.simi.utils.Unit;

/**
 * -------------------------------
 * 		  ViewController
 * -------------------------------
 *
 * createTime: 2015-04-20
 * updateTime: 2015-04-20
 *
 */
public class ViewController extends RelativeLayout{

    public static final String TYPE_TOP = "top";
    public static final String TYPE_BOTTOM = "bottom";
    public static final String TYPE_TOP_FLOAT = "top_float";
    public static final String TYPE_BOTTOM_FLOAT = "bottom_float";

    private TabNavigatorView mNavigator;
    private ViewPager mViewPager;
    private ViewPager.OnPageChangeListener onPageChangeListener;

    private String mType = TYPE_TOP;

    public ViewController(Context context) {
        this(context, null);
    }

    public ViewController(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewController(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mNavigator = new TabNavigatorView(getContext());
        mNavigator.setId(Unit.generateViewId());
        mViewPager = new ViewPager(getContext());
        mViewPager.setId(Unit.generateViewId());
        if (attrs != null) {
            mType = AttributeParser.parserType(getContext(), AttributeParser.SIMI_NAMESPACE, attrs, TYPE_TOP);
            mNavigator.setBackgroundColor(AttributeParser.parseNavigatorColor(getContext(), AttributeParser.SIMI_NAMESPACE, attrs, 0xFF1E88EF));
            mViewPager.setBackgroundColor(AttributeParser.parseContentColor(getContext(), AttributeParser.SIMI_NAMESPACE, attrs, 0xFFFFFFF));
            mNavigator.setTextSize(AttributeParser.parseTextSize(AttributeParser.SIMI_NAMESPACE, attrs, 16));
            mNavigator.setTextColor(AttributeParser.parseTextColor(getContext(), AttributeParser.SIMI_NAMESPACE, attrs, 0x66FFFFFF));
            mNavigator.setTextSelectColor(AttributeParser.parseTextSelectColor(getContext(), AttributeParser.SIMI_NAMESPACE, attrs, 0xFFFFFFFF));
            mNavigator.setIndicatorColor(AttributeParser.parseIndicatorColor(getContext(), AttributeParser.SIMI_NAMESPACE, attrs, 0x66FFFFFF));
        }else {
            mNavigator.setBackgroundColor(0xFF1E88EF);
            mViewPager.setBackgroundColor(0xFFFFFFFF);
            mNavigator.setTextSize(16);
            mNavigator.setTextColor(0x66FFFFFF);
            mNavigator.setTextSelectColor(0xFFFFFFFF);
            mNavigator.setIndicatorColor(0x66FFFFFF);
        }
        addView(mViewPager);
        addView(mNavigator);
        updateView();
    }

    public void setAdapter(PagerAdapter adapter) {
        if (adapter == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }else {
            mViewPager.setAdapter(adapter);
            mNavigator.setViewPager(mViewPager);
        }
    }

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;
        mNavigator.setOnPageChangeListener(onPageChangeListener);
    }

    private void updateView() {
        LayoutParams mNavigatorLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, Unit.dp2px(getContext(), 48));
        LayoutParams mViewPagerLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        switch (mType) {
            case TYPE_BOTTOM:
                mNavigatorLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                mViewPagerLayoutParams.addRule(RelativeLayout.ABOVE, mNavigator.getId());
                break;
            case TYPE_TOP_FLOAT:
                mNavigatorLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
                break;
            case TYPE_BOTTOM_FLOAT:
                mNavigatorLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                break;
            default:
                mNavigatorLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
                mViewPagerLayoutParams.addRule(RelativeLayout.BELOW, mNavigator.getId());
                break;
        }
        mNavigator.setLayoutParams(mNavigatorLayoutParams);
        mViewPager.setLayoutParams(mViewPagerLayoutParams);
    }

    public void setTextSize(int textSize) {
        mNavigator.setTextSize(textSize);
    }

    public void setTextColor(int color) {
        mNavigator.setTextColor(color);
    }

    public void setTextSelectColor(int color) {
        mNavigator.setTextSelectColor(color);
    }

    public void setIndicatorColor(int color) {
        mNavigator.setIndicatorColor(color);
    }

    public void setBackgroundColor(int color) {
        mNavigator.setBackgroundColor(color);
    }
}
