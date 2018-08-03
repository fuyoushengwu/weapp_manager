package cn.aijiamuyingfang.weapp.manager.widgets.recycleview.adapter;

import android.content.Context;

import java.util.List;

/**
 * Created by pc on 2018/3/31.
 */

public abstract class CommonAdapter<T> extends MultiItemTypeAdapter<T> {

    public CommonAdapter(Context context, List<T> data, final int layoutId) {
        super(context, data);
        addItemViewDelegate(new ItemViewDelegate<T>() {
            @Override
            public int getItemViewLayoutId() {
                return layoutId;
            }

            @Override
            public boolean isForViewType(T itemData, int position) {
                return true;
            }

            @Override
            public void convert(RecyclerViewHolder holder, T itemData, int position) {
                CommonAdapter.this.convert(holder, itemData, position);
            }
        });
    }

    protected abstract void convert(RecyclerViewHolder viewHolder, T itemData, int position);

}
