package io.simi.widget;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

public class RecyclerView extends android.support.v7.widget.RecyclerView{

    private int pressPosition = 0;
    private long pressTime = 0;
    private boolean isPressed = false;
    private Handler itemHandler = new Handler();
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;
    private OnLackDataListener onLackDataListener;
    private OnScrollListener onScrollListener;
    private Runnable itemRunnable = new Runnable() {
        @Override
        public void run() {
            if (isPressed && System.currentTimeMillis() - pressTime >= ViewConfiguration.getLongPressTimeout() && onItemLongClickListener != null) {
                onItemLongClickListener.onItemLongClick(getChildAt(pressPosition), pressPosition);
                isPressed = false;
                itemHandler.removeCallbacks(itemRunnable);
            }else {
                itemHandler.postDelayed(itemRunnable, 100);
            }
        }
    };
    private OnScrollListener onScrollDefaultListener = new OnScrollListener() {
        @Override
        public void onScrollStateChanged(android.support.v7.widget.RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (onScrollListener != null) {
                onScrollListener.onScrollStateChanged(recyclerView, newState);
            }
        }
        @Override
        public void onScrolled(android.support.v7.widget.RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (onLackDataListener != null) {
                try {
                    LinearLayoutManager manager = (LinearLayoutManager) getLayoutManager();
                    if (manager.findLastVisibleItemPosition() >= manager.getItemCount() - 4 && dy > 0 && !onLackDataListener.isLoading()) {
                        onLackDataListener.onLackData();
                    }
                }catch (Exception e) {
                    throw new IllegalStateException("this RecyclerView's LayoutManager isn't a LinearLayoutManager.");
                }
            }
            if (onScrollListener != null) {
                onScrollListener.onScrolled(recyclerView, dx, dy);
            }
        }
    };

    public RecyclerView(Context context) {
        super(context);
    }

    public RecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        super.setOnScrollListener(onScrollDefaultListener);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isPressed = true;
                if (onItemClickListener != null || onItemLongClickListener != null) {
                    pressPosition = getOnClickItemPosition((int) e.getRawY());
                    pressTime = System.currentTimeMillis();
                }
                if (onItemLongClickListener != null) {
                    itemHandler.post(itemRunnable);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isPressed && onItemClickListener != null) {
                    onItemClickListener.onItemClick(getChildAt(pressPosition), pressPosition);
                }
            case MotionEvent.ACTION_CANCEL:
                isPressed = false;
                break;
        }
        return super.onTouchEvent(e);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public void setOnLackDataListener(OnLackDataListener onLackDataListener) {
        this.onLackDataListener = onLackDataListener;
    }

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    private int getOnClickItemPosition(int y) {
        int start = 0;
        int end = getChildCount() - 1;
        while (start <= end) {
            int middle = (start + end) / 2;
            if (y < getChildAt(middle).getHeight() * (middle + 1)) {
                end = middle - 1;
            }else if (y > getChildAt(middle).getHeight() * (middle + 2)) {
                start = middle + 1;
            }else {
                return middle;
            }
        }
        return -1;
    }

    public interface OnItemClickListener{
        public void onItemClick(View view, int position);
    }

    public interface OnItemLongClickListener{
        public void onItemLongClick(View view, int position);
    }

    public interface OnLackDataListener{
        public void onLackData();
        public boolean isLoading();
    }
}
