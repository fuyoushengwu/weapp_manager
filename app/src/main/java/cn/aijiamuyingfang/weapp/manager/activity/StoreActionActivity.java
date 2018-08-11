package cn.aijiamuyingfang.weapp.manager.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.tencent.lbssearch.TencentSearch;
import com.tencent.lbssearch.httpresponse.BaseObject;
import com.tencent.lbssearch.httpresponse.HttpResponseListener;
import com.tencent.lbssearch.object.param.SuggestionParam;
import com.tencent.lbssearch.object.result.SuggestionResultObject;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.aijiamuyingfang.client.rest.api.StoreControllerApi;
import cn.aijiamuyingfang.commons.domain.goods.Store;
import cn.aijiamuyingfang.commons.domain.goods.WorkTime;
import cn.aijiamuyingfang.commons.domain.response.ResponseBean;
import cn.aijiamuyingfang.commons.domain.response.ResponseCode;
import cn.aijiamuyingfang.weapp.manager.R;
import cn.aijiamuyingfang.weapp.manager.access.server.impl.StoreControllerClient;
import cn.aijiamuyingfang.weapp.manager.access.server.utils.RxJavaUtils;
import cn.aijiamuyingfang.weapp.manager.bean.CityViewData;
import cn.aijiamuyingfang.weapp.manager.bean.CountyViewData;
import cn.aijiamuyingfang.weapp.manager.bean.ProvinceViewData;
import cn.aijiamuyingfang.weapp.manager.commons.CommonApp;
import cn.aijiamuyingfang.weapp.manager.commons.Constant;
import cn.aijiamuyingfang.weapp.manager.commons.activity.BaseActivity;
import cn.aijiamuyingfang.weapp.manager.commons.utils.JsonUtils;
import cn.aijiamuyingfang.weapp.manager.commons.utils.ToastUtils;
import cn.aijiamuyingfang.weapp.manager.widgets.ClearEditText;
import cn.aijiamuyingfang.weapp.manager.widgets.EditableImageView;
import cn.aijiamuyingfang.weapp.manager.widgets.GlideUtils;
import cn.aijiamuyingfang.weapp.manager.widgets.WeToolBar;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class StoreActionActivity extends BaseActivity {
    private static final String TAG = StoreActionActivity.class.getName();
    @BindView(R.id.toolbar)
    WeToolBar mToolBar;
    @BindView(R.id.store_name)
    ClearEditText mStoreNameEditText;
    @BindView(R.id.store_startWorkTime)
    ClearEditText mStoreStartWorkTimeEditText;
    @BindView(R.id.store_endWorkTime)
    ClearEditText mStoreEndWorkTimeEditText;
    @BindView(R.id.store_contactNumber)
    ClearEditText mStoreContactPhoneEditText;
    @BindView(R.id.store_address)
    TextView mStoreAddressTextView;
    @BindView(R.id.store_detail_address)
    ClearEditText mStoreDetailAddressEditText;
    @BindView(R.id.lv_suggestions)
    ListView mSuggestionListView;
    @BindView(R.id.store_address_longitude)
    TextView mStoreAddressLongitudeTextView;
    @BindView(R.id.store_address_latitude)
    TextView mStoreAddressLatitudeTextView;
    @BindView(R.id.store_coverImg)
    EditableImageView mStoreCoverImageView;

    @BindView(R.id.store_detailImg_1)
    EditableImageView mStoreDetail1ImageView;
    @BindView(R.id.store_detailImg_2)
    EditableImageView mStoreDetail2ImageView;
    @BindView(R.id.store_detailImg_3)
    EditableImageView mStoreDetail3ImageView;
    @BindView(R.id.store_detailImg_4)
    EditableImageView mStoreDetail4ImageView;
    @BindView(R.id.store_detailImg_5)
    EditableImageView mStoreDetail5ImageView;
    @BindView(R.id.store_detailImg_6)
    EditableImageView mStoreDetail6ImageView;
    @BindView(R.id.store_detailImg_7)
    EditableImageView mStoreDetail7ImageView;
    @BindView(R.id.store_detailImg_8)
    EditableImageView mStoreDetail8ImageView;
    @BindView(R.id.store_detailImg_9)
    EditableImageView mStoreDetail9ImageView;

    private Store mCurStore;
    private StoreControllerApi storeControllerApi = new StoreControllerClient();
    private List<Disposable> storeDisposableList = new ArrayList<>();

    private List<ProvinceViewData> provinceList = new ArrayList<>();
    private List<List<CityViewData>> cityList = new ArrayList<>();
    private List<List<List<CountyViewData>>> countyList = new ArrayList<>();

    private ProvinceViewData mCurProvinceJsonBean;
    private CityViewData mCurCityJsonBean;
    private CountyViewData mCurCountyJsonBean;

    @SuppressWarnings("unchecked")
    public void showAddressPickView() {
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, (options1, options2, options3, v) -> {
            //返回的分别是三个级别的选中位置
            mCurProvinceJsonBean = provinceList.get(options1);
            mCurCityJsonBean = mCurProvinceJsonBean.getCity().get(options2);
            mCurCountyJsonBean = mCurCityJsonBean.getCounty().get(options3);

            String tx = mCurProvinceJsonBean.getName() + "-" + mCurCityJsonBean.getName() + "-" + mCurCountyJsonBean.getName();
            mStoreAddressTextView.setText(tx);
        })
                .setTitleText("城市选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK)
                .setContentTextSize(20)
                .build();
        pvOptions.setPicker(provinceList, cityList, countyList);
        pvOptions.show();
    }


    /**
     * province.json数据是否加载完成
     */
    private boolean isLoaded = false;

    @Override
    protected void init() {

        Observable.create((ObservableOnSubscribe<Boolean>) e -> {
            try {
                String jsonData = JsonUtils.getJson(StoreActionActivity.this, "province.json");
                StoreActionActivity.this.provinceList = JsonUtils.json2List(jsonData, ProvinceViewData.class);
                for (ProvinceViewData provinceJsonBean : StoreActionActivity.this.provinceList) {
                    List<List<CountyViewData>> counties = new ArrayList<>();
                    for (CityViewData cityJsonBean : provinceJsonBean.getCity()) {
                        counties.add(cityJsonBean.getCounty());
                    }
                    StoreActionActivity.this.cityList.add(provinceJsonBean.getCity());
                    StoreActionActivity.this.countyList.add(counties);
                }
            } catch (Exception e1) {
                e.onNext(false);
            }
            e.onNext(true);
            e.onComplete();
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {
                storeDisposableList.add(d);
            }

            @Override
            public void onNext(Boolean value) {
                StoreActionActivity.this.isLoaded = value;
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "read province.json failed", e);
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "read province.json complete");
            }
        });
        initListener();
        initData();
    }


    private void initData() {
        Intent intent = getIntent();
        mCurStore = intent.getParcelableExtra(Constant.INTENT_STORE);
        if (mCurStore != null) {
            mToolBar.setRightButtonText("删除");
            mToolBar.setRightButtonOnClickListener(v ->
                    deleteStore(mCurStore.getId())
            );
            mStoreNameEditText.setText(mCurStore.getName());
            WorkTime workTime = mCurStore.getWorkTime();
            if (workTime != null) {
                mStoreStartWorkTimeEditText.setText(workTime.getStart());
                mStoreEndWorkTimeEditText.setText(workTime.getEnd());
            }
            mStoreContactPhoneEditText.setText(mCurStore.getStoreAddress().getContactor());

            String address = "";
            cn.aijiamuyingfang.commons.domain.address.Province province = mCurStore.getStoreAddress().getProvince();
            if (province != null) {
                mCurProvinceJsonBean = new ProvinceViewData();
                mCurProvinceJsonBean.setName(province.getName());
                mCurProvinceJsonBean.setCode(province.getCode());
                address = province.getName();
            }

            cn.aijiamuyingfang.commons.domain.address.City city = mCurStore.getStoreAddress().getCity();
            if (city != null) {
                mCurCityJsonBean = new CityViewData();
                mCurCityJsonBean.setName(city.getName());
                mCurCityJsonBean.setCode(city.getCode());
                address += "-" + city.getName();
            }

            cn.aijiamuyingfang.commons.domain.address.County county = mCurStore.getStoreAddress().getCounty();
            if (county != null) {
                mCurCountyJsonBean = new CountyViewData();
                mCurCountyJsonBean.setName(county.getName());
                mCurCountyJsonBean.setCode(county.getCode());
                address += "-" + county.getName();
            }
            mStoreAddressTextView.setText(address);
            mStoreDetailAddressEditText.setText(mCurStore.getStoreAddress().getDetail());
            mStoreAddressLongitudeTextView.setText(mCurStore.getStoreAddress().getCoordinate().getLongitude() + "");
            mStoreAddressLatitudeTextView.setText(mCurStore.getStoreAddress().getCoordinate().getLatitude() + "");
            GlideUtils.load(StoreActionActivity.this, mCurStore.getCoverImg(), mStoreCoverImageView);
            EditableImageView[] imageViews = new EditableImageView[]{mStoreDetail1ImageView, mStoreDetail2ImageView,
                    mStoreDetail3ImageView, mStoreDetail4ImageView, mStoreDetail5ImageView,
                    mStoreDetail6ImageView, mStoreDetail7ImageView, mStoreDetail8ImageView,
                    mStoreDetail9ImageView,};
            for (int i = 0; i < mCurStore.getDetailImgList().size(); i++) {
                GlideUtils.load(StoreActionActivity.this, mCurStore.getDetailImgList().get(i), imageViews[i]);
            }
        }
    }

    private static final int MSG_SUGGESTION = 100000;
    private final MyHandler handler = new MyHandler(this);
    private SuggestionAdapter suggestionAdapter;

    private class SuggestionAdapter extends BaseAdapter {

        List<SuggestionResultObject.SuggestionData> mSuggestionDataList;

        private SuggestionAdapter(List<SuggestionResultObject.SuggestionData> suggestionDatas) {
            setDataList(suggestionDatas);
        }

        private void setDataList(List<SuggestionResultObject.SuggestionData> suggestDataList) {
            mSuggestionDataList = suggestDataList;
        }

        @Override
        public int getCount() {
            return mSuggestionDataList.size();
        }

        @Override
        public SuggestionResultObject.SuggestionData getItem(int position) {
            return mSuggestionDataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(StoreActionActivity.this,
                        R.layout.suggestion_list_item, null);
                viewHolder = new ViewHolder();
                viewHolder.tvTitle = convertView.findViewById(R.id.label);
                viewHolder.tvAddress = convertView.findViewById(R.id.desc);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.tvTitle.setText(mSuggestionDataList.get(position).title);
            viewHolder.tvAddress.setText(mSuggestionDataList.get(position).address);
            return convertView;
        }

        private class ViewHolder {
            TextView tvTitle;
            TextView tvAddress;
        }
    }


    private static class MyHandler extends Handler {
        private WeakReference<StoreActionActivity> mActivity;

        private MyHandler(StoreActionActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            StoreActionActivity activity = mActivity.get();
            if (activity != null) {
                activity.handleMessage(msg);
            }
        }

    }

    public void handleMessage(Message msg) {
        if (msg.what == MSG_SUGGESTION) {
            showAutoComplet((SuggestionResultObject) msg.obj);
        }
    }

    protected void showAutoComplet(SuggestionResultObject obj) {
        if (obj.data.isEmpty()) {
            mSuggestionListView.setVisibility(View.GONE);
            return;
        }
        if (suggestionAdapter == null) {
            suggestionAdapter = new SuggestionAdapter(obj.data);
            mSuggestionListView.setAdapter(suggestionAdapter);
        } else {
            suggestionAdapter.setDataList(obj.data);
            suggestionAdapter.notifyDataSetChanged();
        }
        setListViewHeight(mSuggestionListView);
        mSuggestionListView.setVisibility(View.VISIBLE);
    }

    protected void setListViewHeight(ListView lv) {
        Adapter adapter = lv.getAdapter();
        if (adapter == null) {
            return;
        }
        int maxHeight = 400;
        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, lv);
            listItem.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }
        int sumHeight = totalHeight + (lv.getDividerHeight() * (adapter.getCount() - 1));
        sumHeight = (sumHeight < maxHeight) ? sumHeight : maxHeight;
        ViewGroup.LayoutParams viewGLayoutParams = lv.getLayoutParams();
        viewGLayoutParams.height = sumHeight;
    }


    private void suggestion(String keyword) {
        if (keyword.trim().length() == 0) {
            mSuggestionListView.setVisibility(View.GONE);
            return;
        }
        TencentSearch tencentSearch = new TencentSearch(this);
        SuggestionParam suggestionParam = new SuggestionParam();
        suggestionParam.keyword(keyword);
        if (mCurCountyJsonBean != null) {
            //suggestion也提供了filter()方法和region方法
            //具体说明见文档，或者官网的webservice对应接口
            suggestionParam.region(mCurCityJsonBean.getName());
        }

        tencentSearch.suggestion(suggestionParam, new HttpResponseListener() {

            @Override
            public void onSuccess(int arg0, BaseObject arg1) {
                if (arg1 == null ||
                        mStoreDetailAddressEditText.getText().toString().trim().length() == 0) {
                    mSuggestionListView.setVisibility(View.GONE);
                    return;
                }

                Message msg = new Message();
                msg.what = MSG_SUGGESTION;
                msg.obj = arg1;
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(int arg0, String arg1, Throwable arg2) {
                Log.e(TAG, arg1);
            }
        });
    }

    private void initListener() {
        final TextWatcher textWatcher = new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                suggestion(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                //NOT NEED IMPLEMENTATION
            }

            @Override
            public void afterTextChanged(Editable s) {
                //NOT NEED IMPLEMENTATION
            }
        };
        mStoreDetailAddressEditText.addTextChangedListener(textWatcher);
        mStoreDetailAddressEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!mStoreDetailAddressEditText.hasFocus()) {
                mSuggestionListView.setVisibility(View.GONE);
            }
        });
        mSuggestionListView.setOnItemClickListener((parent, view, position, id) -> {
            mStoreDetailAddressEditText.removeTextChangedListener(textWatcher);
            SuggestionResultObject.SuggestionData data = suggestionAdapter.getItem(position);
            mStoreAddressLongitudeTextView.setText(data.location.lng + "");
            mStoreAddressLatitudeTextView.setText(data.location.lat + "");
            mStoreDetailAddressEditText.setText(data.address);
            mStoreDetailAddressEditText.setSelection(mStoreDetailAddressEditText.getText().length());

            mSuggestionListView.setVisibility(View.GONE);
            mStoreDetailAddressEditText.addTextChangedListener(textWatcher);
        });
    }

    private void deleteStore(String storeid) {
        storeControllerApi.deprecateStore(CommonApp.getApplication().getUserToken(), storeid).subscribe(new Observer<ResponseBean<Void>>() {
            @Override
            public void onSubscribe(Disposable d) {
                storeDisposableList.add(d);
            }

            @Override
            public void onNext(ResponseBean<Void> responseBean) {
                if (ResponseCode.OK.getCode().equals(responseBean.getCode())) {
                    StoreActionActivity.this.finish();
                } else {
                    Log.e(TAG, responseBean.getMsg());
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "deprecated store failed", e);
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "deprecated store complete");
            }
        });
    }

    private void createStore() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);

                File coverImg = mStoreCoverImageView.getImageFileSync();
                RequestBody requestCoverImg = RequestBody.create(MultipartBody.FORM, coverImg);
                requestBodyBuilder.addFormDataPart("coverImage", coverImg.getName(), requestCoverImg);

                List<File> detailImageList = new ArrayList<>();
                detailImageList.add(mStoreDetail1ImageView.getImageFileSync());
                detailImageList.add(mStoreDetail2ImageView.getImageFileSync());
                detailImageList.add(mStoreDetail3ImageView.getImageFileSync());
                detailImageList.add(mStoreDetail4ImageView.getImageFileSync());
                detailImageList.add(mStoreDetail5ImageView.getImageFileSync());
                detailImageList.add(mStoreDetail6ImageView.getImageFileSync());
                detailImageList.add(mStoreDetail7ImageView.getImageFileSync());
                detailImageList.add(mStoreDetail8ImageView.getImageFileSync());
                detailImageList.add(mStoreDetail9ImageView.getImageFileSync());
                for (File detailImageFile : detailImageList) {
                    RequestBody requestDetail = RequestBody.create(MultipartBody.FORM, detailImageFile);
                    requestBodyBuilder.addFormDataPart("detailImages", detailImageFile.getName(), requestDetail);
                }

                requestBodyBuilder.addFormDataPart("name", mStoreNameEditText.getText().toString());
                requestBodyBuilder.addFormDataPart("workTime.start", mStoreStartWorkTimeEditText.getText().toString());
                requestBodyBuilder.addFormDataPart("workTime.end", mStoreEndWorkTimeEditText.getText().toString());

                requestBodyBuilder.addFormDataPart("storeAddress.detail", mStoreDetailAddressEditText.getText().toString());
                requestBodyBuilder.addFormDataPart("storeAddress.phone", mStoreContactPhoneEditText.getText().toString());

                requestBodyBuilder.addFormDataPart("storeAddress.coordinate.longitude", mStoreAddressLongitudeTextView.getText().toString());
                requestBodyBuilder.addFormDataPart("storeAddress.coordinate.latitude", mStoreAddressLatitudeTextView.getText().toString());

                if (mCurProvinceJsonBean != null) {
                    requestBodyBuilder.addFormDataPart("storeAddress.province.name", mCurProvinceJsonBean.getName());
                    requestBodyBuilder.addFormDataPart("storeAddress.province.code", mCurProvinceJsonBean.getCode());
                }

                if (mCurCityJsonBean != null) {
                    requestBodyBuilder.addFormDataPart("storeAddress.city.name", mCurCityJsonBean.getName());
                    requestBodyBuilder.addFormDataPart("storeAddress.city.code", mCurCityJsonBean.getCode());
                }

                if (mCurCountyJsonBean != null) {
                    requestBodyBuilder.addFormDataPart("storeAddress.county.name", mCurCountyJsonBean.getName());
                    requestBodyBuilder.addFormDataPart("storeAddress.county.code", mCurCountyJsonBean.getCode());
                }

                if (mCurStore != null) {
                    requestBodyBuilder.addFormDataPart("id", mCurStore.getId() + "");
                    requestBodyBuilder.addFormDataPart("storeAddress.id", mCurStore.getStoreAddress().getId() + "");
                }
                storeControllerApi.createStore(CommonApp.getApplication().getUserToken(), requestBodyBuilder.build()).subscribe(new Observer<ResponseBean<Store>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        storeDisposableList.add(d);
                    }

                    @Override
                    public void onNext(ResponseBean<Store> responseBean) {
                        if (ResponseCode.OK.getCode().equals(responseBean.getCode())) {
                            StoreActionActivity.this.finish();
                        } else {
                            Log.e(TAG, responseBean.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "create store failed", e);
                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG, "create store complete");
                    }
                });
            }
        };
        thread.start();
    }

    @Override
    protected int getContentResourceId() {
        return R.layout.activity_add_store;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //在拍照、选取照片、裁剪Activity结束后，调用的方法
        List<EditableImageView> imageViewList = new ArrayList<>();
        imageViewList.add(mStoreCoverImageView);
        imageViewList.add(mStoreDetail1ImageView);
        imageViewList.add(mStoreDetail2ImageView);
        imageViewList.add(mStoreDetail3ImageView);
        imageViewList.add(mStoreDetail4ImageView);
        imageViewList.add(mStoreDetail5ImageView);
        imageViewList.add(mStoreDetail6ImageView);
        imageViewList.add(mStoreDetail7ImageView);
        imageViewList.add(mStoreDetail8ImageView);
        imageViewList.add(mStoreDetail9ImageView);
        for (EditableImageView imageView : imageViewList) {
            if (imageView != null && imageView.isInEdit()) {
                imageView.onActivityResult(requestCode, resultCode, data);
            }
        }
    }


    @OnClick({R.id.store_address, R.id.save_store})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.store_address:
                if (isLoaded) {
                    showAddressPickView();
                } else {
                    ToastUtils.showSafeToast(StoreActionActivity.this, "请稍等,数据获取中");
                }
                break;
            case R.id.save_store:
                createStore();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxJavaUtils.dispose(storeDisposableList);
    }

}