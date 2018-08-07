package cn.aijiamuyingfang.weapp.manager.recycleadapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.aijiamuyingfang.client.rest.api.GoodControllerApi;
import cn.aijiamuyingfang.commons.domain.coupon.VoucherItem;
import cn.aijiamuyingfang.commons.domain.goods.Good;
import cn.aijiamuyingfang.commons.domain.response.ResponseBean;
import cn.aijiamuyingfang.commons.domain.response.ResponseCode;
import cn.aijiamuyingfang.weapp.manager.R;
import cn.aijiamuyingfang.weapp.manager.access.server.impl.GoodControllerClient;
import cn.aijiamuyingfang.weapp.manager.commons.CommonApp;
import cn.aijiamuyingfang.weapp.manager.widgets.GlideUtils;
import cn.aijiamuyingfang.weapp.manager.widgets.recycleview.adapter.CommonAdapter;
import cn.aijiamuyingfang.weapp.manager.widgets.recycleview.adapter.RecyclerViewHolder;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class VoucherItemAdapter extends CommonAdapter<VoucherItem> {
    private static final String TAG = VoucherItemAdapter.class.getName();
    private GoodControllerApi goodControllerApi = new GoodControllerClient();
    /**
     * checkbox的visibility属性值
     */
    private int checkboxVisible = View.GONE;

    private Set<VoucherItem> selectedItems = new HashSet<>();

    public VoucherItemAdapter(Context context, List<VoucherItem> data) {
        super(context, data, R.layout.item_voucheritem);
    }

    @Override
    protected void convert(final RecyclerViewHolder viewHolder, final VoucherItem itemData, int position) {
        String goodid = itemData.getGoodid();
        viewHolder.setText(R.id.item_name, itemData.getName());
        viewHolder.setText(R.id.item_score, itemData.getScore() + "");
        goodControllerApi.getGood(CommonApp.getApplication().getUserToken(), goodid).subscribe(new Observer<ResponseBean<Good>>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(ResponseBean<Good> responseBean) {
                if (ResponseCode.OK.getCode().equals(responseBean.getCode())) {
                    Good good = responseBean.getData();
                    viewHolder.setText(R.id.good_name, good.getName());
                    GlideUtils.load(mContext, good.getCoverImg(), (ImageView) viewHolder.getView(R.id.iv_view));
                } else {
                    Log.e(TAG, responseBean.getMsg());
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "get good failed", e);
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "get good complete");
            }
        });
        View view = viewHolder.getView(R.id.checkbox_operate_data);
        if (view instanceof CheckBox) {
            CheckBox checkBox = (CheckBox) view;
            checkBox.setVisibility(checkboxVisible);
            checkBox.setChecked(selectedItems.contains(itemData));
            if (View.VISIBLE == checkboxVisible) {
                checkBox.setOnClickListener(v -> {
                    if (selectedItems.contains(itemData)) {
                        selectedItems.remove(itemData);
                    } else {
                        selectedItems.add(itemData);
                    }
                });
            }
        }

    }

    public void setCheckboxVisible(int visible) {
        this.checkboxVisible = visible;
    }

    public List<VoucherItem> getSelectedItems() {
        return new ArrayList<>(selectedItems);
    }

    public void setSelectedItems(List<VoucherItem> selectedItems) {
        if (selectedItems != null) {
            this.selectedItems.clear();
            this.selectedItems.addAll(selectedItems);
        }
    }


}