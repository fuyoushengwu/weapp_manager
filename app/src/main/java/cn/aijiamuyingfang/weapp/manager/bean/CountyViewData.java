package cn.aijiamuyingfang.weapp.manager.bean;

import com.bigkoo.pickerview.model.IPickerViewData;

/**
 * Created by pc on 2018/5/4.
 */

public class CountyViewData extends cn.aijiamuyingfang.commons.domain.address.County implements IPickerViewData {
    @Override
    public String getPickerViewText() {
        return name;
    }
}
