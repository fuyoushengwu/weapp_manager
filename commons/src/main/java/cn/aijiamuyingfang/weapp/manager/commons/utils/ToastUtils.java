package cn.aijiamuyingfang.weapp.manager.commons.utils;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import cn.aijiamuyingfang.weapp.manager.commons.CommonApp;


/**
 * Created by pc on 2018/3/30.
 */

public final class ToastUtils {
    private ToastUtils() {
    }

    private static Toast mToast;


    /**
     * 显示吐司(在非UI线程也可以调用)
     *
     * @param context
     * @param text
     */
    public static void showSafeToast(final Context context, final String text) {
        if (Looper.myLooper() != Looper.getMainLooper()) {//如果不是在主线程弹出吐司，那么抛到主线程弹
            new Handler(Looper.getMainLooper()).post(
                    new Runnable() {
                        @Override
                        public void run() {
                            showUIToast(context, text);
                        }
                    }
            );
        } else {
            showUIToast(context, text);
        }
    }

    /**
     * 显示吐司提示框(在UI线程中调用)
     *
     * @param context
     * @param text
     */
    private static void showUIToast(Context context, String text) {
        if (context == null) {
            context = CommonApp.getApplication();
        }
        if (null == mToast) {
            mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        }
        if (text.length() < 10) {
            mToast.setDuration(Toast.LENGTH_SHORT);
        } else {
            mToast.setDuration(Toast.LENGTH_LONG);
        }

        View view = mToast.getView();
        view.setPadding(28, 12, 28, 12);
        TextView tv = view.findViewById(android.R.id.message);
        tv.setTextSize(16);
        tv.setTextColor(Color.WHITE);
        mToast.setText(text);
        mToast.setGravity(Gravity.CENTER, 0, 40);
        mToast.setView(view);
        mToast.show();
    }
}
