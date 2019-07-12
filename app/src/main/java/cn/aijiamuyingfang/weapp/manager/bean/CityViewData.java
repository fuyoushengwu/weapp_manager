package cn.aijiamuyingfang.weapp.manager.bean;

import com.bigkoo.pickerview.model.IPickerViewData;

import java.util.List;

import cn.aijiamuyingfang.vo.address.City;


/**
 * Created by pc on 2018/5/4.
 */

public class CityViewData extends City implements IPickerViewData {
    private List<CountyViewData> county;

    public List<CountyViewData> getCounty() {
        return county;
    }

    public void setCounty(List<CountyViewData> county) {
        this.county = county;
    }

    @Override
    public String getPickerViewText() {
        return name;
    }
}
