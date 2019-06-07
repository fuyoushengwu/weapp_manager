package cn.aijiamuyingfang.weapp.manager.bean;

import com.bigkoo.pickerview.model.IPickerViewData;

import java.util.List;

import cn.aijiamuyingfang.client.domain.address.Province;

/**
 * Created by pc on 2018/5/4.
 */

public final class ProvinceViewData extends Province implements IPickerViewData {
    private List<CityViewData> city;

    public List<CityViewData> getCity() {
        return city;
    }

    @Override
    public String getPickerViewText() {
        return name;
    }
}
