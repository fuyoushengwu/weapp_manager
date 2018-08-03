package cn.aijiamuyingfang.weapp.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.aijiamuyingfang.weapp.manager.commons.Constant;
import cn.aijiamuyingfang.weapp.manager.commons.activity.BaseActivity;
import cn.aijiamuyingfang.weapp.manager.commons.utils.ToastUtils;
import cn.aijiamuyingfang.weapp.manager.fragment.ClassifyFragment;
import cn.aijiamuyingfang.weapp.manager.fragment.GoodVoucherFragment;
import cn.aijiamuyingfang.weapp.manager.fragment.MessageFragment;
import cn.aijiamuyingfang.weapp.manager.fragment.StoreFragment;
import cn.aijiamuyingfang.weapp.manager.fragment.VoucherItemFragment;
import cn.aijiamuyingfang.weapp.manager.widgets.FragmentTabHost;
import cn.aijiamuyingfang.weapp.manager.widgets.Tab;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class MainActivity extends BaseActivity {
    private static final String TAG = MainActivity.class.getName();
    @BindView(android.R.id.tabhost)
    FragmentTabHost mTabHost;

    private TabSelectBroadCastReciever broadCastReciever = new TabSelectBroadCastReciever();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.BROADCAST_ACTION_TAB_SELECTED);
        registerReceiver(broadCastReciever, intentFilter);
    }

    @Override
    protected int getContentResourceId() {
        return R.layout.activity_main;
    }


    @Override
    protected void init() {
        initTab();
    }

    /**
     * 初始化选项卡1
     */
    private void initTab() {
        List<Tab> mTabs = new ArrayList<>();
        mTabs.add(new Tab(R.string.goods, R.drawable.selector_icon_classify, ClassifyFragment.class));
        mTabs.add(new Tab(R.string.goodvoucher, R.drawable.selector_icon_classify, GoodVoucherFragment.class));
        mTabs.add(new Tab(R.string.voucheritem, R.drawable.selector_icon_classify, VoucherItemFragment.class));
        mTabs.add(new Tab(R.string.store, R.drawable.selector_icon_classify, StoreFragment.class));
        mTabs.add(new Tab(R.string.message, R.drawable.selector_icon_classify, MessageFragment.class));

        mTabHost.setup(this, this.getSupportFragmentManager(), R.id.realtabcontent);
        for (Tab tab : mTabs) {
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(getString(tab.getTitle()));
            tabSpec.setIndicator(buildIndicator(tab));
            mTabHost.addTab(tabSpec, tab.getFragment(), null);
        }

        mTabHost.setOnTabChangedListener(tabId -> Log.i(TAG, "onTabChanged:" + tabId));
        mTabHost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
        mTabHost.setCurrentTab(0);
    }

    private View buildIndicator(Tab tab) {
        View view = LayoutInflater.from(this).inflate(R.layout.tab_indicator, null);
        ImageView img = view.findViewById(R.id.icon_tab);
        TextView tv = view.findViewById(R.id.txt_indicator);
        img.setImageResource(tab.getIcon());
        tv.setText(tab.getTitle());
        return view;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadCastReciever);
    }

    //控制物理返回键
    // 用来计算返回键的点击间隔时间
    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (System.currentTimeMillis() - exitTime > 2000) {
                ToastUtils.showSafeToast(MainActivity.this, "再点一次退出轻松购");
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void selectTab(int position) {
        mTabHost.setCurrentTab(position);
    }

    /**
     * MainActivity Tab Index变更的监听器
     */
    private class TabSelectBroadCastReciever extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (Constant.BROADCAST_ACTION_TAB_SELECTED.equals(intent.getAction())) {
                int tabIndex = intent.getIntExtra(Constant.INTENT_TAB_INDEX, 0);
                MainActivity.this.selectTab(tabIndex);
            }
        }
    }
}
