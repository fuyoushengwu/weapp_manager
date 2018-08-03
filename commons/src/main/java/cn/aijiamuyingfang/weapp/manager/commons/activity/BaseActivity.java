package cn.aijiamuyingfang.weapp.manager.commons.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import butterknife.ButterKnife;
import cn.aijiamuyingfang.weapp.manager.commons.R;

/**
 * Created by pc on 2018/3/30.
 */
@SuppressWarnings("squid:MaximumInheritanceDepth")
public abstract class BaseActivity extends AppCompatActivity {
    protected static final String TAG = BaseActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentResourceId());
        setStatusBar();
        ButterKnife.bind(this);
        init();
    }


    /**
     * android 5.0 及以下沉浸式状态栏
     */
    protected void setStatusBar() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//设置透明状态栏
        initSystemBar(this);
    }


    /**
     * 沉浸式状态栏.
     */
    public void initSystemBar(Activity activity) {
        setTranslucentStatus(activity);

        SystemBarTintManager tintManager = new SystemBarTintManager(activity);
        tintManager.setStatusBarTintEnabled(true);
        // 使用颜色资源
        tintManager.setStatusBarTintResource(R.color.colorPrimary);
    }

    private void setTranslucentStatus(Activity activity) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;

        winParams.flags |= bits;
        win.setAttributes(winParams);
    }


    protected abstract void init();

    protected abstract int getContentResourceId();
}
