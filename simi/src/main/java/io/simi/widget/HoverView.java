package io.simi.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import io.simi.utils.ViewHelperUtils;

/**
 * -------------------------------
 * 		    HoverView
 * -------------------------------
 *
 * createTime: 2015-04-27
 * updateTime: 2015-04-27
 *
 */
public class HoverView extends FrameLayout implements AbsListView.OnScrollListener {

	private LinearLayout mHeaderLayout;
	private LinearLayout mHeaderTitleLayout;
	private LinearLayout mHeaderHoverLayout;

	private View mPaddingView;
	private ListView mListView;

    private OnLackDataListener onLackDataListener;
	private OnHoverStatusListener onHoverStatusListener;
    private AbsListView.OnScrollListener onScrollListener;

	private int headerHeight;

	public HoverView(Context context) {
		this(context, null);
	}

	public HoverView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public HoverView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);

		mHeaderLayout = new LinearLayout(getContext());
		mHeaderLayout.setOrientation(LinearLayout.VERTICAL);
		mHeaderLayout.setBackgroundColor(Color.TRANSPARENT);
		mHeaderLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		mHeaderLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				headerHeight = mHeaderLayout.getHeight();
				resetHeaderView(headerHeight);
			}
		});

        mHeaderTitleLayout = new LinearLayout(getContext());
        mHeaderTitleLayout.setOrientation(LinearLayout.VERTICAL);
        mHeaderTitleLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        mHeaderHoverLayout = new LinearLayout(getContext());
        mHeaderHoverLayout.setBackgroundColor(Color.GRAY);
        mHeaderHoverLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        mPaddingView = new FrameLayout(getContext());
        mPaddingView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT));
        mPaddingView.setBackgroundColor(Color.TRANSPARENT);
        mPaddingView.setPadding(0, headerHeight, 0, 0);

        mListView = new ListView(getContext());
        mListView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mListView.addHeaderView(mPaddingView);
        mListView.setBackgroundColor(Color.TRANSPARENT);
        mListView.setOnScrollListener(this);

		mHeaderLayout.addView(mHeaderTitleLayout);
		mHeaderLayout.addView(mHeaderHoverLayout);
		addView(mListView);
		addView(mHeaderLayout);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		float translationY = Math.max(-getScrollY(view), -mHeaderTitleLayout.getHeight());
		ViewHelperUtils.setTranslationY(mHeaderLayout, translationY);
		if (onHoverStatusListener != null) {
            onHoverStatusListener.isHover(translationY <= -mHeaderTitleLayout.getHeight());
		}
        if (onLackDataListener != null) {
            if (visibleItemCount >= totalItemCount - 4 && !onLackDataListener.isLoading()) {
                onLackDataListener.onLackData();
            }
        }
        if (onScrollListener != null) {
            onScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (onScrollListener != null) {
            onScrollListener.onScrollStateChanged(view, scrollState);
        }
	}

    private int getScrollY(AbsListView view) {
        View c = view.getChildAt(0);
        if (c == null) {
            return 0;
        }
        int firstVisiblePosition = view.getFirstVisiblePosition();
        int top = c.getTop();
        int headerHeight = 0;
        if (firstVisiblePosition >= 1) {
            headerHeight = mHeaderLayout.getHeight();
        }
        return -top + firstVisiblePosition * c.getHeight() + headerHeight;
    }

	public void resetHeaderView(int height){
		if (mPaddingView != null) {
            mPaddingView.setPadding(0, height, 0, 0);
		}
	}

	public ListView getListView(){
		return mListView;
	}

    public void setAdapter(ListAdapter adapter) {
        mListView.setAdapter(adapter);
    }

	public void setTitleView(View titleView) {
        mHeaderTitleLayout.removeAllViews();
        mHeaderTitleLayout.addView(titleView);
    }

	public void setHoverView(View hoverView){
        mHeaderHoverLayout.removeAllViews();
        mHeaderHoverLayout.addView(hoverView);
	}

    public void setOnLackDataListener(OnLackDataListener onLackDataListener) {
        this.onLackDataListener = onLackDataListener;
    }

	public void setOnHoverStatusListener(OnHoverStatusListener hoverStatusListener) {
		this.onHoverStatusListener = hoverStatusListener;
	}

    public void setOnScrollListener(AbsListView.OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        mListView.setOnItemClickListener(onItemClickListener);
    }

    public void setOnItemLongClickListener(AdapterView.OnItemLongClickListener onItemLongClickListener) {
        mListView.setOnItemLongClickListener(onItemLongClickListener);
    }

    public static interface OnHoverStatusListener{
		public void isHover(boolean status);
	}

    public interface OnLackDataListener{
        public void onLackData();
        public boolean isLoading();
    }
}