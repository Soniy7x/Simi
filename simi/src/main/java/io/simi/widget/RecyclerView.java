package io.simi.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * -------------------------------
 * 		  RecyclerView
 * -------------------------------
 *
 * createTime: 2015-04-21
 * updateTime: 2015-04-23
 *
 */
public class RecyclerView extends android.support.v7.widget.RecyclerView{

    private Adapter adapter;
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;
    private OnLackDataListener onLackDataListener;
    private OnScrollListener onScrollListener;

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
                    if (manager.findLastVisibleItemPosition() >= manager.getItemCount() - onLackDataListener.minNumber() && dy > 0 && !onLackDataListener.isLoading()) {
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
        this(context, null);
    }

    public RecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        super.setLayoutManager(new LinearLayoutManager(getContext()));
        super.setOnScrollListener(onScrollDefaultListener);
    }

    @Override
    public void setAdapter(android.support.v7.widget.RecyclerView.Adapter adapter) {
        throw new RuntimeException("You should change android.support.v7.widget.RecyclerView.Adapter to io.simi.widget.RecyclerView.Adapter!");
    }

    public void setAdapter(Adapter adapter) {
        this.adapter = adapter;
        this.adapter.setOnItemClickListener(onItemClickListener);
        this.adapter.setOnItemLongClickListener(onItemLongClickListener);
        super.setAdapter(adapter);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        if (adapter != null) {
            adapter.setOnItemClickListener(onItemClickListener);
        }
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
        if (adapter != null) {
            adapter.setOnItemLongClickListener(onItemLongClickListener);
        }
    }

    public void setOnLackDataListener(OnLackDataListener onLackDataListener) {
        this.onLackDataListener = onLackDataListener;
    }

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
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
        public int minNumber();
    }

    public static abstract class Adapter<VH extends ViewHolder> extends android.support.v7.widget.RecyclerView.Adapter<VH> {

        private OnItemClickListener onItemClickListener;
        private OnItemLongClickListener onItemLongClickListener;

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            VH viewHolder = onCreateViewHolder(parent);
            viewHolder.setOnItemClickListener(onItemClickListener);
            viewHolder.setOnItemLongClickListener(onItemLongClickListener);
            return viewHolder;
        }

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
            this.onItemLongClickListener = onItemLongClickListener;
        }

        public abstract VH onCreateViewHolder(ViewGroup parent);
    }

    public static abstract class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder implements OnClickListener, OnLongClickListener{

        private OnItemClickListener onItemClickListener;
        private OnItemLongClickListener onItemLongClickListener;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
            this.onItemLongClickListener = onItemLongClickListener;
        }

        @Override
        public void onClick(View view) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(view, getAdapterPosition());
            }
        }

        @Override
        public boolean onLongClick(View view) {
            if (onItemLongClickListener != null) {
                onItemLongClickListener.onItemLongClick(view, getAdapterPosition());
            }
            return false;
        }
    }
}
