package cn.aijiamuyingfang.weapp.manager.activity;

import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.aijiamuyingfang.client.rest.api.CouponControllerApi;
import cn.aijiamuyingfang.commons.domain.coupon.GoodVoucher;
import cn.aijiamuyingfang.commons.domain.coupon.VoucherItem;
import cn.aijiamuyingfang.commons.domain.response.ResponseCode;
import cn.aijiamuyingfang.weapp.manager.FragmentContainerActivity;
import cn.aijiamuyingfang.weapp.manager.R;
import cn.aijiamuyingfang.weapp.manager.access.server.impl.CouponControllerClient;
import cn.aijiamuyingfang.weapp.manager.commons.CommonApp;
import cn.aijiamuyingfang.weapp.manager.commons.Constant;
import cn.aijiamuyingfang.weapp.manager.commons.activity.BaseActivity;
import cn.aijiamuyingfang.weapp.manager.commons.utils.ToastUtils;
import cn.aijiamuyingfang.weapp.manager.fragment.VoucherItemFragment;
import cn.aijiamuyingfang.weapp.manager.recycleadapter.VoucherItemAdapter;
import cn.aijiamuyingfang.weapp.manager.widgets.recycleview.adapter.OnItemClickListener;
import cn.aijiamuyingfang.weapp.manager.widgets.ClearEditText;
import cn.aijiamuyingfang.weapp.manager.widgets.WeToolBar;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class GoodVoucherActionActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    WeToolBar mToolBar;
    @BindView(R.id.voucher_name)
    ClearEditText mNameEditText;

    @BindView(R.id.voucher_score)
    ClearEditText mScoreEditText;

    @BindView(R.id.voucher_description)
    ClearEditText mDescriptionEditText;

    @BindView(R.id.refreshable_view)
    RecyclerView mRecyclerView;

    private VoucherItemAdapter mAdapter;

    private GoodVoucher mGoodVoucher;
    private ArrayList<VoucherItem> mVoucheritemList;

    private CouponControllerApi couponControllerApi = new CouponControllerClient();

    @Override
    protected void init() {
        initAdapter();
        initData();
        initToolBar();
    }

    private void initToolBar() {
        if (mGoodVoucher != null) {
            mToolBar.setRightButtonText("删除");
            mToolBar.setRightButtonOnClickListener(v -> couponControllerApi.deprecateGoodVoucher(CommonApp.getApplication().getUserToken(), mGoodVoucher.getId()).subscribe(responseBean -> {
                        if (ResponseCode.OK.getCode().equals(responseBean.getCode())) {
                            GoodVoucherActionActivity.this.finish();
                        } else {
                            ToastUtils.showSafeToast(GoodVoucherActionActivity.this, "删除兑换券失败");
                        }
                    })
            );
        }
    }

    private void initAdapter() {
        mAdapter = new VoucherItemAdapter(this, new ArrayList<>());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.HORIZONTAL));
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(GoodVoucherActionActivity.this, VoucherItemActionActivity.class);
                intent.putExtra(Constant.INTENT_VOUCHERITEM, mAdapter.getData(position));
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    private void initData() {
        Intent intent = getIntent();
        mGoodVoucher = intent.getParcelableExtra(Constant.INTENT_GOODVOUCHER);
        if (mGoodVoucher != null) {
            initVoucherItemList(mGoodVoucher);
            mNameEditText.setText(mGoodVoucher.getName());
            mScoreEditText.setText(mGoodVoucher.getScore() + "");
            mDescriptionEditText.setText(mGoodVoucher.getDescription());
            mAdapter.clearData();
            mAdapter.setDatas(mVoucheritemList);
        }
    }

    private void initVoucherItemList(GoodVoucher goodVoucher) {
        List<String> voucheritemidList = goodVoucher.getVoucheritemIdList();
        for (String itemid : voucheritemidList) {
            couponControllerApi.getVoucherItem(CommonApp.getApplication().getUserToken(), itemid).subscribe(responseBean -> {
                if (ResponseCode.OK.getCode().equals(responseBean.getCode())) {
                    this.mVoucheritemList.add(responseBean.getData());
                }
            });
        }
    }

    @OnClick({R.id.save_goodvoucher, R.id.add_voucher_item})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.save_goodvoucher:
                saveGoodVoucher();
                break;
            case R.id.add_voucher_item:
                selectVoucherItem();
                break;
            default:
                break;
        }
    }

    private void saveGoodVoucher() {
        GoodVoucher goodvoucher = new GoodVoucher();
        goodvoucher.setName(mNameEditText.getText().toString());
        goodvoucher.setDescription(mDescriptionEditText.getText().toString());
        goodvoucher.setScore(Integer.parseInt(mScoreEditText.getText().toString()));
        List<String> voucheritemidList = new ArrayList<>();
        for (VoucherItem item : mVoucheritemList) {
            voucheritemidList.add(item.getId());
        }
        goodvoucher.setVoucheritemIdList(voucheritemidList);
        couponControllerApi.createGoodVoucher(CommonApp.getApplication().getUserToken(), goodvoucher).subscribe(responseBean -> {
            if (ResponseCode.OK.getCode().equals(responseBean.getCode())) {
                GoodVoucherActionActivity.this.finish();
            } else {
                ToastUtils.showSafeToast(GoodVoucherActionActivity.this, "创建兑换券失败");
            }
        });
    }

    private void selectVoucherItem() {
        Intent intent = new Intent(this, FragmentContainerActivity.class);
        intent.putExtra(Constant.INTENT_FRAGMENT_NAME, VoucherItemFragment.class.getName());
        intent.putExtra(Constant.INTENT_FRAGMENT_FROM, GoodVoucherActionActivity.class.getName());
        intent.putExtra(Constant.INTENT_SELECTED_VOUCHERITEM, mVoucheritemList);
        startActivityForResult(intent, 0);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ArrayList<VoucherItem> voucheritemList = data.getParcelableArrayListExtra(Constant.INTENT_SELECTED_VOUCHERITEM);
        mAdapter.clearData();
        mVoucheritemList = voucheritemList;
        mAdapter.setDatas(mVoucheritemList);
    }

    @Override
    protected int getContentResourceId() {
        return R.layout.activity_add_goodvoucher;
    }
}