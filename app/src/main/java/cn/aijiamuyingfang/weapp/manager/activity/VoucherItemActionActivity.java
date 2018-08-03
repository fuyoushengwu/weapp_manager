package cn.aijiamuyingfang.weapp.manager.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import cn.aijiamuyingfang.client.rest.api.CouponControllerApi;
import cn.aijiamuyingfang.client.rest.api.GoodControllerApi;
import cn.aijiamuyingfang.commons.domain.coupon.VoucherItem;
import cn.aijiamuyingfang.commons.domain.goods.Good;
import cn.aijiamuyingfang.commons.domain.response.ResponseCode;
import cn.aijiamuyingfang.commons.utils.StringUtils;
import cn.aijiamuyingfang.weapp.manager.R;
import cn.aijiamuyingfang.weapp.manager.access.server.impl.CouponControllerClient;
import cn.aijiamuyingfang.weapp.manager.access.server.impl.GoodControllerClient;
import cn.aijiamuyingfang.weapp.manager.commons.CommonApp;
import cn.aijiamuyingfang.weapp.manager.commons.Constant;
import cn.aijiamuyingfang.weapp.manager.commons.activity.BaseActivity;
import cn.aijiamuyingfang.weapp.manager.commons.utils.ToastUtils;
import cn.aijiamuyingfang.weapp.manager.widgets.ClearEditText;
import cn.aijiamuyingfang.weapp.manager.widgets.GlideUtils;
import cn.aijiamuyingfang.weapp.manager.widgets.WeToolBar;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class VoucherItemActionActivity extends BaseActivity {
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

    private VoucherItem mItem;
    private Good mCurGood;
    private CouponControllerApi couponControllerApi = new CouponControllerClient();
    private GoodControllerApi goodControllerApi = new GoodControllerClient();

    @Override
    protected void init() {
        initData();
        initToolBar();
    }

    private void initToolBar() {
        if (mItem != null) {
            mToolBar.setRightButtonText("删除");
            mToolBar.setRightButtonOnClickListener(v ->
                    couponControllerApi.deprecateVoucherItem(CommonApp.getApplication().getUserToken(), mItem.getId()).subscribe(responseBean -> {
                        if (ResponseCode.OK.getCode().equals(responseBean.getCode())) {
                            VoucherItemActionActivity.this.finish();
                        } else {
                            ToastUtils.showSafeToast(VoucherItemActionActivity.this, "删除兑换项失败");
                        }
                    })
            );
        }

    }

    private void initData() {
        Intent intent = getIntent();
        this.mCurGood = intent.getParcelableExtra(Constant.INTENT_GOOD);
        if (this.mCurGood != null) {
            return;
        }
        mItem = intent.getParcelableExtra(Constant.INTENT_VOUCHERITEM);
        if (null == this.mItem) {
            return;
        }
        String goodId = mItem.getGoodid();
        mItemNameTV.setText(mItem.getName());
        mHolderThresholdTV.setText(mItem.getScore() + "");
        mItemDescription.setText(mItem.getDescription());

        if (StringUtils.hasContent(goodId)) {
            goodControllerApi.getGood(CommonApp.getApplication().getUserToken(), goodId).subscribe(responseBean -> {
                if (ResponseCode.OK.getCode().equals(responseBean.getCode())) {
                    VoucherItemActionActivity.this.mCurGood = responseBean.getData();
                    if (VoucherItemActionActivity.this.mCurGood != null) {
                        mGoodNameTextView.setText(VoucherItemActionActivity.this.mCurGood.getName());
                        GlideUtils.load(VoucherItemActionActivity.this, VoucherItemActionActivity.this.mCurGood.getCoverImg(), mGoodCoverIV);
                    }
                }
            });
        }

    }

    @OnClick({R.id.save_goodvoucher_item})
    public void onClick(View view) {
        VoucherItem item = new VoucherItem();
        item.setGoodid(mCurGood.getId());
        item.setName(mItemNameTV.getText().toString());
        item.setDescription(mItemDescription.getText().toString());
        item.setScore(Integer.valueOf(mHolderThresholdTV.getText().toString()));
        couponControllerApi.createVoucherItem(CommonApp.getApplication().getUserToken(), item).subscribe(responseBean -> {
            if (ResponseCode.OK.getCode().equals(responseBean.getCode())) {
                VoucherItemActionActivity.this.finish();
            } else {
                ToastUtils.showSafeToast(VoucherItemActionActivity.this, "保存兑换项失败");
            }
        });
    }

    @Override
    protected int getContentResourceId() {
        return R.layout.activity_voucher_item_detail;
    }
}