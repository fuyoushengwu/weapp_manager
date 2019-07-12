package cn.aijiamuyingfang.weapp.manager.bean;

import com.bigkoo.pickerview.model.IPickerViewData;

import cn.aijiamuyingfang.vo.address.County;


/**
 * Created by pc on 2018/5/4.
 */

public class CountyViewData extends County implements IPickerViewData {
    @Override
    public String getPickerViewText() {
        return name;
    }
}
