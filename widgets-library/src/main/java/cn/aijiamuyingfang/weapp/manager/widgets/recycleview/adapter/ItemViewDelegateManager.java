package cn.aijiamuyingfang.weapp.manager.widgets.recycleview.adapter;

import android.util.SparseArray;

/**
 * Created by pc on 2018/3/31.
 */

public class ItemViewDelegateManager<T> {
    private SparseArray<ItemViewDelegate<T>> delegates = new SparseArray<>();

    public int getItemViewDelegateCount() {
        return delegates.size();
    }

    public ItemViewDelegateManager<T> addDelegate(ItemViewDelegate<T> delegate) {
        if (delegate != null) {
            delegates.put(delegates.size(), delegate);
        }
        return this;
    }

    public int getItemViewType(T itemData, int position) {
        for (int i = 0; i < delegates.size(); i++) {
            ItemViewDelegate<T> delegate = delegates.get(i);
            if (delegate.isForViewType(itemData, position)) {
                return i;
            }
        }
        throw new IllegalArgumentException("No ItemViewDelegate added that matches position=" + position + " in data source");
    }

    public ItemViewDelegate<T> getItemViewDelegate(int viewType) {
        return delegates.get(viewType);
    }

    public int getItemViewLayoutId(int viewType) {
        return getItemViewDelegate(viewType).getItemViewLayoutId();
    }

    public int getItemViewType(ItemViewDelegate<T> itemViewDelegate) {
        return delegates.indexOfValue(itemViewDelegate);
    }

    public void convert(RecyclerViewHolder viewHolder, T itemData, int position) {
        int viewType = getItemViewType(itemData, position);
        ItemViewDelegate<T> delegate = getItemViewDelegate(viewType);
        delegate.convert(viewHolder, itemData, position);
    }

}
