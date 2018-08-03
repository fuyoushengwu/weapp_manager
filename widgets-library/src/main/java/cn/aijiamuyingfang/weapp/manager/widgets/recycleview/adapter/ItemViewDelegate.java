package cn.aijiamuyingfang.weapp.manager.widgets.recycleview.adapter;

/**
 * Created by pc on 2018/3/31.
 */

public interface ItemViewDelegate<T> {
    /**
     * 获得项布局ID
     *
     * @return
     */
    int getItemViewLayoutId();

    /**
     * 判断positon下的项是否可以用itemData填充
     *
     * @param itemData
     * @param position
     * @return
     */
    boolean isForViewType(T itemData, int position);

    /**
     * 用itemData填充position下的项
     *
     * @param holder
     * @param itemData
     * @param position
     */
    void convert(RecyclerViewHolder holder, T itemData, int position);
}
