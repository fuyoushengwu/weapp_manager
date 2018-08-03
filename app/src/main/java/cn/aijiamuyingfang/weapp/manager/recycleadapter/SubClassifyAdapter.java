package cn.aijiamuyingfang.weapp.manager.recycleadapter;

import android.content.Context;
import android.widget.ImageView;

import java.util.List;

import cn.aijiamuyingfang.commons.domain.goods.Classify;
import cn.aijiamuyingfang.weapp.manager.R;
import cn.aijiamuyingfang.weapp.manager.widgets.recycleview.adapter.CommonAdapter;
import cn.aijiamuyingfang.weapp.manager.widgets.recycleview.adapter.RecyclerViewHolder;
import cn.aijiamuyingfang.weapp.manager.widgets.GlideUtils;

public class SubClassifyAdapter extends CommonAdapter<Classify> {
    public SubClassifyAdapter(Context context, List<Classify> data) {
        super(context, data, R.layout.item_sub_classify);
    }

    @Override
    protected void convert(RecyclerViewHolder viewHolder, Classify itemData, int position) {
        viewHolder.setText(R.id.tv_name, itemData.getName());
        GlideUtils.load(mContext, itemData.getCoverImg(), (ImageView) viewHolder.getView(R.id.iv_view));
    }
}