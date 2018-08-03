package cn.aijiamuyingfang.weapp.manager.recycleadapter;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;

import java.util.List;

import cn.aijiamuyingfang.commons.domain.coupon.GoodVoucher;
import cn.aijiamuyingfang.weapp.manager.R;
import cn.aijiamuyingfang.weapp.manager.widgets.recycleview.adapter.CommonAdapter;
import cn.aijiamuyingfang.weapp.manager.widgets.recycleview.adapter.RecyclerViewHolder;

public class GoodVoucherAdapter extends CommonAdapter<GoodVoucher> {
    /**
     * checkbox的visibility属性值
     */
    private int checkboxVisible = View.GONE;

    private GoodVoucher goodvoucher;

    public GoodVoucherAdapter(Context context, List<GoodVoucher> data) {
        super(context, data, R.layout.item_goodvoucher);
    }

    @Override
    protected void convert(RecyclerViewHolder viewHolder, final GoodVoucher itemData, int position) {
        viewHolder.setText(R.id.goodvoucher_name, itemData.getName());
        viewHolder.setText(R.id.goodvoucher_score, itemData.getScore() + "");
        viewHolder.setText(R.id.goodvoucher_description, itemData.getDescription());
        View view = viewHolder.getView(R.id.checkbox_operate_data);
        if (view instanceof CheckBox) {
            CheckBox checkBox = (CheckBox) view;
            checkBox.setVisibility(checkboxVisible);
            checkBox.setChecked(itemData.equals(goodvoucher));
            if (View.VISIBLE == checkboxVisible) {
                checkBox.setOnClickListener(v -> {
                    if (itemData.equals(goodvoucher)) {
                        goodvoucher = null;
                    } else {
                        goodvoucher = itemData;
                    }
                    notifyDataSetChanged();
                });
            }
        }

    }

    public void setCheckboxVisible(int checkboxVisible) {
        this.checkboxVisible = checkboxVisible;
    }

    public GoodVoucher getGoodVoucher() {
        return goodvoucher;
    }

    public void setGoodVoucher(GoodVoucher goodvoucher) {
        this.goodvoucher = goodvoucher;
    }
}