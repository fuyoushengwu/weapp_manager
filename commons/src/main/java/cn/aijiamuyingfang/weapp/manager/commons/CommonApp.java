package cn.aijiamuyingfang.weapp.manager.commons;

import android.app.Application;
import android.os.Environment;

//全局Context
public class CommonApp extends Application {
    private static CommonApp application;

    public static CommonApp getApplication() {
        return application;
    }

    private String defaultImageDir;

    private String defaultHttpCacheDir;

    private String userToken;

    private String username;


    @Override
    public void onCreate() {
        super.onCreate();
        CommonApp.application = this;
        //应用使用的图片目录
        defaultImageDir = Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/" + application.getPackageName() + "/images/";
        //OKHttp缓存目录
        defaultHttpCacheDir = Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/" + application.getPackageName() + "/httpcache/";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getDefaultImageDir() {
        return defaultImageDir;
    }

    public String getDefaultHttpCacheDir() {
        return defaultHttpCacheDir;
    }
}
