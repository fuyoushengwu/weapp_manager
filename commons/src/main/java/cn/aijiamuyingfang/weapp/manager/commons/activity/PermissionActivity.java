package cn.aijiamuyingfang.weapp.manager.commons.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.Window;

import cn.aijiamuyingfang.commons.utils.StringUtils;
import cn.aijiamuyingfang.weapp.manager.commons.CommonApp;
import cn.aijiamuyingfang.weapp.manager.commons.utils.PermissionUtils;

/**
 * Created by pc on 2018/4/3.
 */
@SuppressWarnings("squid:MaximumInheritanceDepth")
public class PermissionActivity extends AppCompatActivity {

    /**
     * 非法的请求码
     */
    public static final int INVALID_PERMISSION_REQUEST_CODE = -1;
    /**
     * intent中保存的申请的permission信息,对应的key
     */
    public static final String INTENT_PERMISSION = "intent_permission";
    /**
     * intent中保存的申请的request_code信息,对应的key
     */
    public static final String INTENT_REQUEST_CODE = "intent_request_code";

    private static int permissionRequestCode = INVALID_PERMISSION_REQUEST_CODE;
    /**
     * 权限申请成功后的回调方法集合,Key为Permission的Request_code
     */
    private static final SparseArray<PermissionUtils.PermissionGrantedCallBack> permissionCallBacks = new SparseArray<>();

    public static synchronized void checkAndRequestPermission(Context context, String permission, PermissionUtils.PermissionGrantedCallBack grantedCallBack) {
        if (StringUtils.isEmpty(permission)) {
            invokeCallBack(grantedCallBack);
        }
        if (null == context) {
            context = CommonApp.getApplication();
        }
        if (PermissionUtils.checkPermission(context, permission)) {
            invokeCallBack(grantedCallBack);
        } else {
            requestPermission(context, permission, grantedCallBack);
        }
    }

    private static void requestPermission(Context context, String permission, PermissionUtils.PermissionGrantedCallBack grantedCallBack) {
        permissionRequestCode++;
        permissionCallBacks.put(permissionRequestCode, grantedCallBack);
        Intent intent = new Intent(context, PermissionActivity.class);
        intent.putExtra(INTENT_PERMISSION, permission);
        intent.putExtra(INTENT_REQUEST_CODE, permissionRequestCode);
        context.startActivity(intent);
    }

    /**
     * @param grantedCallBack 回调方法
     */
    private static void invokeCallBack(PermissionUtils.PermissionGrantedCallBack grantedCallBack) {
        if (grantedCallBack != null) {
            grantedCallBack.onPermissionGranted();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        if (intent != null) {
            String permission = intent.getStringExtra(INTENT_PERMISSION);
            int requestCode = intent.getIntExtra(INTENT_REQUEST_CODE, INVALID_PERMISSION_REQUEST_CODE);
            PermissionUtils.requestPermission(this, permission, requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (PermissionUtils.isPermissionRequestSuccess(grantResults)) {
            PermissionUtils.PermissionGrantedCallBack callBack = permissionCallBacks.get(permissionRequestCode);
            permissionCallBacks.remove(permissionRequestCode);
            invokeCallBack(callBack);
        }
        finish();
    }
}
