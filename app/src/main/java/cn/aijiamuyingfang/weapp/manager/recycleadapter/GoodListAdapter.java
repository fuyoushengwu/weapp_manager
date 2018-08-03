package cn.aijiamuyingfang.weapp.manager.recycleadapter;

import android.content.Context;
import android.widget.ImageView;

import java.util.List;

import cn.aijiamuyingfang.commons.domain.goods.Good;
import cn.aijiamuyingfang.weapp.manager.R;
import cn.aijiamuyingfang.weapp.manager.widgets.recycleview.adapter.CommonAdapter;
import cn.aijiamuyingfang.weapp.manager.widgets.recycleview.adapter.RecyclerViewHolder;
import cn.aijiamuyingfang.weapp.manager.widgets.GlideUtils;

public class GoodListAdapter extends CommonAdapter<Good> {
    public GoodListAdapter(Context context, List<Good> data) {
        super(context, data, R.layout.template_hot_wares);
    }

    @Override
    protected void convert(RecyclerViewHolder viewHolder, final Good itemData, int position) {
        GlideUtils.load(mContext, itemData.getCoverImg(), (ImageView) viewHolder.getView(R.id.iv_view));
        viewHolder.setText(R.id.text_title, itemData.getName());
        viewHolder.setText(R.id.text_price, "￥" + itemData.getPrice());
        viewHolder.setText(R.id.text_count, "库存:" + itemData.getCount());
    }


}