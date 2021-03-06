package cn.aijiamuyingfang.weapp.manager;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import cn.aijiamuyingfang.vo.ImageSource;
import cn.aijiamuyingfang.vo.goods.Good;
import cn.aijiamuyingfang.client.rest.api.GoodControllerApi;
import cn.aijiamuyingfang.vo.goods.PagableGoodList;
import cn.aijiamuyingfang.vo.response.ResponseBean;
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
public class GoodsListActivity extends RefreshableBaseActivity<Good, PagableGoodList> {
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
                mCurrPosition = position;
                if (position == 0) {
                    Intent intent = new Intent(GoodsListActivity.this, GoodActionActivity.class);
                    intent.putExtra(Constant.INTENT_SUB_CLASSIFY_ID, mCurClassifyId);
                    GoodsListActivity.this.startActivity(intent);
                } else {
                    Intent intent = new Intent(GoodsListActivity.this, GoodActionActivity.class);
                    intent.putExtra(Constant.INTENT_GOOD, mAdapter.getData(position));
                    startActivityForResult(intent, Constant.REQUEST_GOOD_ACTION);
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
    protected Observable<ResponseBean<PagableGoodList>> customGetData(int mCurrPage, int mPageSize) {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null == data) {
            return;
        }
        Good good = data.getParcelableExtra(Constant.INTENT_GOOD);
        if (null == good) {
            return;
        }
        mAdapter.getDataList().set(mCurrPosition, good);
        mAdapter.notifyItemChanged(mCurrPosition);
    }
}