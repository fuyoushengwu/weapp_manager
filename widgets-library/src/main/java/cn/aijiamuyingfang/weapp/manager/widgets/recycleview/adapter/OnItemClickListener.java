package cn.aijiamuyingfang.weapp.manager.widgets.recycleview.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 循环项被点击后的回调接口
 */
public interface OnItemClickListener {
    void onItemClick(View view, RecyclerView.ViewHolder holder, int position);

    boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position);
}
