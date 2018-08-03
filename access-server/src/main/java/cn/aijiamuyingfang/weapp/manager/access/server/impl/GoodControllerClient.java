package cn.aijiamuyingfang.weapp.manager.access.server.impl;

import java.util.List;

import cn.aijiamuyingfang.client.rest.api.GoodControllerApi;
import cn.aijiamuyingfang.commons.domain.goods.Good;
import cn.aijiamuyingfang.commons.domain.goods.GoodDetail;
import cn.aijiamuyingfang.commons.domain.goods.response.GetClassifyGoodListResponse;
import cn.aijiamuyingfang.commons.domain.response.ResponseBean;
import cn.aijiamuyingfang.weapp.manager.access.server.rxjava.RxRetrofitClient;
import cn.aijiamuyingfang.weapp.manager.access.server.utils.RxTransformerUtils;
import io.reactivex.Observable;
import okhttp3.MultipartBody;

/**
 * [描述]:
 * <p>
 * 客户端调用GoodController的服务
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 */
public class GoodControllerClient implements GoodControllerApi {
    private static GoodControllerApi instance;

    static {
        instance = RxRetrofitClient.createGApi(GoodControllerApi.class);
    }

    @Override
    public Observable<ResponseBean<GetClassifyGoodListResponse>> getClassifyGoodList(String token, String classifyid,
            List<String> packFilter, List<String> levelFilter, String orderType, String orderValue, int currentpage,
            int pagesize) {
        return instance.getClassifyGoodList(token, classifyid, packFilter, levelFilter, orderType, orderValue,
                currentpage, pagesize)
                .compose(RxTransformerUtils.<ResponseBean<GetClassifyGoodListResponse>>switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<Good>> createGood(String token, MultipartBody goodRequest) {
        return instance.createGood(token, goodRequest).compose(RxTransformerUtils.<ResponseBean<Good>>switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<Good>> getGood(String token, String goodid) {
        return instance.getGood(token, goodid).compose(RxTransformerUtils.<ResponseBean<Good>>switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<Void>> deprecateGood(String token, String goodid) {
        return instance.deprecateGood(token, goodid).compose(RxTransformerUtils.<ResponseBean<Void>>switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<GoodDetail>> getGoodDetail(String token, String goodid) {
        return instance.getGoodDetail(token, goodid)
                .compose(RxTransformerUtils.<ResponseBean<GoodDetail>>switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<Good>> updateGood(String token, String goodid, Good request) {
        return instance.updateGood(token, goodid, request)
                .compose(RxTransformerUtils.<ResponseBean<Good>>switchSchedulers());
    }
}
