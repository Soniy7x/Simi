package io.simi.widget;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

/**
 * -------------------------------
 * 		  RefreshRecyclerView
 * -------------------------------
 *
 * createTime: 2015-04-21
 * updateTime: 2015-04-21
 *
 */
public class RefreshRecyclerView extends SwipeRefreshLayout {

    private RecyclerView mRecyclerView;

    public RefreshRecyclerView(Context context) {
        this(context, null);
    }

    public RefreshRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mRecyclerView = new RecyclerView(getContext());
        addView(mRecyclerView);
    }

    public RecyclerView getConfigRecyclerView() {
        return mRecyclerView;
    }

    public void setLayoutManager(android.support.v7.widget.RecyclerView.LayoutManager layout) {
        mRecyclerView.setLayoutManager(layout);
    }

    public void setAdapter(android.support.v7.widget.RecyclerView.Adapter adapter) {
        mRecyclerView.setAdapter(adapter);
    }

    public void setOnItemClickListener(RecyclerView.OnItemClickListener onItemClickListener) {
        mRecyclerView.setOnItemClickListener(onItemClickListener);
    }

    public void setOnItemLongClickListener(RecyclerView.OnItemLongClickListener onItemLongClickListener) {
        mRecyclerView.setOnItemLongClickListener(onItemLongClickListener);
    }

    public void setOnLackDataListener(RecyclerView.OnLackDataListener onLackDataListener) {
        mRecyclerView.setOnLackDataListener(onLackDataListener);
    }

    public void setOnScrollListener(android.support.v7.widget.RecyclerView.OnScrollListener onScrollListener) {
        mRecyclerView.setOnScrollListener(onScrollListener);
    }
}
