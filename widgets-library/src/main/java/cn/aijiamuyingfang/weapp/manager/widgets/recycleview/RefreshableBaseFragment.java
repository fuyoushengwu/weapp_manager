package cn.aijiamuyingfang.weapp.manager.widgets.recycleview;

import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;

import java.util.List;

import cn.aijiamuyingfang.commons.domain.PageResponse;
import cn.aijiamuyingfang.commons.domain.response.ResponseBean;
import cn.aijiamuyingfang.commons.domain.response.ResponseCode;
import cn.aijiamuyingfang.weapp.manager.commons.fragment.BaseFragment;
import cn.aijiamuyingfang.weapp.manager.commons.utils.ToastUtils;
import cn.aijiamuyingfang.weapp.manager.widgets.R;
import cn.aijiamuyingfang.weapp.manager.widgets.recycleview.adapter.CommonAdapter;
import cn.aijiamuyingfang.weapp.manager.widgets.recycleview.adapter.OnItemClickListener;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * 可刷新的Fragment的基类
 */
public abstract class RefreshableBaseFragment<E, V extends PageResponse<E>> extends BaseFragment {

    /**
     * 页面状态:正常显示
     */
    private static final int STATE_NORMAL = 0;
    /**
     * 页面状态:刷新
     */
    private static final int STATE_REFRESH = 1;
    /**
     * 页面状态:请求更多
     */
    private static final int STATE_MORE = 2;

    /**
     * 页面状态
     */
    private int mCurState = STATE_NORMAL;
    private int mCurrPage = 1;//当前请求的是第几页
    private int mTotalPage = 1;//一共有多少页
    private MaterialRefreshLayout mRefreshLaout;
    private RecyclerView mRecyclerView;
    protected CommonAdapter<E> mAdapter;

    @Override
    public int getContentResourceId() {
        return R.layout.fragment_refreshable;
    }

    @Override
    protected void init() {
        mRefreshLaout = fragmentView.findViewById(R.id.refreshable_mr);
        mRecyclerView = fragmentView.findViewById(R.id.refreshable_view);
        initRefreshLayout();
        initRecyclerViewAdapter();
        customRecyclerView();
    }

    /**
     * 初始化MaterialRefreshLayout
     */
    private void initRefreshLayout() {
        mRefreshLaout.setLoadMore(true);
        mRefreshLaout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                refreshData();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                super.onRefreshLoadMore(materialRefreshLayout);
                if (mCurrPage < mTotalPage) {
                    loadMoreData();
                } else {
                    ToastUtils.showSafeToast(mContext, "没有更多数据啦");
                    mRefreshLaout.finishRefreshLoadMore();
                }
            }
        });
    }

    /**
     * 初始化列表项适配器
     */
    private void initRecyclerViewAdapter() {
        mAdapter = getRecyclerViewAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext,
                DividerItemDecoration.HORIZONTAL));
        mAdapter.setOnItemClickListener(getOnItemClickListener());
    }

    @Override
    public void onStart() {
        super.onStart();
        refreshData();
    }

    /**
     * 刷新数据
     */
    public void refreshData() {
        mCurState = STATE_REFRESH;
        mCurrPage = 1;
        getData();
    }

    /**
     * 获取更多数据
     */
    public void loadMoreData() {
        mCurState = STATE_MORE;
        mCurrPage++;
        getData();
    }

    /**
     * 获取数据
     */
    private void getData() {
        customGetData(mCurrPage, 10).subscribe(new Consumer<ResponseBean<V>>() {
            @Override
            public void accept(ResponseBean<V> responseBean) {
                if (ResponseCode.OK.getCode().equals(responseBean.getCode())) {
                    PageResponse<E> response = responseBean.getData();
                    mCurrPage = response.getCurrentpage();
                    mTotalPage = response.getTotalpage();
                    List<E> serverDataList = response.getDataList();
                    List<E> beforeDataList = customBeforeServerData();
                    if (beforeDataList != null && !beforeDataList.isEmpty()) {
                        serverDataList.addAll(0, beforeDataList);
                    }
                    showData(serverDataList);
                }
            }
        });
    }


    /**
     * @param dataList 数据
     */
    private void showData(List<E> dataList) {
        switch (mCurState) {
            case STATE_NORMAL:
                break;
            case STATE_REFRESH:
                mAdapter.clearData();
                mAdapter.setDatas(dataList);
                mRecyclerView.scrollToPosition(0);
                mRefreshLaout.finishRefresh();
                ToastUtils.showSafeToast(mContext, "刷新完成");
                mCurState = STATE_NORMAL;
                break;
            case STATE_MORE:
                mAdapter.addData(mAdapter.getDatas().size(), dataList);
                mRecyclerView.scrollToPosition(mAdapter.getDatas().size());
                mRefreshLaout.finishRefreshLoadMore();
                mCurState = STATE_NORMAL;
                break;
            default:
                break;
        }
    }

    /**
     * @param position 位置
     * @return 获取第position个数据
     */
    protected E getData(int position) {
        if (mAdapter != null) {
            return mAdapter.getData(position);
        }
        return null;
    }


    /**
     * @return 获取列表项的数据适配
     */
    @NonNull
    public abstract CommonAdapter<E> getRecyclerViewAdapter();

    /**
     * @return 获取 列表项的点击监听器
     */
    public abstract OnItemClickListener getOnItemClickListener();

    /**
     * 根据子类需要对列表展示进行定制
     */
    public abstract void customRecyclerView();

    /**
     * @param mCurrPage 当前页
     * @param mPageSize 每页大小
     * @return 获取数据
     */
    protected abstract Observable<ResponseBean<V>> customGetData(int mCurrPage, int mPageSize);

    /**
     * @return 在从Server段获取的数据前, 定制要显示的数据
     */
    protected abstract List<E> customBeforeServerData();

}
