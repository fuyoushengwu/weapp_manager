package cn.aijiamuyingfang.weapp.manager.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.aijiamuyingfang.client.rest.api.ClassifyControllerApi;
import cn.aijiamuyingfang.commons.domain.goods.Classify;
import cn.aijiamuyingfang.commons.domain.response.ResponseBean;
import cn.aijiamuyingfang.commons.domain.response.ResponseCode;
import cn.aijiamuyingfang.commons.utils.StringUtils;
import cn.aijiamuyingfang.weapp.manager.GoodsListActivity;
import cn.aijiamuyingfang.weapp.manager.R;
import cn.aijiamuyingfang.weapp.manager.access.server.impl.ClassifyControllerClient;
import cn.aijiamuyingfang.weapp.manager.access.server.utils.RxJavaUtils;
import cn.aijiamuyingfang.weapp.manager.activity.SubClassifyActionActivity;
import cn.aijiamuyingfang.weapp.manager.commons.CommonApp;
import cn.aijiamuyingfang.weapp.manager.commons.Constant;
import cn.aijiamuyingfang.weapp.manager.commons.fragment.BaseFragment;
import cn.aijiamuyingfang.weapp.manager.recycleadapter.SubClassifyAdapter;
import cn.aijiamuyingfang.weapp.manager.recycleadapter.TopClassifyRecyclerAdapter;
import cn.aijiamuyingfang.weapp.manager.widgets.WeToolBar;
import cn.aijiamuyingfang.weapp.manager.widgets.recycleview.adapter.OnItemClickListener;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class ClassifyFragment extends BaseFragment {
    private static final String TAG = ClassifyFragment.class.getName();
    @BindView(R.id.toolbar)
    WeToolBar weToolBar;
    //顶层条目界面相关的属性
    @BindView(R.id.top_classify)
    RecyclerView mTopRecycler;
    private TopClassifyRecyclerAdapter mTopClassifyAdapter;
    //子条目界面相关的属性
    @BindView(R.id.sub_classify)
    RecyclerView mSubRecycler;
    private SubClassifyAdapter mSubClassifyAdapter;

    /**
     * 当前顶层条目的ID
     */
    private String mCurTopClassifyId;

    private ClassifyControllerApi classifyControllerApi = new ClassifyControllerClient();
    private List<Disposable> classifyDisposableList = new ArrayList<>();

    @Override
    public int getContentResourceId() {
        return R.layout.fragment_classify;
    }

    @Override
    protected void init() {
        initTopClassify();
        initSubClassify();
    }

    @Override
    public void onResume() {
        super.onResume();
        requestTopClassifies();
    }

    /**
     * 初始化顶层条目界面的Adapter
     */
    private void initTopClassify() {
        mTopClassifyAdapter = new TopClassifyRecyclerAdapter(getContext(), Collections.emptyList());
        mTopClassifyAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Classify classify = mTopClassifyAdapter.getData(position);
                mTopClassifyAdapter.setSelectPosition(position);
                mTopClassifyAdapter.notifyDataSetChanged();
                requestSubClassify(classify.getId());
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        mTopRecycler.setAdapter(mTopClassifyAdapter);
        mTopRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mTopRecycler.setItemAnimator(new DefaultItemAnimator());
    }

    /**
     * 初始化子条目界面的Adapter
     */
    private void initSubClassify() {
        mSubClassifyAdapter = new SubClassifyAdapter(getContext(), Collections.emptyList());
        mSubClassifyAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (position == 0) {
                    Intent intent = new Intent(getContext(), SubClassifyActionActivity.class);
                    intent.putExtra(Constant.INTENT_TOP_CLASSIFY_ID, mCurTopClassifyId);
                    ClassifyFragment.this.startActivity(intent);
                } else {
                    Intent intent = new Intent(getContext(), GoodsListActivity.class);
                    Classify classify = mSubClassifyAdapter.getData(position);
                    intent.putExtra(Constant.INTENT_SUB_CLASSIFY_ID, classify.getId());
                    ClassifyFragment.this.startActivity(intent);
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        mSubRecycler.setAdapter(mSubClassifyAdapter);
        mSubRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        mSubRecycler.setItemAnimator(new DefaultItemAnimator());
    }


    private void requestTopClassifies() {
        classifyControllerApi.getTopClassifyList(CommonApp.getApplication().getUserToken()).subscribe(new Observer<ResponseBean<List<Classify>>>() {
            @Override
            public void onSubscribe(Disposable d) {
                classifyDisposableList.add(d);
            }

            @Override
            public void onNext(ResponseBean<List<Classify>> responseBean) {
                if (ResponseCode.OK.getCode().equals(responseBean.getCode())) {
                    List<Classify> classifyList = responseBean.getData();
                    mTopClassifyAdapter.setDatas(classifyList);
                    mSubClassifyAdapter.clearData();
                    if (StringUtils.hasContent(mCurTopClassifyId)) {
                        requestSubClassify(mCurTopClassifyId);
                    } else if (!classifyList.isEmpty()) {
                        requestSubClassify(classifyList.get(0).getId());
                    }
                } else {
                    Log.e(TAG, responseBean.getMsg());
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "get top classify list failed", e);
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "get top classify list complete");
            }
        });
    }

    private void requestSubClassify(final String classifyId) {
        this.mCurTopClassifyId = classifyId;
        classifyControllerApi.getSubClassifyList(CommonApp.getApplication().getUserToken(), classifyId).subscribe(new Observer<ResponseBean<List<Classify>>>() {
            @Override
            public void onSubscribe(Disposable d) {
                classifyDisposableList.add(d);
            }

            @Override
            public void onNext(ResponseBean<List<Classify>> responseBean) {
                if (ResponseCode.OK.getCode().equals(responseBean.getCode())) {
                    List<Classify> subClassifies = responseBean.getData();
                    Classify addClassify = new Classify();
                    addClassify.setLevel(2);
                    addClassify.setName("添加");
                    addClassify.setCoverImg(getString(R.string.add_logo));
                    subClassifies.add(0, addClassify);
                    mSubClassifyAdapter.setDatas(responseBean.getData());
                } else {
                    Log.e(TAG, responseBean.getMsg());
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "get sub classify list failed", e);
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "get sub classify list complete");
            }
        });
    }

    @OnClick(R.id.add_top_classify)
    public void addTopClassify(View view) {
        LayoutInflater li = LayoutInflater.from(getContext());
        View promptsView = li.inflate(R.layout.add_classify_dialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getContext());
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = promptsView
                .findViewById(R.id.classify_name);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("添加", (dialog, id) -> {
                    Classify classify = new Classify();
                    classify.setLevel(1);
                    classify.setName(userInput.getText().toString());
                    classifyControllerApi.createTopClassify(CommonApp.getApplication().getUserToken(), classify).subscribe(new Observer<ResponseBean<Classify>>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            classifyDisposableList.add(d);
                        }

                        @Override
                        public void onNext(ResponseBean<Classify> responseBean) {
                            if (ResponseCode.OK.getCode().equals(responseBean.getCode())) {
                                requestTopClassifies();
                            } else {
                                Log.e(TAG, responseBean.getMsg());
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e(TAG, "create top classify failed", e);
                        }

                        @Override
                        public void onComplete() {
                            Log.i(TAG, "create top classify complete");
                        }
                    });
                })
                .setNegativeButton("取消", (dialog, id) ->
                        dialog.cancel()
                );

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxJavaUtils.dispose(classifyDisposableList);
    }
}
