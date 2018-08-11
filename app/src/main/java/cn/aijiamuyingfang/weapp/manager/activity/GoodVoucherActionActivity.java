package cn.aijiamuyingfang.weapp.manager.activity;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.aijiamuyingfang.client.rest.api.CouponControllerApi;
import cn.aijiamuyingfang.commons.domain.coupon.GoodVoucher;
import cn.aijiamuyingfang.commons.domain.coupon.VoucherItem;
import cn.aijiamuyingfang.commons.domain.response.ResponseBean;
import cn.aijiamuyingfang.commons.domain.response.ResponseCode;
import cn.aijiamuyingfang.weapp.manager.FragmentContainerActivity;
import cn.aijiamuyingfang.weapp.manager.R;
import cn.aijiamuyingfang.weapp.manager.access.server.impl.CouponControllerClient;
import cn.aijiamuyingfang.weapp.manager.access.server.utils.RxJavaUtils;
import cn.aijiamuyingfang.weapp.manager.commons.CommonApp;
import cn.aijiamuyingfang.weapp.manager.commons.Constant;
import cn.aijiamuyingfang.weapp.manager.commons.activity.BaseActivity;
import cn.aijiamuyingfang.weapp.manager.commons.utils.ToastUtils;
import cn.aijiamuyingfang.weapp.manager.fragment.VoucherItemFragment;
import cn.aijiamuyingfang.weapp.manager.recycleadapter.VoucherItemAdapter;
import cn.aijiamuyingfang.weapp.manager.widgets.ClearEditText;
import cn.aijiamuyingfang.weapp.manager.widgets.WeToolBar;
import cn.aijiamuyingfang.weapp.manager.widgets.recycleview.adapter.OnItemClickListener;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class GoodVoucherActionActivity extends BaseActivity {
    private static final String TAG = GoodVoucherActionActivity.class.getName();
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

    private GoodVoucher mCurGoodVoucher;
    private List<VoucherItem> mVoucheritemList = new ArrayList<>();

    private CouponControllerApi couponControllerApi = new CouponControllerClient();
    private List<Disposable> couponDisposableList = new ArrayList<>();

    @Override
    protected void init() {
        initAdapter();
        initData();
        initToolBar();
    }

    private void initToolBar() {
        if (mCurGoodVoucher != null) {
            mToolBar.setRightButtonText("删除");
            mToolBar.setRightButtonOnClickListener(v ->
                    couponControllerApi.deprecateGoodVoucher(CommonApp.getApplication().getUserToken(), mCurGoodVoucher.getId()).subscribe(new Observer<ResponseBean<Void>>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            couponDisposableList.add(d);
                        }

                        @Override
                        public void onNext(ResponseBean<Void> responseBean) {
                            if (ResponseCode.OK.getCode().equals(responseBean.getCode())) {
                                GoodVoucherActionActivity.this.finish();
                            } else {
                                Log.e(TAG, responseBean.getMsg());
                                ToastUtils.showSafeToast(GoodVoucherActionActivity.this, "删除兑换券失败");
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e(TAG, "deprecated good voucher failed", e);
                        }

                        @Override
                        public void onComplete() {
                            Log.i(TAG, "deprecated good voucher complete");
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
        mCurGoodVoucher = intent.getParcelableExtra(Constant.INTENT_GOODVOUCHER);
        if (mCurGoodVoucher != null) {
            initVoucherItemList(mCurGoodVoucher);
            mNameEditText.setText(mCurGoodVoucher.getName());
            mScoreEditText.setText(mCurGoodVoucher.getScore() + "");
            mDescriptionEditText.setText(mCurGoodVoucher.getDescription());
            mAdapter.clearData();
            mAdapter.setDatas(mVoucheritemList);
        }
    }

    private void initVoucherItemList(GoodVoucher goodVoucher) {
        List<String> voucheritemidList = goodVoucher.getVoucheritemIdList();
        GoodVoucherActionActivity.this.mVoucheritemList.clear();
        for (String itemid : voucheritemidList) {
            couponControllerApi.getVoucherItem(CommonApp.getApplication().getUserToken(), itemid).subscribe(new Observer<ResponseBean<VoucherItem>>() {
                @Override
                public void onSubscribe(Disposable d) {
                    couponDisposableList.add(d);
                }

                @Override
                public void onNext(ResponseBean<VoucherItem> responseBean) {
                    if (ResponseCode.OK.getCode().equals(responseBean.getCode())) {
                        GoodVoucherActionActivity.this.mVoucheritemList.add(responseBean.getData());
                        GoodVoucherActionActivity.this.mAdapter.setDatas(GoodVoucherActionActivity.this.mVoucheritemList);
                    } else {
                        Log.e(TAG, responseBean.getMsg());
                    }
                }

                @Override
                public void onError(Throwable e) {
                    Log.e(TAG, "get voucher item failed", e);
                }

                @Override
                public void onComplete() {
                    Log.i(TAG, "get voucher item complete");
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
        if (mCurGoodVoucher != null) {
            goodvoucher.setId(mCurGoodVoucher.getId());
        }
        couponControllerApi.createGoodVoucher(CommonApp.getApplication().getUserToken(), goodvoucher).subscribe(new Observer<ResponseBean<GoodVoucher>>() {
            @Override
            public void onSubscribe(Disposable d) {
                couponDisposableList.add(d);
            }

            @Override
            public void onNext(ResponseBean<GoodVoucher> responseBean) {
                if (ResponseCode.OK.getCode().equals(responseBean.getCode())) {
                    GoodVoucherActionActivity.this.finish();
                } else {
                    Log.e(TAG, responseBean.getMsg());
                    ToastUtils.showSafeToast(GoodVoucherActionActivity.this, "创建兑换券失败");
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "create good voucher failed", e);
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "create good voucher complete");
            }
        });
    }

    private void selectVoucherItem() {
        Intent intent = new Intent(this, FragmentContainerActivity.class);
        intent.putExtra(Constant.INTENT_FRAGMENT_NAME, VoucherItemFragment.class.getName());
        intent.putExtra(Constant.INTENT_FRAGMENT_FROM, GoodVoucherActionActivity.class.getName());
        intent.putParcelableArrayListExtra(Constant.INTENT_SELECTED_VOUCHERITEM, (ArrayList<? extends Parcelable>) mVoucheritemList);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxJavaUtils.dispose(couponDisposableList);
    }
}