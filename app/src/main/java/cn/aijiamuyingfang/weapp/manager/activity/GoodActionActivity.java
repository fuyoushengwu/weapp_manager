package cn.aijiamuyingfang.weapp.manager.activity;

import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import cn.aijiamuyingfang.client.rest.api.CouponControllerApi;
import cn.aijiamuyingfang.client.rest.api.GoodControllerApi;
import cn.aijiamuyingfang.commons.domain.coupon.GoodVoucher;
import cn.aijiamuyingfang.commons.domain.goods.Good;
import cn.aijiamuyingfang.commons.domain.goods.GoodDetail;
import cn.aijiamuyingfang.commons.domain.goods.ShelfLife;
import cn.aijiamuyingfang.commons.domain.response.ResponseCode;
import cn.aijiamuyingfang.commons.utils.StringUtils;
import cn.aijiamuyingfang.weapp.manager.FragmentContainerActivity;
import cn.aijiamuyingfang.weapp.manager.R;
import cn.aijiamuyingfang.weapp.manager.access.server.impl.CouponControllerClient;
import cn.aijiamuyingfang.weapp.manager.access.server.impl.GoodControllerClient;
import cn.aijiamuyingfang.weapp.manager.commons.CommonApp;
import cn.aijiamuyingfang.weapp.manager.commons.Constant;
import cn.aijiamuyingfang.weapp.manager.commons.activity.BaseActivity;
import cn.aijiamuyingfang.weapp.manager.fragment.GoodVoucherFragment;
import cn.aijiamuyingfang.weapp.manager.recycleadapter.GoodVoucherAdapter;
import cn.aijiamuyingfang.weapp.manager.widgets.ClearEditText;
import cn.aijiamuyingfang.weapp.manager.widgets.EditableImageView;
import cn.aijiamuyingfang.weapp.manager.widgets.GlideUtils;
import cn.aijiamuyingfang.weapp.manager.widgets.WeToolBar;
import cn.aijiamuyingfang.weapp.manager.widgets.recycleview.adapter.OnItemClickListener;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class GoodActionActivity extends BaseActivity {

    @BindView(R.id.et_good_name)
    ClearEditText mNameEditText;

    @BindView(R.id.et_good_level)
    ClearEditText mLevelEditText;

    @BindView(R.id.et_bar_code)
    ClearEditText mBarCodeEditText;

    @BindView(R.id.et_good_price)
    ClearEditText mPriceEditText;

    @BindView(R.id.et_good_pack)
    ClearEditText mPackEditText;

    @BindView(R.id.et_good_count)
    ClearEditText mCountEditText;

    @BindView(R.id.et_good_lifetime)
    ClearEditText mLifeTimeEditText;

    @BindView(R.id.iv_good_logo)
    EditableImageView mLogoImageView;

    @BindView(R.id.iv_good_detail_1)
    EditableImageView mDetail1ImageView;

    @BindView(R.id.iv_good_detail_2)
    EditableImageView mDetail2ImageView;

    @BindView(R.id.iv_good_detail_3)
    EditableImageView mDetail3ImageView;

    @BindView(R.id.iv_good_detail_4)
    EditableImageView mDetail4ImageView;

    @BindView(R.id.iv_good_detail_5)
    EditableImageView mDetail5ImageView;

    @BindView(R.id.iv_good_detail_6)
    EditableImageView mDetail6ImageView;

    @BindView(R.id.toolbar)
    WeToolBar mToolBar;

    @BindView(R.id.et_good_score)
    ClearEditText mGoodScoreEditText;

    @BindView(R.id.refreshable_view)
    RecyclerView mRecyclerView;
    private GoodVoucherAdapter mAdapter;

    /**
     * 当前商品
     */
    private Good mCurGood;

    /**
     * 商品关联的抵用券
     */
    private GoodVoucher mGoodVoucher;

    private GoodControllerApi goodControllerApi = new GoodControllerClient();
    private CouponControllerApi couponControllerApi = new CouponControllerClient();


    @OnClick({R.id.btn_good, R.id.create_voucheritem, R.id.add_goodvoucher})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_good:
                saveGood();
                break;
            case R.id.create_voucheritem:
                Intent intent = new Intent(GoodActionActivity.this, VoucherItemActionActivity.class);
                intent.putExtra(Constant.INTENT_GOOD, mCurGood);
                startActivity(intent);
                break;
            case R.id.add_goodvoucher:
                addGoodVoucher();
                break;
            default:
                break;
        }
    }

    private void addGoodVoucher() {
        Intent intent = new Intent(this, FragmentContainerActivity.class);
        intent.putExtra(Constant.INTENT_FRAGMENT_NAME, GoodVoucherFragment.class.getName());
        intent.putExtra(Constant.INTENT_FRAGMENT_FROM, GoodActionActivity.class.getName());
        intent.putExtra(Constant.INTENT_SELECTED_GOODVOUCHER, mGoodVoucher);
        startActivityForResult(intent, Constant.REQUEST_GOOD_HOLDERCART);
    }


    private void saveGood() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                String goodName = mNameEditText.getText().toString();
                if (StringUtils.isEmpty(goodName)) {
                    return;
                }
                MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
                requestBodyBuilder.addFormDataPart("name", goodName);
                requestBodyBuilder.addFormDataPart("count", mCountEditText.getText().toString());
                requestBodyBuilder.addFormDataPart("barCode", mBarCodeEditText.getText().toString());
                requestBodyBuilder.addFormDataPart("price", mPriceEditText.getText().toString());
                requestBodyBuilder.addFormDataPart("marketPrice", mPriceEditText.getText().toString());
                requestBodyBuilder.addFormDataPart("saleCount", 0 + "");
                requestBodyBuilder.addFormDataPart("level", mLevelEditText.getText().toString());
                requestBodyBuilder.addFormDataPart("unit", mPackEditText.getText().toString());
                requestBodyBuilder.addFormDataPart("lifetime", mLifeTimeEditText.getText().toString());
                requestBodyBuilder.addFormDataPart("score", mGoodScoreEditText.getText().toString());

                File logo = mLogoImageView.getImageFileSync();
                RequestBody requestLogo = RequestBody.create(MultipartBody.FORM, logo);
                requestBodyBuilder.addFormDataPart("logo", logo.getName(), requestLogo);


                List<File> detailImageList = new ArrayList<>();
                detailImageList.add(mDetail1ImageView.getImageFileSync());
                detailImageList.add(mDetail2ImageView.getImageFileSync());
                detailImageList.add(mDetail3ImageView.getImageFileSync());
                detailImageList.add(mDetail4ImageView.getImageFileSync());
                detailImageList.add(mDetail5ImageView.getImageFileSync());
                detailImageList.add(mDetail6ImageView.getImageFileSync());
                for (File detailImageFile : detailImageList) {
                    RequestBody requestDetail = RequestBody.create(MultipartBody.FORM, detailImageFile);
                    requestBodyBuilder.addFormDataPart("detailImages", detailImageFile.getName(), requestDetail);
                }

                if (mCurGood != null) {
                    requestBodyBuilder.addFormDataPart("id", mCurGood.getId() + "");
                }

                if (mGoodVoucher != null) {
                    requestBodyBuilder.addFormDataPart("goodvoucher.id", mGoodVoucher.getId() + "");
                }
                goodControllerApi.createGood(CommonApp.getApplication().getUserToken(), requestBodyBuilder.build()).subscribe(responseBean -> {
                    if (ResponseCode.OK.getCode().equals(responseBean.getCode())) {
                        GoodActionActivity.this.finish();
                    }
                });

            }
        };
        thread.start();
    }

    @Override
    protected void init() {
        initAdapter();
        initData();
    }

    private void initAdapter() {
        mAdapter = new GoodVoucherAdapter(this, new ArrayList<>());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.HORIZONTAL));

        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(GoodActionActivity.this, GoodVoucherActionActivity.class);
                intent.putExtra(Constant.INTENT_GOODVOUCHER, mAdapter.getData(position));
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    private void deleteGood(String goodid) {
        goodControllerApi.deprecateGood(CommonApp.getApplication().getUserToken(), goodid).subscribe(voidResponseBean -> {
            if (ResponseCode.OK.getCode().equals(voidResponseBean.getCode())) {
                GoodActionActivity.this.finish();
            }
        });
    }

    private void initData() {
        Intent intent = getIntent();
        mCurGood = intent.getParcelableExtra(Constant.INTENT_GOOD);
        if (mCurGood != null) {
            mToolBar.setRightButtonText("删除");
            mToolBar.setRightButtonOnClickListener(v -> deleteGood(mCurGood.getId()));
            String voucherId = mCurGood.getVoucherId();
            if (StringUtils.hasContent(voucherId)) {
                setGoodVoucher(voucherId);
            }
            mNameEditText.setText(mCurGood.getName());
            mPackEditText.setText(mCurGood.getPack());
            mLevelEditText.setText(mCurGood.getLevel());
            mBarCodeEditText.setText(mCurGood.getBarcode());
            mPriceEditText.setText(mCurGood.getPrice() + "");
            mCountEditText.setText(mCurGood.getCount() + "");
            mGoodScoreEditText.setText(mCurGood.getScore() + "");
            GlideUtils.load(GoodActionActivity.this, mCurGood.getCoverImg(), mLogoImageView);

            goodControllerApi.getGoodDetail(CommonApp.getApplication().getUserToken(), mCurGood.getId()).subscribe(goodDetailResponseBean -> {
                if (ResponseCode.OK.getCode().equals(goodDetailResponseBean.getCode())) {
                    GoodDetail goodDetail = goodDetailResponseBean.getData();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    ShelfLife shelfLife = goodDetail.getLifetime();
                    mLifeTimeEditText.setText(dateFormat.format(shelfLife.getStart()) + "-" + dateFormat.format(shelfLife.getEnd()));

                    EditableImageView[] imageViews = new EditableImageView[]{mDetail1ImageView, mDetail2ImageView, mDetail3ImageView,
                            mDetail4ImageView, mDetail5ImageView, mDetail6ImageView,};
                    for (int i = 0; i < goodDetail.getDetailImgList().size(); i++) {
                        GlideUtils.load(GoodActionActivity.this, goodDetail.getDetailImgList().get(i), imageViews[i]);
                    }
                }
            });
        }
    }

    private void setGoodVoucher(String voucherid) {
        couponControllerApi.getGoodVoucher(CommonApp.getApplication().getUserToken(), voucherid).subscribe(responseBean -> {
            if (ResponseCode.OK.getCode().equals(responseBean.getCode())) {
                mAdapter.getDatas().add(mGoodVoucher);
                mAdapter.notifyDataSetChanged();
            }
        });
    }


    @Override
    protected int getContentResourceId() {
        return R.layout.activity_add_good;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (Constant.REQUEST_GOOD_HOLDERCART == requestCode) {
            mGoodVoucher = data.getParcelableExtra(Constant.INTENT_SELECTED_GOODVOUCHER);
            mAdapter.clearData();
            if (mGoodVoucher != null) {
                mAdapter.getDatas().add(mGoodVoucher);
            }
            mAdapter.notifyDataSetChanged();
            return;
        }
        //在拍照、选取照片、裁剪Activity结束后，调用的方法
        List<EditableImageView> imageViewList = new ArrayList<>();
        imageViewList.add(mLogoImageView);
        imageViewList.add(mDetail1ImageView);
        imageViewList.add(mDetail2ImageView);
        imageViewList.add(mDetail3ImageView);
        imageViewList.add(mDetail4ImageView);
        imageViewList.add(mDetail5ImageView);
        imageViewList.add(mDetail6ImageView);
        for (EditableImageView imageView : imageViewList) {
            if (imageView != null && imageView.isInEdit()) {
                imageView.onActivityResult(requestCode, resultCode, data);
            }
        }
    }
}