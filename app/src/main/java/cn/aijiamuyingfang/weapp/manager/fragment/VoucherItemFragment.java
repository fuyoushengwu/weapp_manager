package cn.aijiamuyingfang.weapp.manager.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import cn.aijiamuyingfang.client.rest.api.CouponControllerApi;
import cn.aijiamuyingfang.commons.domain.coupon.VoucherItem;
import cn.aijiamuyingfang.commons.domain.coupon.response.GetVoucherItemListResponse;
import cn.aijiamuyingfang.commons.domain.response.ResponseBean;
import cn.aijiamuyingfang.weapp.manager.R;
import cn.aijiamuyingfang.weapp.manager.access.server.impl.CouponControllerClient;
import cn.aijiamuyingfang.weapp.manager.activity.GoodVoucherActionActivity;
import cn.aijiamuyingfang.weapp.manager.activity.VoucherItemActionActivity;
import cn.aijiamuyingfang.weapp.manager.commons.CommonApp;
import cn.aijiamuyingfang.weapp.manager.commons.Constant;
import cn.aijiamuyingfang.weapp.manager.recycleadapter.VoucherItemAdapter;
import cn.aijiamuyingfang.weapp.manager.widgets.recycleview.adapter.CommonAdapter;
import cn.aijiamuyingfang.weapp.manager.widgets.recycleview.adapter.OnItemClickListener;
import cn.aijiamuyingfang.weapp.manager.widgets.recycleview.RefreshableBaseFragment;
import cn.aijiamuyingfang.weapp.manager.widgets.WeToolBar;
import io.reactivex.Observable;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class VoucherItemFragment extends RefreshableBaseFragment<VoucherItem, GetVoucherItemListResponse> {
    @BindView(R.id.toolbar)
    WeToolBar mToolBar;

    /**
     * 列表项展示
     */
    private VoucherItemAdapter mVoucherItemAdapter;
    /**
     * 调用兑换券服务
     */
    private CouponControllerApi couponControllerApi = new CouponControllerClient();

    @NonNull
    @Override
    public synchronized CommonAdapter<VoucherItem> getRecyclerViewAdapter() {
        if (null == mVoucherItemAdapter) {
            mVoucherItemAdapter = new VoucherItemAdapter(getContext(), new ArrayList<>());
        }
        return mVoucherItemAdapter;
    }

    @Override
    public OnItemClickListener getOnItemClickListener() {
        return new OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(getContext(), VoucherItemActionActivity.class);
                VoucherItem voucheritem = getData(position);
                if (voucheritem != null) {
                    intent.putExtra(Constant.INTENT_VOUCHERITEM, voucheritem);
                }
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        };
    }

    @Override
    public void customRecyclerView() {
        mToolBar.setTitle("商品兑换项");
        Bundle bundle = getArguments();
        if (bundle != null && GoodVoucherActionActivity.class.getName().equals(bundle.getString(Constant.INTENT_FRAGMENT_FROM))) {
            mToolBar.setRightButtonText("确认");
            mToolBar.setRightButtonOnClickListener(v -> {
                Intent intent = new Intent();
                List<VoucherItem> voucherItemList = mVoucherItemAdapter.getSelectedItems();
                if (voucherItemList != null && !voucherItemList.isEmpty()) {
                    intent.putParcelableArrayListExtra(Constant.INTENT_SELECTED_VOUCHERITEM, (ArrayList<? extends Parcelable>) voucherItemList);
                }

                Activity activity = VoucherItemFragment.this.getActivity();
                if (activity != null) {
                    activity.setResult(0, intent);
                    activity.finish();
                }
            });
            mVoucherItemAdapter.setCheckboxVisible(View.VISIBLE);
            List<VoucherItem> selectedHolderCartItems = bundle.getParcelableArrayList(Constant.INTENT_SELECTED_VOUCHERITEM);
            mVoucherItemAdapter.setSelectedItems(selectedHolderCartItems);
        } else {
            mToolBar.setRightButtonOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), VoucherItemActionActivity.class);
                startActivity(intent);
            });
        }
    }

    @Override
    protected Observable<ResponseBean<GetVoucherItemListResponse>> customGetData(int mCurrPage, int mPageSize) {
        return couponControllerApi.getVoucherItemList(CommonApp.getApplication().getUserToken(), mCurrPage, mPageSize);
    }

    @Override
    protected List<VoucherItem> customBeforeServerData() {
        return Collections.emptyList();
    }
}
