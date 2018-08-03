package cn.aijiamuyingfang.weapp.manager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import cn.aijiamuyingfang.weapp.manager.commons.Constant;
import cn.aijiamuyingfang.weapp.manager.commons.activity.BaseActivity;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class FragmentContainerActivity extends BaseActivity {
    @Override
    protected void init() {
        initFragment();
    }

    private void initFragment() {
        Intent intent = getIntent();
        String fragment = intent.getStringExtra(Constant.INTENT_FRAGMENT_NAME);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fmt = fragmentManager.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString(Constant.INTENT_FRAGMENT_FROM, intent.getStringExtra(Constant.INTENT_FRAGMENT_FROM));
        bundle.putParcelable(Constant.INTENT_SELECTED_GOODVOUCHER, intent.getParcelableExtra(Constant.INTENT_SELECTED_GOODVOUCHER));
        bundle.putParcelableArrayList(Constant.INTENT_SELECTED_VOUCHERITEM, intent.getParcelableArrayListExtra(Constant.INTENT_SELECTED_VOUCHERITEM));
        fmt.add(R.id.realtabcontent, Fragment.instantiate(this, fragment, bundle), fragment);
        fmt.commit();
    }

    @Override
    protected int getContentResourceId() {
        return R.layout.activity_fragment_container;
    }
}
