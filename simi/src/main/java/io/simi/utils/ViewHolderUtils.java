package io.simi.utils;

import android.util.SparseArray;
import android.view.View;

/**
 * -------------------------------
 * 		  ViewHolderUtils
 * -------------------------------
 *
 * createTime: 2015-04-27
 * updateTime: 2015-04-27
 *
 */
public final class ViewHolderUtils {

    private ViewHolderUtils(){}

    @SuppressWarnings("unchecked")
    public static <T extends View> T get(View view, int id) {
        SparseArray<View> holder = (SparseArray<View>)view.getTag();
        if (holder == null) {
            holder = new SparseArray<View>();
            view.setTag(holder);
        }
        View childView = holder.get(id);
        if (childView == null) {
            childView = view.findViewById(id);
            holder.put(id, childView);
        }
        return (T) childView;
    }
}
