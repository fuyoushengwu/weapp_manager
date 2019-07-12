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

import cn.aijiamuyingfang.vo.response.ResponseBean;
import cn.aijiamuyingfang.vo.response.ResponseCode;
import cn.aijiamuyingfang.vo.coupon.VoucherItem;
import cn.aijiamuyingfang.vo.goods.Good;
import cn.aijiamuyingfang.client.rest.api.GoodControllerApi;
import cn.aijiamuyingfang.weapp.manager.R;
import cn.aijiamuyingfang.weapp.manager.access.server.impl.GoodControllerClient;
import cn.aijiamuyingfang.weapp.manager.commons.utils.ToastUtils;
import cn.aijiamuyingfang.weapp.manager.widgets.GlideUtils;
import cn.aijiamuyingfang.weapp.manager.widgets.recycleview.adapter.CommonAdapter;
import cn.aijiamuyingfang.weapp.manager.widgets.recycleview.adapter.RecyclerViewHolder;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class VoucherItemAdapter extends CommonAdapter<VoucherItem> {
    private static final String TAG = VoucherItemAdapter.class.getName();
    private static final GoodControllerApi goodControllerApi = new GoodControllerClient();
    private final Set<VoucherItem> selectedItems = new HashSet<>();
    /**
     * checkbox的visibility属性值
     */
    private int checkboxVisible = View.GONE;

    public VoucherItemAdapter(Context context, List<VoucherItem> data) {
        super(context, data, R.layout.item_voucheritem);
    }

    @Override
    protected void convert(final RecyclerViewHolder viewHolder, final VoucherItem itemData, int position) {
        String goodId = itemData.getGoodId();
        viewHolder.setText(R.id.item_name, itemData.getName());
        viewHolder.setText(R.id.item_score, itemData.getScore() + "");
        goodControllerApi.getGood(goodId).subscribe(new Observer<ResponseBean<Good>>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(ResponseBean<Good> responseBean) {
                if (ResponseCode.OK.getCode().equals(responseBean.getCode())) {
                    Good good = responseBean.getData();
                    viewHolder.setText(R.id.good_name, good.getName());
                    GlideUtils.load(mContext, good.getCoverImg().getUrl(), (ImageView) viewHolder.getView(R.id.iv_view));
                } else {
                    Log.e(TAG, responseBean.getMsg());
                    ToastUtils.showSafeToast(mContext, "因服务端的原因,获取兑换项相关的商品信息失败");
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "get good failed", e);
                ToastUtils.showSafeToast(mContext, "因客户端的原因,获取兑换项相关的商品信息失败");
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