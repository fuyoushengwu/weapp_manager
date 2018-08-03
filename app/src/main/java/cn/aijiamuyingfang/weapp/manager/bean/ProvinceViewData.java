package cn.aijiamuyingfang.weapp.manager.bean;

import com.bigkoo.pickerview.model.IPickerViewData;

import java.util.List;

/**
 * Created by pc on 2018/5/4.
 */

public final class ProvinceViewData extends cn.aijiamuyingfang.commons.domain.address.Province implements IPickerViewData {
    private List<CityViewData> city;

    public List<CityViewData> getCity() {
        return city;
    }

    public void setCity(List<CityViewData> city) {
        this.city = city;
    }

    @Override
    public String getPickerViewText() {
        return name;
    }
}
