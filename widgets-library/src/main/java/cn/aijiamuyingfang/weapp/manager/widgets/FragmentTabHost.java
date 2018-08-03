package cn.aijiamuyingfang.weapp.manager.widgets;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabWidget;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pc on 2018/3/30.
 */

public class FragmentTabHost extends TabHost implements
        TabHost.OnTabChangeListener {
    /**
     * 所有Tab信息
     */
    private List<TabInfo> mTabInfos = new ArrayList<>();
    /**
     * 最后一次操作的Tab
     */
    private TabInfo mLastTab;

    /**
     * Fragment管理器
     */
    private FragmentManager mFragmentManager;

    /**
     * 存放Fragment的容器ID
     */
    private int mFragmentContentId;

    private Context mContext;
    private OnTabChangeListener mOnTabChangeListener;
    private boolean mAttached;

    public FragmentTabHost(Context context) {
        this(context, null);
    }

    public FragmentTabHost(Context context, AttributeSet attrs) {
        super(context, attrs);
        super.setOnTabChangedListener(this);
    }

    public void setup(Context context, FragmentManager manager, int containerId) {
        ensureTabStruct(context);
        super.setup();
        mContext = context;
        mFragmentManager = manager;
        mFragmentContentId = containerId;
        if (getId() == View.NO_ID) {
            setId(android.R.id.tabhost);
        }
    }

    /**
     * 构造TabHost结构
     *
     * @param context
     */
    private void ensureTabStruct(Context context) {
        if (null == findViewById(android.R.id.tabs)) {
            LinearLayout ll = new LinearLayout(context);
            ll.setOrientation(LinearLayout.VERTICAL);
            addView(ll, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            TabWidget tw = new TabWidget(context);
            tw.setId(android.R.id.tabs);
            tw.setOrientation(TabWidget.HORIZONTAL);
            ll.addView(tw, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0));

            FrameLayout fl = new FrameLayout(context);
            fl.setId(android.R.id.tabcontent);
            ll.addView(fl, new LinearLayout.LayoutParams(0, 0, 0));
        }
    }

    /**
     * 添加Tab
     *
     * @param tabSpec
     * @param clazz
     * @param bundle
     */
    public void addTab(TabSpec tabSpec, Class<?> clazz, Bundle bundle) {
        tabSpec.setContent(new DummyTabFactory(mContext));
        String tag = tabSpec.getTag();
        TabInfo info = new TabInfo(tag, clazz, bundle);
        if (mAttached) {
            info.fragment = mFragmentManager.findFragmentByTag(tag);
            if (info.fragment != null && !info.fragment.isDetached()) {
                FragmentTransaction ft = mFragmentManager.beginTransaction();
                ft.hide(info.fragment);
                ft.commit();
            }
        }

        mTabInfos.add(info);
        addTab(tabSpec);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        String currentTab = getCurrentTabTag();
        FragmentTransaction ft = null;
        for (TabInfo tabInfo : mTabInfos) {
            tabInfo.fragment = mFragmentManager.findFragmentByTag(tabInfo.tag);
            if (tabInfo.fragment != null) {
                if (tabInfo.tag.equals(currentTab)) {
                    mLastTab = tabInfo;
                } else {
                    if (null == ft) {
                        ft = mFragmentManager.beginTransaction();
                    }
                    ft.hide(tabInfo.fragment);
                }
            }
        }

        mAttached = true;
        ft = doTabChanged(currentTab, ft);
        if (ft != null) {
            ft.commitAllowingStateLoss();
            mFragmentManager.executePendingTransactions();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mAttached = false;
    }

    @Override
    public void onTabChanged(String tabId) {
        if (mAttached) {
            FragmentTransaction ft = doTabChanged(tabId, null);
            if (ft != null) {
                ft.commit();
            }
        }
        if (mOnTabChangeListener != null) {
            mOnTabChangeListener.onTabChanged(tabId);
        }
    }

    /**
     * 点击Tab选项卡的响应
     *
     * @param tabId
     * @param ft
     * @return
     */
    private FragmentTransaction doTabChanged(String tabId, FragmentTransaction ft) {
        TabInfo targetTab = null;
        for (TabInfo tabInfo : mTabInfos) {
            if (tabInfo.tag.equals(tabId)) {
                targetTab = tabInfo;
            }
        }
        if (null == targetTab) {
            throw new IllegalArgumentException(String.format("Tab[%s] not exist", tabId));
        }
        if (targetTab != mLastTab) {
            if (null == ft) {
                ft = mFragmentManager.beginTransaction();
            }
            if (mLastTab != null && mLastTab.fragment != null) {
                ft.detach(mLastTab.fragment);
            }
            if (null == targetTab.fragment) {
                targetTab.fragment = Fragment.instantiate(mContext, targetTab.clazz.getName(), targetTab.bundle);
                ft.add(mFragmentContentId, targetTab.fragment, targetTab.tag);
            } else {
                ft.attach(targetTab.fragment);
            }
            mLastTab = targetTab;
        }
        return ft;
    }

    public void setOnTabChangedListener(OnTabChangeListener onTabChangeListener) {
        this.mOnTabChangeListener = onTabChangeListener;
    }

    private static final class TabInfo {
        private final String tag;
        private final Class<?> clazz;
        private final Bundle bundle;
        private Fragment fragment;

        public TabInfo(String tag, Class<?> clazz, Bundle bundle) {
            this.tag = tag;
            this.clazz = clazz;
            this.bundle = bundle;
        }
    }

    private static final class DummyTabFactory implements TabContentFactory {
        private final Context mContext;

        public DummyTabFactory(Context context) {
            mContext = context;
        }

        @Override
        public View createTabContent(String tag) {
            View v = new View(mContext);
            v.setMinimumWidth(0);
            v.setMinimumHeight(0);
            return v;
        }
    }
}
