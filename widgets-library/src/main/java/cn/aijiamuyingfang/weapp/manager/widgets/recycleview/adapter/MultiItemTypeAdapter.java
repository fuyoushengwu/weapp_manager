package cn.aijiamuyingfang.weapp.manager.widgets.recycleview.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by pc on 2018/3/31.
 */

public class MultiItemTypeAdapter<T> extends RecyclerView.Adapter<RecyclerViewHolder> {

    protected Context mContext;
    protected List<T> mDatas;
    protected OnItemClickListener mOnItemClickListener;
    protected ItemViewDelegateManager<T> mItemViewDelegateManager;

    public MultiItemTypeAdapter(Context context, List<T> data) {
        mContext = context;
        mDatas = data;
        mItemViewDelegateManager = new ItemViewDelegateManager<>();
    }

    @Override
    public int getItemViewType(int position) {
        return mItemViewDelegateManager.getItemViewType(mDatas.get(position), position);
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemViewDelegate<T> delegate = mItemViewDelegateManager.getItemViewDelegate(viewType);
        int layoutId = delegate.getItemViewLayoutId();
        final RecyclerViewHolder viewHolder = RecyclerViewHolder.createViewHolder(mContext, parent, layoutId);
        viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(v, viewHolder, viewHolder.getAdapterPosition());
                }
            }
        });

        viewHolder.getConvertView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnItemClickListener != null) {
                    return mOnItemClickListener.onItemLongClick(v, viewHolder, viewHolder.getAdapterPosition());
                }
                return false;
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        mItemViewDelegateManager.convert(holder, mDatas.get(position), holder.getAdapterPosition());
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
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
     * @param datas
     */
    public void setDatas(List<T> datas) {
        if (datas != null) {
            this.mDatas = datas;
            this.notifyDataSetChanged();
        }
    }

    public List<T> getDatas() {
        return mDatas;
    }

    public void removeData(int position) {
        mDatas.remove(position);
        notifyItemRangeRemoved(position, 1);
    }

    /**
     * 获取Adapter某个位置对应的数据
     *
     * @param position
     * @return
     */
    public T getData(int position) {
        return this.mDatas.get(position);
    }

    public void clearData() {
        this.mDatas.clear();
        notifyItemRangeRemoved(0, this.mDatas.size());
    }

    public void addData(List<T> datas) {
        addData(0, datas);
    }

    public void addData(int position, List<T> datas) {
        if (datas != null && !datas.isEmpty()) {
            mDatas.addAll(datas);
            notifyItemRangeChanged(position, mDatas.size());
        }

    }
}
