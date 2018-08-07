package cn.aijiamuyingfang.weapp.manager;

import android.util.Log;

import java.io.File;
import java.io.IOException;

import cn.aijiamuyingfang.weapp.manager.commons.CommonApp;
import cn.aijiamuyingfang.weapp.manager.commons.utils.FileUtils;

/**
 * Created by pc on 2018/3/30.
 */
@SuppressWarnings("squid:MaximumInheritanceDepth")
public class WeApplication extends CommonApp {

    private static final String TAG = WeApplication.class.getName();
    @Override
    public void onCreate() {
        super.onCreate();
        File imageDir = new File(getDefaultImageDir());

        if (imageDir.exists()) {
            try {
                FileUtils.cleanDirectory(imageDir);
            } catch (IOException e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
    }


}
