package cn.aijiamuyingfang.weapp.manager;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import cn.aijiamuyingfang.client.domain.ImageSource;
import cn.aijiamuyingfang.client.commons.domain.ResponseBean;
import cn.aijiamuyingfang.client.domain.classify.response.GetClassifyGoodListResponse;
import cn.aijiamuyingfang.client.domain.goods.Good;
import cn.aijiamuyingfang.client.rest.api.GoodControllerApi;
import cn.aijiamuyingfang.weapp.manager.access.server.impl.GoodControllerClient;
import cn.aijiamuyingfang.weapp.manager.activity.GoodActionActivity;
import cn.aijiamuyingfang.weapp.manager.commons.Constant;
import cn.aijiamuyingfang.weapp.manager.recycleadapter.GoodListAdapter;
import cn.aijiamuyingfang.weapp.manager.widgets.WeToolBar;
import cn.aijiamuyingfang.weapp.manager.widgets.recycleview.RefreshableBaseActivity;
import cn.aijiamuyingfang.weapp.manager.widgets.recycleview.adapter.CommonAdapter;
import cn.aijiamuyingfang.weapp.manager.widgets.recycleview.adapter.OnItemClickListener;
import io.reactivex.Observable;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class GoodsListActivity extends RefreshableBaseActivity<Good, GetClassifyGoodListResponse> {
    private static final GoodControllerApi goodControllerApi = new GoodControllerClient();

    @BindView(R.id.toolbar)
    WeToolBar mToolBar;
    /**
     * 当前条目的ID
     */
    private String mCurClassifyId;

    @NonNull
    @Override
    public CommonAdapter<Good> getRecyclerViewAdapter() {
        return new GoodListAdapter(this, Collections.emptyList());
    }

    @Override
    public OnItemClickListener getOnItemClickListener() {
        return new OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (position == 0) {
                    Intent intent = new Intent(GoodsListActivity.this, GoodActionActivity.class);
                    intent.putExtra(Constant.INTENT_SUB_CLASSIFY_ID, mCurClassifyId);
                    GoodsListActivity.this.startActivity(intent);
                } else {
                    Intent intent = new Intent(GoodsListActivity.this, GoodActionActivity.class);
                    intent.putExtra(Constant.INTENT_GOOD, mAdapter.getData(position));
                    startActivity(intent);
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        };
    }

    @Override
    public void customRecyclerView() {
        mCurClassifyId = getIntent().getStringExtra(Constant.INTENT_SUB_CLASSIFY_ID);
        mToolBar.setNavigationOnClickListener(v -> GoodsListActivity.this.finish());
    }

    @Override
    protected Observable<ResponseBean<GetClassifyGoodListResponse>> customGetData(int mCurrPage, int mPageSize) {
        return goodControllerApi.getClassifyGoodList(mCurClassifyId, null,
                null, null, null, mCurrPage, mPageSize);
    }

    @Override
    protected List<Good> customBeforeServerData() {
        List<Good> goodList = new ArrayList<>();
        Good good = new Good();
        good.setName("添加");
        good.setCoverImg(new ImageSource("", getString(R.string.add_logo)));
        goodList.add(good);
        return goodList;
    }

    @Override
    public int getContentResourceId() {
        return R.layout.activity_goods_list;
    }
}