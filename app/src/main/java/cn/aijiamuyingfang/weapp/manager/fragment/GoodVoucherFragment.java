package cn.aijiamuyingfang.weapp.manager.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import cn.aijiamuyingfang.client.rest.api.CouponControllerApi;
import cn.aijiamuyingfang.commons.domain.coupon.GoodVoucher;
import cn.aijiamuyingfang.commons.domain.coupon.response.GetGoodVoucherListResponse;
import cn.aijiamuyingfang.commons.domain.response.ResponseBean;
import cn.aijiamuyingfang.weapp.manager.R;
import cn.aijiamuyingfang.weapp.manager.access.server.impl.CouponControllerClient;
import cn.aijiamuyingfang.weapp.manager.activity.GoodActionActivity;
import cn.aijiamuyingfang.weapp.manager.activity.GoodVoucherActionActivity;
import cn.aijiamuyingfang.weapp.manager.commons.CommonApp;
import cn.aijiamuyingfang.weapp.manager.commons.Constant;
import cn.aijiamuyingfang.weapp.manager.recycleadapter.GoodVoucherAdapter;
import cn.aijiamuyingfang.weapp.manager.widgets.WeToolBar;
import cn.aijiamuyingfang.weapp.manager.widgets.recycleview.RefreshableBaseFragment;
import cn.aijiamuyingfang.weapp.manager.widgets.recycleview.adapter.CommonAdapter;
import cn.aijiamuyingfang.weapp.manager.widgets.recycleview.adapter.OnItemClickListener;
import io.reactivex.Observable;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class GoodVoucherFragment extends RefreshableBaseFragment<GoodVoucher, GetGoodVoucherListResponse> {
    @BindView(R.id.toolbar)
    WeToolBar mToolBar;
    private GoodVoucherAdapter mGoodVoucherAdapter;
    private CouponControllerApi couponControllerApi = new CouponControllerClient();

    @NonNull
    @Override
    public synchronized CommonAdapter<GoodVoucher> getRecyclerViewAdapter() {
        if (null == mGoodVoucherAdapter) {
            mGoodVoucherAdapter = new GoodVoucherAdapter(getContext(), new ArrayList<>());
        }
        return mGoodVoucherAdapter;
    }

    @Override
    public OnItemClickListener getOnItemClickListener() {
        return new OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(getContext(), GoodVoucherActionActivity.class);
                intent.putExtra(Constant.INTENT_GOODVOUCHER, mAdapter.getData(position));
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
        mToolBar.setTitle("商品兑换券");
        Bundle bundle = getArguments();
        if (bundle != null && GoodActionActivity.class.getName().equals(bundle.getString(Constant.INTENT_FRAGMENT_FROM))) {
            mToolBar.setRightButtonText("确认");
            mToolBar.setRightButtonOnClickListener(v -> {
                Intent intent = new Intent();
                intent.putExtra(Constant.INTENT_SELECTED_GOODVOUCHER, mGoodVoucherAdapter.getGoodVoucher());
                Activity activity = GoodVoucherFragment.this.getActivity();
                if (activity != null) {
                    activity.setResult(0, intent);
                    activity.finish();
                }

            });
            mGoodVoucherAdapter.setCheckboxVisible(View.VISIBLE);
            GoodVoucher goodvoucher = bundle.getParcelable(Constant.INTENT_SELECTED_GOODVOUCHER);
            mGoodVoucherAdapter.setGoodVoucher(goodvoucher);
        } else {
            mToolBar.setRightButtonOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), GoodVoucherActionActivity.class);
                startActivity(intent);
            });
        }
    }

    @Override
    protected Observable<ResponseBean<GetGoodVoucherListResponse>> customGetData(int mCurrPage, int mPageSize) {
        return couponControllerApi.getGoodVoucherList(CommonApp.getApplication().getUserToken(), mCurrPage, mPageSize);
    }

    @Override
    protected List<GoodVoucher> customBeforeServerData() {
        return Collections.emptyList();
    }
}
