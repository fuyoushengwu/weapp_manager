package cn.aijiamuyingfang.weapp.manager.commons.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import cn.aijiamuyingfang.commons.utils.StringUtils;

/**
 * Created by pc on 2018/4/3.
 * 权限工具类
 */

public class PermissionUtils {

    /**
     * 权限申请成功/已赋予权限的回调借口
     */
    public interface PermissionGrantedCallBack {
        /**
         * 权限申请成功/已赋予权限的回调方法
         */
        void onPermissionGranted();
    }

    /**
     * 检测权限
     *
     * @return true：已授权； false：未授权；
     */
    public static boolean checkPermission(Context context, String permission) {
        return context != null && StringUtils.hasContent(permission) && ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 请求权限
     */
    public static void requestPermission(Context context, String permission, int requestCode) {
        if (null == context || StringUtils.isEmpty(permission)) {
            return;
        }
        ActivityCompat.requestPermissions((Activity) context, new String[]{permission}, requestCode);
    }

    /**
     * 判断权限是否申请成功
     */
    public static boolean isPermissionRequestSuccess(int[] grantResults) {
        if (null == grantResults) {
            return false;
        }
        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 检测多个权限
     *
     * @return 未授权的权限
     */
    public static List<String> checkMorePermissions(Context context, String[] permissions) {
        List<String> permissionList = new ArrayList<>();
        for (String permission : permissions) {
            if (!checkPermission(context, permission)) {
                permissionList.add(permission);
            }
        }
        return permissionList;
    }
}
