package cn.aijiamuyingfang.weapp.manager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import cn.aijiamuyingfang.client.rest.api.ClassifyControllerApi;
import cn.aijiamuyingfang.client.rest.api.CouponControllerApi;
import cn.aijiamuyingfang.client.rest.api.GoodControllerApi;
import cn.aijiamuyingfang.commons.domain.coupon.GoodVoucher;
import cn.aijiamuyingfang.commons.domain.goods.Good;
import cn.aijiamuyingfang.commons.domain.goods.GoodDetail;
import cn.aijiamuyingfang.commons.domain.goods.ShelfLife;
import cn.aijiamuyingfang.commons.domain.response.ResponseBean;
import cn.aijiamuyingfang.commons.domain.response.ResponseCode;
import cn.aijiamuyingfang.commons.utils.StringUtils;
import cn.aijiamuyingfang.weapp.manager.FragmentContainerActivity;
import cn.aijiamuyingfang.weapp.manager.R;
import cn.aijiamuyingfang.weapp.manager.access.server.impl.ClassifyControllerClient;
import cn.aijiamuyingfang.weapp.manager.access.server.impl.CouponControllerClient;
import cn.aijiamuyingfang.weapp.manager.access.server.impl.GoodControllerClient;
import cn.aijiamuyingfang.weapp.manager.access.server.utils.RxJavaUtils;
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
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class GoodActionActivity extends BaseActivity {
    private static final String TAG = GoodActionActivity.class.getName();
    @BindView(R.id.toolbar)
    WeToolBar mToolBar;
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
    @BindView(R.id.et_good_lifetime_start)
    EditText mLifeTimeStartEditText;
    @BindView(R.id.et_good_lifetime_end)
    EditText mLifeTimeEndEditText;
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
    @BindView(R.id.et_good_score)
    ClearEditText mGoodScoreEditText;
    @BindView(R.id.refreshable_view)
    RecyclerView mRecyclerView;
    private GoodVoucherAdapter mAdapter;

    /**
     * 当前子条目ID
     */
    private String mCurSubClasifyId;
    /**
     * 当前商品
     */
    private Good mCurGood;

    /**
     * 商品关联的抵用券
     */
    private GoodVoucher mGoodVoucher;

    private ClassifyControllerApi classifyControllerApi = new ClassifyControllerClient();
    private GoodControllerApi goodControllerApi = new GoodControllerClient();
    private CouponControllerApi couponControllerApi = new CouponControllerClient();
    private List<Disposable> disposableList = new ArrayList<>();
    private Calendar mCalendar;
    private CustomDatePickerDialogFragment mLifeTimeStartDialog;
    private CustomDatePickerDialogFragment mLifeTimeEndDialog;

    @OnClick({R.id.save_good, R.id.save_voucheritem, R.id.add_goodvoucher, R.id.et_good_lifetime_start, R.id.et_good_lifetime_end})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.save_good:
                saveGood();
                break;
            case R.id.save_voucheritem:
                Intent intent = new Intent(GoodActionActivity.this, VoucherItemActionActivity.class);
                intent.putExtra(Constant.INTENT_GOOD, mCurGood);
                startActivity(intent);
                break;
            case R.id.add_goodvoucher:
                addGoodVoucher();
                break;
            case R.id.et_good_lifetime_start:
            case R.id.et_good_lifetime_end:
                showDatePickDlg((EditText) view);
                break;
            default:
                break;
        }
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
                File logo = mLogoImageView.getImageFileSync();
                RequestBody requestLogo = RequestBody.create(MultipartBody.FORM, logo);
                requestBodyBuilder.addFormDataPart("coverImage", logo.getName(), requestLogo);


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


                requestBodyBuilder.addFormDataPart("name", goodName);
                requestBodyBuilder.addFormDataPart("count", mCountEditText.getText().toString());
                requestBodyBuilder.addFormDataPart("barcode", mBarCodeEditText.getText().toString());
                requestBodyBuilder.addFormDataPart("price", mPriceEditText.getText().toString());
                requestBodyBuilder.addFormDataPart("marketprice", mPriceEditText.getText().toString());
                requestBodyBuilder.addFormDataPart("level", mLevelEditText.getText().toString());
                requestBodyBuilder.addFormDataPart("pack", mPackEditText.getText().toString());
                String scoreStr = mGoodScoreEditText.getText().toString();
                requestBodyBuilder.addFormDataPart("score", StringUtils.hasContent(scoreStr) ? scoreStr : "0");
                requestBodyBuilder.addFormDataPart("lifetime.start", mLifeTimeStartEditText.getText().toString());
                requestBodyBuilder.addFormDataPart("lifetime.end", mLifeTimeEndEditText.getText().toString());


                if (mCurGood != null) {
                    requestBodyBuilder.addFormDataPart("id", mCurGood.getId() + "");
                }

                if (mGoodVoucher != null) {
                    requestBodyBuilder.addFormDataPart("voucherId", mGoodVoucher.getId() + "");
                }
                goodControllerApi.createGood(CommonApp.getApplication().getUserToken(), requestBodyBuilder.build()).subscribe(
                        new Observer<ResponseBean<Good>>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                disposableList.add(d);
                            }

                            @Override
                            public void onNext(ResponseBean<Good> responseBean) {
                                if (ResponseCode.OK.getCode().equals(responseBean.getCode())) {
                                    Good createdGood = responseBean.getData();
                                    if (StringUtils.hasContent(mCurSubClasifyId) && createdGood !=
                                            null && StringUtils.hasContent(createdGood.getId())) {
                                        classifyControllerApi.addClassifyGood(CommonApp.getApplication().getUserToken(), mCurSubClasifyId, createdGood.getId())
                                                .subscribe(new Observer<ResponseBean<Void>>() {
                                                    @Override
                                                    public void onSubscribe(Disposable d) {
                                                        disposableList.add(d);
                                                    }

                                                    @Override
                                                    public void onNext(ResponseBean<Void> value) {
                                                        GoodActionActivity.this.finish();
                                                    }

                                                    @Override
                                                    public void onError(Throwable e) {
                                                        Log.e(TAG, "add classify good failed", e);
                                                    }

                                                    @Override
                                                    public void onComplete() {
                                                        Log.i(TAG, "add classify good complete");
                                                    }
                                                });
                                    } else {
                                        GoodActionActivity.this.finish();
                                    }
                                } else {
                                    Log.e(TAG, responseBean.getMsg());
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(TAG, "create good failed", e);
                            }

                            @Override
                            public void onComplete() {
                                Log.i(TAG, "create good complete");
                            }
                        });
            }
        };
        thread.start();
    }

    private void addGoodVoucher() {
        Intent intent = new Intent(this, FragmentContainerActivity.class);
        intent.putExtra(Constant.INTENT_FRAGMENT_NAME, GoodVoucherFragment.class.getName());
        intent.putExtra(Constant.INTENT_FRAGMENT_FROM, GoodActionActivity.class.getName());
        intent.putExtra(Constant.INTENT_SELECTED_GOODVOUCHER, mGoodVoucher);
        startActivityForResult(intent, Constant.REQUEST_GOOD_VOUCHER);
    }

    @OnFocusChange({R.id.et_good_lifetime_start, R.id.et_good_lifetime_end})
    public void onFocusChange(View view, boolean hasFocus) {
        switch (view.getId()) {
            case R.id.et_good_lifetime_start:
            case R.id.et_good_lifetime_end:
                if (hasFocus) {
                    showDatePickDlg((EditText) view);
                }
                break;
            default:
                break;
        }

    }

    protected void showDatePickDlg(EditText editText) {
        switch (editText.getId()) {
            case R.id.et_good_lifetime_start:
                if (null == mLifeTimeStartDialog) {
                    mLifeTimeStartDialog = createDatePickerDialog(editText);
                }
                mLifeTimeStartDialog.show(getFragmentManager(), CustomDatePickerDialogFragment.class.getSimpleName());
                break;
            case R.id.et_good_lifetime_end:
                if (null == mLifeTimeEndDialog) {
                    mLifeTimeEndDialog = createDatePickerDialog(editText);
                }
                mLifeTimeEndDialog.show(getFragmentManager(), CustomDatePickerDialogFragment.class.getSimpleName());
                break;
            default:
                break;
        }
    }

    private CustomDatePickerDialogFragment createDatePickerDialog(EditText editText) {
        if (null == mCalendar) {
            mCalendar = Calendar.getInstance();
            mCalendar.setTimeInMillis(System.currentTimeMillis());
            mCalendar.set(Calendar.HOUR_OF_DAY, 0);
            mCalendar.set(Calendar.MINUTE, 0);
            mCalendar.set(Calendar.SECOND, 0);
            mCalendar.set(Calendar.MILLISECOND, 0);
        }


        CustomDatePickerDialogFragment dialogFragment = new CustomDatePickerDialogFragment();
        dialogFragment.setOnSelectedDateListener((year, monthOfYear, dayOfMonth) -> {
            StringBuilder sb = new StringBuilder();
            sb.append(year).append("-");
            monthOfYear += 1;
            if (monthOfYear > 9) {
                sb.append(monthOfYear);
            } else {
                sb.append("0" + monthOfYear);
            }
            sb.append("-");
            if (dayOfMonth > 9) {
                sb.append(dayOfMonth);
            } else {
                sb.append("0" + dayOfMonth);
            }

            editText.setText(sb.toString());
        });

        Bundle bundle = new Bundle();
        bundle.putSerializable(CustomDatePickerDialogFragment.CURRENT_DATE, mCalendar);
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }


    @Override
    protected void init() {
        mLifeTimeStartEditText.setInputType(InputType.TYPE_NULL); //不显示系统输入键盘
        mLifeTimeEndEditText.setInputType(InputType.TYPE_NULL); //不显示系统输入键盘
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
        goodControllerApi.deprecateGood(CommonApp.getApplication().getUserToken(), goodid).subscribe(new Observer<ResponseBean<Void>>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposableList.add(d);
            }

            @Override
            public void onNext(ResponseBean<Void> responseBean) {
                if (ResponseCode.OK.getCode().equals(responseBean.getCode())) {
                    GoodActionActivity.this.finish();
                } else {
                    Log.e(TAG, responseBean.getMsg());
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "deprecated good failed", e);
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "deprecated good complete");
            }
        });
    }

    private void initData() {
        Intent intent = getIntent();
        mCurSubClasifyId = intent.getStringExtra(Constant.INTENT_SUB_CLASSIFY_ID);
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

            goodControllerApi.getGoodDetail(CommonApp.getApplication().getUserToken(), mCurGood.getId()).subscribe(new Observer<ResponseBean<GoodDetail>>() {
                @Override
                public void onSubscribe(Disposable d) {
                    disposableList.add(d);
                }

                @Override
                public void onNext(ResponseBean<GoodDetail> responseBean) {
                    if (ResponseCode.OK.getCode().equals(responseBean.getCode())) {
                        GoodDetail goodDetail = responseBean.getData();
                        ShelfLife shelfLife = goodDetail.getLifetime();
                        mLifeTimeStartEditText.setText(shelfLife.getStart());
                        mLifeTimeEndEditText.setText(shelfLife.getEnd());


                        EditableImageView[] imageViews = new EditableImageView[]{mDetail1ImageView, mDetail2ImageView, mDetail3ImageView,
                                mDetail4ImageView, mDetail5ImageView, mDetail6ImageView,};
                        for (int i = 0; i < goodDetail.getDetailImgList().size(); i++) {
                            GlideUtils.load(GoodActionActivity.this, goodDetail.getDetailImgList().get(i), imageViews[i]);
                        }
                    } else {
                        Log.e(TAG, responseBean.getMsg());
                    }
                }

                @Override
                public void onError(Throwable e) {
                    Log.e(TAG, "get good detail failed", e);
                }

                @Override
                public void onComplete() {
                    Log.i(TAG, "get good detail complete");
                }
            });
        }
    }

    private void setGoodVoucher(String voucherid) {
        couponControllerApi.getGoodVoucher(CommonApp.getApplication().getUserToken(), voucherid).subscribe(new Observer<ResponseBean<GoodVoucher>>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposableList.add(d);
            }

            @Override
            public void onNext(ResponseBean<GoodVoucher> responseBean) {
                if (ResponseCode.OK.getCode().equals(responseBean.getCode())) {
                    mGoodVoucher = responseBean.getData();
                    mAdapter.clearData();
                    mAdapter.setDatas(Arrays.asList(mGoodVoucher));
                } else {
                    Log.e(TAG, responseBean.getMsg());
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "get good voucher failed", e);
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "get good voucher complete");
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

        if (Constant.REQUEST_GOOD_VOUCHER == requestCode) {
            mGoodVoucher = data.getParcelableExtra(Constant.INTENT_SELECTED_GOODVOUCHER);
            mAdapter.clearData();
            if (mGoodVoucher != null) {
                mAdapter.setDatas(Arrays.asList(mGoodVoucher));
            }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxJavaUtils.dispose(disposableList);
    }
}
