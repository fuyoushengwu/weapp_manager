package cn.aijiamuyingfang.weapp.manager.fragment;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import cn.aijiamuyingfang.client.commons.domain.ResponseBean;
import cn.aijiamuyingfang.client.domain.store.Store;
import cn.aijiamuyingfang.client.domain.store.response.GetInUseStoreListResponse;
import cn.aijiamuyingfang.client.rest.api.StoreControllerApi;
import cn.aijiamuyingfang.weapp.manager.R;
import cn.aijiamuyingfang.weapp.manager.access.server.impl.StoreControllerClient;
import cn.aijiamuyingfang.weapp.manager.activity.StoreActionActivity;
import cn.aijiamuyingfang.weapp.manager.commons.Constant;
import cn.aijiamuyingfang.weapp.manager.recycleadapter.StoreAdapter;
import cn.aijiamuyingfang.weapp.manager.widgets.WeToolBar;
import cn.aijiamuyingfang.weapp.manager.widgets.recycleview.RefreshableBaseFragment;
import cn.aijiamuyingfang.weapp.manager.widgets.recycleview.adapter.CommonAdapter;
import cn.aijiamuyingfang.weapp.manager.widgets.recycleview.adapter.OnItemClickListener;
import io.reactivex.Observable;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class StoreFragment extends RefreshableBaseFragment<Store, GetInUseStoreListResponse> {
    private static final StoreControllerApi storeControllerApi = new StoreControllerClient();
    @BindView(R.id.toolbar)
    WeToolBar mToolBar;

    private int mCurrPage; // 当前页
    private int mTotalPage; // 总页数

    @NonNull
    @Override
    public CommonAdapter<Store> getRecyclerViewAdapter() {
        return new StoreAdapter(getContext(), new ArrayList<>());
    }

    @Override
    public OnItemClickListener getOnItemClickListener() {
        return new OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                //进入编辑页
                Intent intent = new Intent(getContext(), StoreActionActivity.class);
                intent.putExtra(Constant.INTENT_STORE, mAdapter.getData(position));
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        };
    }

    @Override
    public void customRecyclerView() {
        mToolBar.setTitle("门店");
        mToolBar.setRightButtonText("添加");
        mToolBar.setRightButtonOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), StoreActionActivity.class);
            StoreFragment.this.startActivity(intent);
        });
    }

    @Override
    protected Observable<ResponseBean<GetInUseStoreListResponse>> customGetData(int mCurrPage, int mPageSize) {
        return storeControllerApi.getInUseStoreList(mCurrPage, mPageSize);
    }

    @Override
    protected List<Store> customBeforeServerData() {
        return Collections.emptyList();
    }

    @Override
    public int getCurrentPage() {
        return mCurrPage;
    }

    @Override
    public void setCurrentPage(int currentpage) {
        this.mCurrPage = currentpage;
    }

    @Override
    public int getTotalPage() {
        return this.mTotalPage;
    }

    @Override
    public void setTotalPage(int totalpage) {
        this.mTotalPage = totalpage;
    }
}
