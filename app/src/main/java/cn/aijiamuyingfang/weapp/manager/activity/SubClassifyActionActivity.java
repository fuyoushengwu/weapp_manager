package cn.aijiamuyingfang.weapp.manager.activity;

import android.content.Intent;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.aijiamuyingfang.client.domain.ResponseBean;
import cn.aijiamuyingfang.client.domain.ResponseCode;
import cn.aijiamuyingfang.client.domain.classify.Classify;
import cn.aijiamuyingfang.client.rest.api.ClassifyControllerApi;
import cn.aijiamuyingfang.client.rest.utils.StringUtils;
import cn.aijiamuyingfang.weapp.manager.R;
import cn.aijiamuyingfang.weapp.manager.access.server.impl.ClassifyControllerClient;
import cn.aijiamuyingfang.weapp.manager.access.server.utils.RxJavaUtils;
import cn.aijiamuyingfang.weapp.manager.commons.CommonApp;
import cn.aijiamuyingfang.weapp.manager.commons.Constant;
import cn.aijiamuyingfang.weapp.manager.commons.activity.BaseActivity;
import cn.aijiamuyingfang.weapp.manager.commons.utils.ToastUtils;
import cn.aijiamuyingfang.weapp.manager.widgets.ClearEditText;
import cn.aijiamuyingfang.weapp.manager.widgets.EditableImageView;
import cn.aijiamuyingfang.weapp.manager.widgets.WeToolBar;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class SubClassifyActionActivity extends BaseActivity {
    private static final String TAG = SubClassifyActionActivity.class.getName();
    private static final ClassifyControllerApi classifyControllerApi = new ClassifyControllerClient();
    private final List<Disposable> classifyDisposableList = new ArrayList<>();
    @BindView(R.id.classify_logo)
    EditableImageView mEditableImageView;
    @BindView(R.id.edittxt_consignee)
    ClearEditText mEditText;
    @BindView(R.id.toolbar)
    WeToolBar mToolBar;

    /**
     * 当前顶层条目的ID
     */
    private String mCurTopClassifyId;

    @Override
    protected void init() {
        mToolBar.setRightButtonOnClickListener(v -> {
            Thread thread = new Thread() {
                @Override
                public void run() {
                    String classifyName = mEditText.getText().toString();
                    File cropLogo = mEditableImageView.getImageFileSync();
                    if (StringUtils.isEmpty(classifyName) || null == cropLogo) {
                        return;
                    }

                    MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
                    requestBodyBuilder.addFormDataPart("name", classifyName);

                    RequestBody requestLogo = RequestBody.create(MultipartBody.FORM, cropLogo);
                    requestBodyBuilder.addFormDataPart("coverImage", cropLogo.getName(), requestLogo);

                    classifyControllerApi.createSubClassify(mCurTopClassifyId, requestBodyBuilder.build(), CommonApp.getApplication().getUserToken()).subscribe(new Observer<ResponseBean<Classify>>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            classifyDisposableList.add(d);
                        }

                        @Override
                        public void onNext(ResponseBean<Classify> responseBean) {
                            if (ResponseCode.OK.getCode().equals(responseBean.getCode())) {
                                SubClassifyActionActivity.this.finish();
                            } else {
                                Log.e(TAG, responseBean.getMsg());
                                ToastUtils.showSafeToast(SubClassifyActionActivity.this, "因服务端的原因,保存子条目失败");
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e(TAG, "create sub classify failed", e);
                            ToastUtils.showSafeToast(SubClassifyActionActivity.this, "因客户端的原因,保存子条目失败");
                        }

                        @Override
                        public void onComplete() {
                            Log.i(TAG, "create sub classify complete");
                        }
                    });
                }
            };
            thread.start();

        });

        Intent intent = getIntent();
        mCurTopClassifyId = intent.getStringExtra(Constant.INTENT_TOP_CLASSIFY_ID);
    }


    @Override
    protected int getContentResourceId() {
        return R.layout.activity_add_sub_classify;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //在拍照、选取照片、裁剪Activity结束后，调用的方法
        if (mEditableImageView != null) {
            mEditableImageView.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxJavaUtils.dispose(classifyDisposableList);
    }
}