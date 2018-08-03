package cn.aijiamuyingfang.weapp.manager.commons.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by pc on 2018/3/30.
 */
@SuppressWarnings("squid:MaximumInheritanceDepth")
public abstract class BaseFragment extends Fragment {
    protected View fragmentView;
    protected Bundle mSavedInstanceState;
    protected LayoutInflater mInflater;
    protected Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mInflater = inflater;
        mSavedInstanceState = savedInstanceState;
        fragmentView = mInflater.inflate(getContentResourceId(), null);
        ButterKnife.bind(this, fragmentView);
        init();
        return fragmentView;
    }

    public abstract int getContentResourceId();

    protected abstract void init();
}
