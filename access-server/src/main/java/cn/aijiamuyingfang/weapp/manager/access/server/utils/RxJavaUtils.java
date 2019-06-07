package cn.aijiamuyingfang.weapp.manager.access.server.utils;

import android.util.Log;

import java.util.List;

import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by pc on 2018/3/31.
 * 控制操作线程的辅助类
 */
public final class RxJavaUtils {
    private static final String TAG = RxJavaUtils.class.getName();

    private RxJavaUtils() {
    }

    public static <T> ObservableTransformer<T, T> switchSchedulers() {
        return upstream -> upstream
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 释放RxJava的监听者
     *
     * @param disposableList
     */
    public static void dispose(List<Disposable> disposableList) {
        if (null == disposableList || disposableList.isEmpty()) {
            Log.i(TAG, "disposable list is empty");
            return;
        }
        for (Disposable disposable : disposableList) {
            if (disposable != null && !disposable.isDisposed()) {
                disposable.dispose();
            }
        }
    }


}
