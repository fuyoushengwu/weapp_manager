package cn.aijiamuyingfang.weapp.manager.widgets.recycleview.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by pc on 2018/3/31.
 */

public class RecyclerViewHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> mViews;
    private View mConvertView;

    public static RecyclerViewHolder createViewHolder(Context context, ViewGroup viewGroup, int itemViewId) {
        View itemView = LayoutInflater.from(context).inflate(itemViewId, viewGroup, false);
        return new RecyclerViewHolder(itemView);
    }

    public RecyclerViewHolder(View itemView) {
        super(itemView);
        mConvertView = itemView;
        mViews = new SparseArray<>();
    }

    public View getView(int viewId) {
        View view = mViews.get(viewId);
        if (null == view) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return view;
    }

    public View getConvertView() {
        return mConvertView;
    }

    /**
     * 设置TextView的值
     *
     * @param viewId
     * @param text
     * @return
     */
    public RecyclerViewHolder setText(int viewId, String text) {
        View v = getView(viewId);
        if (v instanceof TextView) {
            TextView tv = (TextView) v;
            tv.setText(text);
        }
        return this;
    }


    /**
     * 设置点击回调
     *
     * @param viewId
     * @param listener
     */
    public void setOnClickListener(int viewId,
                                   View.OnClickListener listener) {
        View view = getView(viewId);
        if (view != null && listener != null) {
            view.setOnClickListener(listener);
        }
    }

}
