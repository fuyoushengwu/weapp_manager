package cn.aijiamuyingfang.weapp.manager.activity;

import android.content.Intent;

import java.io.File;

import butterknife.BindView;
import cn.aijiamuyingfang.client.rest.api.ClassifyControllerApi;
import cn.aijiamuyingfang.commons.domain.response.ResponseCode;
import cn.aijiamuyingfang.commons.utils.StringUtils;
import cn.aijiamuyingfang.weapp.manager.R;
import cn.aijiamuyingfang.weapp.manager.access.server.impl.ClassifyControllerClient;
import cn.aijiamuyingfang.weapp.manager.commons.CommonApp;
import cn.aijiamuyingfang.weapp.manager.commons.Constant;
import cn.aijiamuyingfang.weapp.manager.commons.activity.BaseActivity;
import cn.aijiamuyingfang.weapp.manager.widgets.ClearEditText;
import cn.aijiamuyingfang.weapp.manager.widgets.EditableImageView;
import cn.aijiamuyingfang.weapp.manager.widgets.WeToolBar;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class SubClassifyActionActivity extends BaseActivity {

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


    private ClassifyControllerApi classifyControllerApi = new ClassifyControllerClient();


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

                    classifyControllerApi.createSubClassify(CommonApp.getApplication().getUserToken(), mCurTopClassifyId, requestBodyBuilder.build()).subscribe(responseBean -> {
                        if (ResponseCode.OK.getCode().equals(responseBean.getCode())) {
                            SubClassifyActionActivity.this.finish();
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
}