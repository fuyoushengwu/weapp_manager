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
import cn.aijiamuyingfang.client.domain.message.UserMessage;
import cn.aijiamuyingfang.client.domain.message.response.GetMessagesListResponse;
import cn.aijiamuyingfang.client.rest.api.UserMessageControllerApi;
import cn.aijiamuyingfang.weapp.manager.R;
import cn.aijiamuyingfang.weapp.manager.access.server.impl.UserMessageControllerClient;
import cn.aijiamuyingfang.weapp.manager.activity.MessageActionActivity;
import cn.aijiamuyingfang.weapp.manager.commons.CommonApp;
import cn.aijiamuyingfang.weapp.manager.commons.Constant;
import cn.aijiamuyingfang.weapp.manager.recycleadapter.MessageAdapter;
import cn.aijiamuyingfang.weapp.manager.widgets.WeToolBar;
import cn.aijiamuyingfang.weapp.manager.widgets.recycleview.RefreshableBaseFragment;
import cn.aijiamuyingfang.weapp.manager.widgets.recycleview.adapter.CommonAdapter;
import cn.aijiamuyingfang.weapp.manager.widgets.recycleview.adapter.OnItemClickListener;
import io.reactivex.Observable;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class MessageFragment extends RefreshableBaseFragment<UserMessage, GetMessagesListResponse> {
    private static final UserMessageControllerApi userMessageControllerApi = new UserMessageControllerClient();

    @BindView(R.id.toolbar)
    WeToolBar mToolBar;

    private int mCurrPage; // 当前页
    private int mTotalPage; // 总页数

    @NonNull
    @Override
    public CommonAdapter<UserMessage> getRecyclerViewAdapter() {
        return new MessageAdapter(getContext(), new ArrayList<>());
    }

    @Override
    public OnItemClickListener getOnItemClickListener() {
        return new OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                //进入编辑页
                Intent intent = new Intent(getContext(), MessageActionActivity.class);
                intent.putExtra(Constant.INTENT_MESSAGE, mAdapter.getData(position));
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
        mToolBar.setTitle("消息");
        mToolBar.setRightButtonText("添加");
        mToolBar.getRightButton().setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MessageActionActivity.class);
            MessageFragment.this.startActivity(intent);
        });
    }

    @Override
    protected Observable<ResponseBean<GetMessagesListResponse>> customGetData(int mCurrPage, int mPageSize) {
        return userMessageControllerApi.getUserMessageList(CommonApp.getApplication().getUsername(), mCurrPage, mPageSize, CommonApp.getApplication().getUserToken());
    }

    @Override
    protected List<UserMessage> customBeforeServerData() {
        return Collections.emptyList();
    }

    @Override
    public int getCurrentPage() {
        return mCurrPage;
    }

    @Override
    public void setCurrentPage(int currentPage) {
        this.mCurrPage = currentPage;
    }

    @Override
    public int getTotalPage() {
        return this.mTotalPage;
    }

    @Override
    public void setTotalPage(int totalPage) {
        this.mTotalPage = totalPage;
    }
}
