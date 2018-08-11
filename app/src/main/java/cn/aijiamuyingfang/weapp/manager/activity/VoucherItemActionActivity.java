package cn.aijiamuyingfang.weapp.manager.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.aijiamuyingfang.client.rest.api.CouponControllerApi;
import cn.aijiamuyingfang.client.rest.api.GoodControllerApi;
import cn.aijiamuyingfang.commons.domain.coupon.VoucherItem;
import cn.aijiamuyingfang.commons.domain.goods.Good;
import cn.aijiamuyingfang.commons.domain.response.ResponseBean;
import cn.aijiamuyingfang.commons.domain.response.ResponseCode;
import cn.aijiamuyingfang.commons.utils.StringUtils;
import cn.aijiamuyingfang.weapp.manager.R;
import cn.aijiamuyingfang.weapp.manager.access.server.impl.CouponControllerClient;
import cn.aijiamuyingfang.weapp.manager.access.server.impl.GoodControllerClient;
import cn.aijiamuyingfang.weapp.manager.access.server.utils.RxJavaUtils;
import cn.aijiamuyingfang.weapp.manager.commons.CommonApp;
import cn.aijiamuyingfang.weapp.manager.commons.Constant;
import cn.aijiamuyingfang.weapp.manager.commons.activity.BaseActivity;
import cn.aijiamuyingfang.weapp.manager.commons.utils.ToastUtils;
import cn.aijiamuyingfang.weapp.manager.widgets.ClearEditText;
import cn.aijiamuyingfang.weapp.manager.widgets.GlideUtils;
import cn.aijiamuyingfang.weapp.manager.widgets.WeToolBar;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class VoucherItemActionActivity extends BaseActivity {
    private static final String TAG = VoucherItemActionActivity.class.getName();
    @BindView(R.id.toolbar)
    WeToolBar mToolBar;
    @BindView(R.id.item_name)
    ClearEditText mItemNameTV;
    @BindView(R.id.good_name)
    TextView mGoodNameTextView;
    @BindView(R.id.good_cover)
    ImageView mGoodCoverIV;
    @BindView(R.id.item_score)
    ClearEditText mHolderThresholdTV;
    @BindView(R.id.item_description)
    ClearEditText mItemDescription;

    private VoucherItem mCurVoucherItem;
    private Good mCurGood;
    private CouponControllerApi couponControllerApi = new CouponControllerClient();
    private List<Disposable> couponDisposableList = new ArrayList<>();
    private GoodControllerApi goodControllerApi = new GoodControllerClient();
    private List<Disposable> goodDisposableList = new ArrayList<>();

    @Override
    protected void init() {
        initData();
        initToolBar();
    }

    private void initData() {
        Intent intent = getIntent();
        Good good = intent.getParcelableExtra(Constant.INTENT_GOOD);
        if (good != null) {
            setGood(good);
            return;
        }
        mCurVoucherItem = intent.getParcelableExtra(Constant.INTENT_VOUCHERITEM);
        if (null == this.mCurVoucherItem) {
            return;
        }
        String goodId = mCurVoucherItem.getGoodid();
        mItemNameTV.setText(mCurVoucherItem.getName());
        mHolderThresholdTV.setText(mCurVoucherItem.getScore() + "");
        mItemDescription.setText(mCurVoucherItem.getDescription());

        if (null == mCurGood && StringUtils.hasContent(goodId)) {
            goodControllerApi.getGood(CommonApp.getApplication().getUserToken(), goodId).subscribe(new Observer<ResponseBean<Good>>() {
                @Override
                public void onSubscribe(Disposable d) {
                    goodDisposableList.add(d);
                }

                @Override
                public void onNext(ResponseBean<Good> responseBean) {
                    if (ResponseCode.OK.getCode().equals(responseBean.getCode())) {
                        setGood(responseBean.getData());
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
        }

    }

    private void setGood(Good good) {
        if (null == good) {
            return;
        }
        mCurGood = good;
        mGoodNameTextView.setText(mCurGood.getName());
        GlideUtils.load(this, mCurGood.getCoverImg(), mGoodCoverIV);
    }


    private void initToolBar() {
        if (mCurVoucherItem != null) {
            mToolBar.setRightButtonText("删除");
            mToolBar.setRightButtonOnClickListener(v ->
                    couponControllerApi.deprecateVoucherItem(CommonApp.getApplication().getUserToken(), mCurVoucherItem.getId()).subscribe(new Observer<ResponseBean<Void>>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            couponDisposableList.add(d);
                        }

                        @Override
                        public void onNext(ResponseBean<Void> responseBean) {
                            if (ResponseCode.OK.getCode().equals(responseBean.getCode())) {
                                VoucherItemActionActivity.this.finish();
                            } else {
                                Log.e(TAG, responseBean.getMsg());
                                ToastUtils.showSafeToast(VoucherItemActionActivity.this, "删除兑换项失败");
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e(TAG, "deprecated voucher item failed", e);
                        }

                        @Override
                        public void onComplete() {
                            Log.i(TAG, "deprecated voucher item complete");
                        }
                    })
            );
        }

    }

    @OnClick({R.id.save_voucher_item})
    public void onClick(View view) {
        saveVoucherItem();
    }

    private void saveVoucherItem() {
        VoucherItem item = new VoucherItem();
        item.setGoodid(mCurGood.getId());
        item.setName(mItemNameTV.getText().toString());
        item.setDescription(mItemDescription.getText().toString());
        item.setScore(Integer.valueOf(mHolderThresholdTV.getText().toString()));
        if (mCurVoucherItem != null) {
            item.setId(mCurVoucherItem.getId());
        }
        couponControllerApi.createVoucherItem(CommonApp.getApplication().getUserToken(), item).subscribe(new Observer<ResponseBean<VoucherItem>>() {
            @Override
            public void onSubscribe(Disposable d) {
                couponDisposableList.add(d);
            }

            @Override
            public void onNext(ResponseBean<VoucherItem> responseBean) {
                if (ResponseCode.OK.getCode().equals(responseBean.getCode())) {
                    VoucherItemActionActivity.this.finish();
                } else {
                    Log.e(TAG, responseBean.getMsg());
                    ToastUtils.showSafeToast(VoucherItemActionActivity.this, "保存兑换项失败");
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "create voucher item failed", e);
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "create voucher item complete");
            }
        });
    }

    @Override
    protected int getContentResourceId() {
        return R.layout.activity_voucher_item_detail;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxJavaUtils.dispose(couponDisposableList);
        RxJavaUtils.dispose(goodDisposableList);
    }
}