package cn.aijiamuyingfang.weapp.manager.widgets.recycleview.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by pc on 2018/3/31.
 */

public class MultiItemTypeAdapter<T> extends RecyclerView.Adapter<RecyclerViewHolder> {
    protected final Context mContext;
    protected List<T> mDataList;
    protected OnItemClickListener mOnItemClickListener;
    protected ItemViewDelegateManager<T> mItemViewDelegateManager;

    public MultiItemTypeAdapter(Context context, List<T> data) {
        mContext = context;
        mDataList = data;
        mItemViewDelegateManager = new ItemViewDelegateManager<>();
    }

    @Override
    public int getItemViewType(int position) {
        return mItemViewDelegateManager.getItemViewType(mDataList.get(position), position);
    }

    @Override
    @NonNull
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemViewDelegate<T> delegate = mItemViewDelegateManager.getItemViewDelegate(viewType);
        int layoutId = delegate.getItemViewLayoutId();
        final RecyclerViewHolder viewHolder = RecyclerViewHolder.createViewHolder(mContext, parent, layoutId);
        viewHolder.getConvertView().setOnClickListener(v -> {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(v, viewHolder, viewHolder.getAdapterPosition());
            }
        });

        viewHolder.getConvertView().setOnLongClickListener(v -> {
            if (mOnItemClickListener != null) {
                return mOnItemClickListener.onItemLongClick(v, viewHolder, viewHolder.getAdapterPosition());
            }
            return false;
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        mItemViewDelegateManager.convert(holder, mDataList.get(position), holder.getAdapterPosition());
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public MultiItemTypeAdapter<T> addItemViewDelegate(ItemViewDelegate<T> delegate) {
        mItemViewDelegateManager.addDelegate(delegate);
        return this;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    /**
     * 设置Adapter数据
     *
     * @param dataList
     */
    public void setDataList(List<T> dataList) {
        if (dataList != null) {
            this.notifyDataSetChanged();
            this.mDataList = dataList;
        }
    }

    public List<T> getDataList() {
        return mDataList;
    }

    /**
     * 获取Adapter某个位置对应的数据
     *
     * @param position
     * @return
     */
    public T getData(int position) {
        return this.mDataList.get(position);
    }

    public void clearData() {
        notifyItemRangeRemoved(0, this.mDataList.size());
        this.mDataList.clear();
    }

    public void addData(int position, List<T> dataList) {
        if (dataList != null && !dataList.isEmpty()) {
            mDataList.addAll(dataList);
            notifyItemRangeChanged(position, mDataList.size());
        }

    }
}
