package cn.aijiamuyingfang.weapp.manager.recycleadapter;

import android.content.Context;

import java.util.List;

import cn.aijiamuyingfang.commons.domain.user.UserMessage;
import cn.aijiamuyingfang.weapp.manager.R;
import cn.aijiamuyingfang.weapp.manager.widgets.recycleview.adapter.CommonAdapter;
import cn.aijiamuyingfang.weapp.manager.widgets.recycleview.adapter.RecyclerViewHolder;

public class MessageAdapter extends CommonAdapter<UserMessage> {

    public MessageAdapter(Context context, List<UserMessage> data) {
        super(context, data, R.layout.item_message);
    }

    @Override
    protected void convert(RecyclerViewHolder viewHolder, final UserMessage itemData, int position) {
        viewHolder.setText(R.id.message_title, "标题:" + itemData.getTitle());
        viewHolder.setText(R.id.message_roundup, "摘要:" + itemData.getRoundup());
        viewHolder.setText(R.id.message_createTime, "创建时间:" + itemData.getCreateTime());
        viewHolder.setText(R.id.message_createTime, "截止日期:" + itemData.getFinishTime());
    }
}