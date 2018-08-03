package cn.aijiamuyingfang.weapp.manager.recycleadapter;

import android.content.Context;

import java.util.List;

import cn.aijiamuyingfang.commons.domain.goods.Classify;
import cn.aijiamuyingfang.weapp.manager.R;
import cn.aijiamuyingfang.weapp.manager.widgets.recycleview.adapter.CommonAdapter;
import cn.aijiamuyingfang.weapp.manager.widgets.recycleview.adapter.RecyclerViewHolder;

public class TopClassifyRecyclerAdapter extends CommonAdapter<Classify> {
    private int selectPosition = 0;

    public TopClassifyRecyclerAdapter(Context context, List<Classify> data) {
        super(context, data, R.layout.item_top_classify);
    }

    @Override
    protected void convert(RecyclerViewHolder viewHolder, Classify itemData, int position) {
        viewHolder.setText(R.id.textView, itemData.getName());
        if (position == selectPosition) {
            viewHolder.getConvertView().setEnabled(false);
        } else {
            viewHolder.getConvertView().setEnabled(true);
        }
    }

    public void setSelectPosition(int selectPosition) {
        this.selectPosition = selectPosition;
    }
}
