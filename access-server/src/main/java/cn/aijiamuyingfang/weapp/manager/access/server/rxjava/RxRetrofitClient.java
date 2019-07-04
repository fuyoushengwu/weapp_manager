package cn.aijiamuyingfang.weapp.manager.access.server.rxjava;

import android.Manifest;

import java.io.File;

import cn.aijiamuyingfang.client.commons.constant.ClientRestConstants;
import cn.aijiamuyingfang.client.commons.utils.RetrofitUtils;
import cn.aijiamuyingfang.weapp.manager.access.server.interceptor.RxCacheInterceptor;
import cn.aijiamuyingfang.weapp.manager.commons.CommonApp;
import cn.aijiamuyingfang.weapp.manager.commons.activity.PermissionActivity;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;


public class RxRetrofitClient {

    private OkHttpClient httpclient;
    private Retrofit retrofit;

    public RxRetrofitClient() {
        final OkHttpClient.Builder httpclientBuilder = RetrofitUtils.getOkHttpClientBuilder(ClientRestConstants.DEFAULT_CONNECT_TIMEOUT, ClientRestConstants.DEFAULT_READ_TIMEOUT, ClientRestConstants.DEFAULT_WRITE_TIMEOUT);
        PermissionActivity.checkAndRequestPermission(null, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                () -> {
                    RxCacheInterceptor cacheInterceptor = new RxCacheInterceptor();
                    Cache cache = new Cache(new File(CommonApp.getApplication().getDefaultHttpCacheDir()),
                            1024 * 1024 * 100);
                    httpclientBuilder.addInterceptor(cacheInterceptor).addNetworkInterceptor(cacheInterceptor)
                            .cache(cache);
                });
        httpclient = httpclientBuilder.build();

        Retrofit.Builder retrofitBuilder = RetrofitUtils.getRetrofitBuilder(ClientRestConstants.DEFAULT_BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        retrofit = retrofitBuilder.client(httpclient).build();
    }

    private static RxRetrofitClient instance;

    public static RxRetrofitClient getInstance() {
        if (instance == null) {
            instance = new RxRetrofitClient();
        }
        return instance;
    }

    public static OkHttpClient getHttpClient() {
        return getInstance().httpclient;
    }

    /**
     * @param cls 服务类API
     * @param <K> 泛型
     * @return 创建服务实例
     */
    public static <K> K createGApi(final Class<K> cls) {
        return getInstance().retrofit.create(cls);
    }
}