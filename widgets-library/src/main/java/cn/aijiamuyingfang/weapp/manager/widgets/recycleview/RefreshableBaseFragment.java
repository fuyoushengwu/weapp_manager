package cn.aijiamuyingfang.weapp.manager.widgets.recycleview;

import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;

import java.util.ArrayList;
import java.util.List;

import cn.aijiamuyingfang.commons.domain.PageResponse;
import cn.aijiamuyingfang.commons.domain.response.ResponseBean;
import cn.aijiamuyingfang.commons.domain.response.ResponseCode;
import cn.aijiamuyingfang.weapp.manager.access.server.utils.RxJavaUtils;
import cn.aijiamuyingfang.weapp.manager.commons.fragment.BaseFragment;
import cn.aijiamuyingfang.weapp.manager.commons.utils.ToastUtils;
import cn.aijiamuyingfang.weapp.manager.widgets.R;
import cn.aijiamuyingfang.weapp.manager.widgets.recycleview.adapter.CommonAdapter;
import cn.aijiamuyingfang.weapp.manager.widgets.recycleview.adapter.OnItemClickListener;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 可刷新的Fragment的基类
 */
public abstract class RefreshableBaseFragment<E, V extends PageResponse<E>> extends BaseFragment {
    private static final String TAG = RefreshableBaseFragment.class.getName();
    /**
     * 页面状态:初始化
     */
    private static final int STATE_INIT = 0;
    /**
     * 页面状态:正常显示
     */
    private static final int STATE_NORMAL = 1;
    /**
     * 页面状态:刷新
     */
    private static final int STATE_REFRESH = 2;
    /**
     * 页面状态:请求更多
     */
    private static final int STATE_MORE = 3;

    /**
     * 页面状态
     */
    private int mCurState = STATE_INIT;
    private MaterialRefreshLayout mRefreshLaout;
    protected RecyclerView mRecyclerView;
    protected CommonAdapter<E> mAdapter;
    private List<Disposable> disposableList = new ArrayList<>();

    /**
     * @return 当前请求的是第几页
     */
    public abstract int getCurrentPage();

    /**
     * @param currentpage 当前请求的是第几页
     */
    public abstract void setCurrentPage(int currentpage);

    /**
     * @return 一共有多少页
     */
    public abstract int getTotalPage();

    /**
     * @param totalpage 一共有多少页
     */
    public abstract void setTotalPage(int totalpage);

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
        initData();
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
                if (getCurrentPage() < getTotalPage()) {
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
        mAdapter.setOnItemClickListener(getOnItemClickListener());
    }


    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mCurState = STATE_INIT;
        setCurrentPage(1);
        setTotalPage(1);
        getData();
    }

    /**
     * 刷新数据
     */
    public void refreshData() {
        mCurState = STATE_REFRESH;
        setCurrentPage(1);
        setTotalPage(1);
        getData();
    }

    /**
     * 获取更多数据
     */
    public void loadMoreData() {
        mCurState = STATE_MORE;
        setCurrentPage(getCurrentPage() + 1);
        getData();
    }

    /**
     * 获取数据
     */
    private void getData() {
        customGetData(getCurrentPage(), 10).subscribe(new Observer<ResponseBean<V>>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposableList.add(d);
            }

            @Override
            public void onNext(ResponseBean<V> responseBean) {
                if (ResponseCode.OK.getCode().equals(responseBean.getCode())) {
                    PageResponse<E> response = responseBean.getData();
                    setCurrentPage(response.getCurrentpage());
                    setTotalPage(response.getTotalpage());
                    List<E> serverDataList = response.getDataList();
                    List<E> beforeDataList = customBeforeServerData();
                    if (beforeDataList != null && !beforeDataList.isEmpty()) {
                        serverDataList.addAll(0, beforeDataList);
                    }
                    showData(serverDataList);
                } else {
                    Log.e(TAG, responseBean.getMsg());
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "custom get data failed", e);
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "custom get data complete");
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
            case STATE_INIT:
                mAdapter.clearData();
                mAdapter.setDatas(dataList);
                mRecyclerView.scrollToPosition(0);
                mRefreshLaout.finishRefresh();
                mCurState = STATE_NORMAL;
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxJavaUtils.dispose(disposableList);
    }
}
