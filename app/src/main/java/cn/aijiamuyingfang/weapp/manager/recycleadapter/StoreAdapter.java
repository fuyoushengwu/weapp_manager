package cn.aijiamuyingfang.weapp.manager.recycleadapter;

import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;

import java.util.List;

import cn.aijiamuyingfang.commons.domain.goods.Store;
import cn.aijiamuyingfang.weapp.manager.R;
import cn.aijiamuyingfang.weapp.manager.commons.CommonApp;
import cn.aijiamuyingfang.weapp.manager.commons.Constant;
import cn.aijiamuyingfang.weapp.manager.widgets.GlideUtils;
import cn.aijiamuyingfang.weapp.manager.widgets.recycleview.adapter.CommonAdapter;
import cn.aijiamuyingfang.weapp.manager.widgets.recycleview.adapter.RecyclerViewHolder;

public class StoreAdapter extends CommonAdapter<Store> {

    public StoreAdapter(Context context, List<Store> data) {
        super(context, data, R.layout.item_store);
    }

    @Override
    protected void convert(RecyclerViewHolder viewHolder, final Store itemData, int position) {
        viewHolder.setText(R.id.text_name, itemData.getName());
        viewHolder.setText(R.id.text_address, itemData.getStoreAddress().getDetail());
        GlideUtils.load(CommonApp.getApplication(), itemData.getCoverImg(), (ImageView) viewHolder.getView(R.id.iv_view));
        viewHolder.setOnClickListener(R.id.goto_store, v -> {
            Intent intent = new Intent();
            intent.setAction(Constant.BROADCAST_ACTION_TAB_SELECTED);
            intent.putExtra(Constant.INTENT_TAB_INDEX, 0);
            mContext.sendBroadcast(intent);
        });
    }
}
