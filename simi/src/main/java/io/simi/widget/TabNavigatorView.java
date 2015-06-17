package io.simi.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import io.simi.utils.AttributeParser;
import io.simi.utils.Unit;

/**
 * -------------------------------
 * 		  TabNavigatorView
 * -------------------------------
 *
 * createTime: 2015-04-20
 * updateTime: 2015-04-20
 *
 */
public class TabNavigatorView extends HorizontalScrollView{

    private LinearLayout mContainer;
    private ViewPager mViewPager;

    private int DP = 0;
    private int mCount = 0;
    private int mTextSize = 16;
    private int mTextColor = 0x66FFFFFF;
    private int mSelectedTextColor = 0xCCFFFFFF;
    private int mIndicatorColor = 0x66FFFFFF;
    private int mUnderLineColor = 0x66FFFFFF;

    private int mLastScrollX = 0;

    private int mCurrentPosition = 0;
    private int mSelectedPosition = 0;
    private float mCurrentPositionOffset = 0;

    private Typeface typeface;
    private Paint mPaint;
    private LinearLayout.LayoutParams mExpandedLayoutParams;
    private ViewPager.OnPageChangeListener onCustomerPageChangeListener;

    {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mExpandedLayoutParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1F);
    }

    public TabNavigatorView(Context context) {
        this(context, null);
    }

    public TabNavigatorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabNavigatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setFillViewport(true);
        setWillNotDraw(false);

        mContainer = new LinearLayout(getContext());
        mContainer.setOrientation(LinearLayout.HORIZONTAL);
        mContainer.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        addView(mContainer);

        DP = Unit.dp2px(getContext(), 1);

        if (attrs != null) {
            setBackgroundColor(AttributeParser.parseNavigatorColor(getContext(), AttributeParser.SIMI_NAMESPACE, attrs, 0xFF1E88EF));
            setTextSize(AttributeParser.parseTextSize(AttributeParser.SIMI_NAMESPACE, attrs, 16));
            setTextColor(AttributeParser.parseTextColor(getContext(), AttributeParser.SIMI_NAMESPACE, attrs, 0x66FFFFFF));
            setTextSelectColor(AttributeParser.parseTextSelectColor(getContext(), AttributeParser.SIMI_NAMESPACE, attrs, 0xFFFFFFFF));
            setIndicatorColor(AttributeParser.parseIndicatorColor(getContext(), AttributeParser.SIMI_NAMESPACE, attrs, 0x66FFFFFF));
            setUnderLineColor(AttributeParser.parseUnderLineColor(getContext(), AttributeParser.SIMI_NAMESPACE, attrs, 0x66FFFFFF));
        }else {
            setBackgroundColor(0xFF1E88EF);
            setTextSize(16);
            setTextColor(0x66FFFFFF);
            setTextSelectColor(0xFFFFFFFF);
            setIndicatorColor(0x66FFFFFF);
            setUnderLineColor(0x66FFFFFF);
        }
    }

    public void setViewPager(ViewPager viewPager) {
        this.mViewPager = viewPager;
        if (viewPager.getAdapter() == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }
        this.mViewPager.setOnPageChangeListener(onPageChangeListener);
        notifyDataSetChanged();
    }

    public void notifyDataSetChanged() {
        mContainer.removeAllViews();
        mCount = mViewPager.getAdapter().getCount();
        TabNavigatorAdapter adapter;
        try {
            adapter = (TabNavigatorAdapter) mViewPager.getAdapter();
        }catch (Exception e) {
            throw new RuntimeException("TabNavigatorView must be use TabNavigatorAdapter！");
        }
        for (int i = 0; i < mCount; i++) {
            addTabWithText(i, adapter.getPageTitle(i).toString(), adapter.getPageIcon(i));
        }
        updateView();
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= 16) {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                mCurrentPosition = mViewPager.getCurrentItem();
                scrollToChild(mCurrentPosition, 0);
            }
        });
    }

    private void addTabWithText(final int position, String text, Drawable icon) {
        TextView tab = new TextView(getContext());
        tab.setText(text);
        tab.setGravity(Gravity.CENTER);
        tab.setSingleLine();
        tab.setFocusable(true);
        tab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(position);
            }
        });
        if (typeface != null) {
            tab.setTypeface(typeface);
        }
        if (icon != null) {
            icon.setBounds(0, 0, icon.getMinimumWidth(), icon.getMinimumHeight());
            tab.setCompoundDrawables(null, icon, null, null);
        }
        mContainer.addView(tab, position, mExpandedLayoutParams);
    }

    private void updateView() {
        for (int i = 0; i < mCount; i++) {
            View childView = mContainer.getChildAt(i);
            if (childView instanceof  TextView) {
                TextView tab = (TextView) childView;
                tab.setTextSize(TypedValue.COMPLEX_UNIT_SP, mTextSize);
                if (typeface != null) {
                    tab.setTypeface(typeface);
                }
                if (i == mSelectedPosition) {
                    tab.setTextColor(mSelectedTextColor);
                }else {
                    tab.setTextColor(mTextColor);
                }
            }
        }
    }

    private void scrollToChild(int position, int offset) {
        if (mCount < 1) {
            return;
        }
        int scrollX = mContainer.getChildAt(position).getLeft() + offset;
        if (position > 0 || offset > 0) {
            scrollX -= 52 * DP;
        }
        if (scrollX != mLastScrollX) {
            mLastScrollX = scrollX;
            scrollTo(scrollX, 0);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mCount < 1) {
            return;
        }
        final int height = getHeight();
        mPaint.setColor(mUnderLineColor);
        canvas.drawRect(0, height - 1F * DP, getWidth(), height, mPaint);
        mPaint.setColor(mIndicatorColor);
        View currentTab = mContainer.getChildAt(mCurrentPosition);
        float indicatorLeft = currentTab.getLeft();
        float indicatorRight = currentTab.getRight();
        if (mCurrentPositionOffset > 0 && mCurrentPosition < mCount - 1) {
            View nextTab = mContainer.getChildAt(mCurrentPosition + 1);
            final float nextTabLeft = nextTab.getLeft();
            final float nextTabRight = nextTab.getRight();
            indicatorLeft = mCurrentPositionOffset * nextTabLeft + (1F - mCurrentPositionOffset) * indicatorLeft;
            indicatorRight = mCurrentPositionOffset * nextTabRight + (1F - mCurrentPositionOffset) * indicatorRight;
        }
        canvas.drawRect(indicatorLeft, height - 3 * DP, indicatorRight, height, mPaint);
    }

    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            mCurrentPosition = position;
            mCurrentPositionOffset = positionOffset;
            scrollToChild(position, (int)(positionOffset * mContainer.getChildAt(position).getWidth()));
            invalidate();
            if (onCustomerPageChangeListener != null) {
                onCustomerPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
        }

        @Override
        public void onPageSelected(int position) {
            mSelectedPosition = position;
            updateView();
            if (onCustomerPageChangeListener != null) {
                onCustomerPageChangeListener.onPageSelected(position);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                scrollToChild(mViewPager.getCurrentItem(), 0);
            }
            if (onCustomerPageChangeListener != null) {
                onCustomerPageChangeListener.onPageScrollStateChanged(state);
            }
        }
    };

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        this.onCustomerPageChangeListener = onPageChangeListener;
    }



    public void setTextSize(int textSize) {
        this.mTextSize = textSize;
        updateView();
    }

    public void setTextColor(int color) {
        this.mTextColor = color;
        updateView();
    }

    public void setTextSelectColor(int color) {
        this.mSelectedTextColor = color;
        updateView();
    }

    public void setIndicatorColor(int color) {
        this.mIndicatorColor = color;
        updateView();
    }

    public void setUnderLineColor(int mUnderLineColor) {
        this.mUnderLineColor = mUnderLineColor;
        updateView();
    }

    public void setTypeface(Typeface typeface) {
        this.typeface = typeface;
        updateView();
    }

    public static abstract class TabNavigatorAdapter extends FragmentPagerAdapter {

        public TabNavigatorAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public abstract CharSequence getPageTitle(int position);
        public abstract Drawable getPageIcon(int position);
    }
}
