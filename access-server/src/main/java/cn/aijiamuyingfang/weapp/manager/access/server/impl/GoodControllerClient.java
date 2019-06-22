package cn.aijiamuyingfang.weapp.manager.access.server.impl;

import java.util.List;

import cn.aijiamuyingfang.client.commons.domain.ResponseBean;
import cn.aijiamuyingfang.client.domain.classify.response.GetClassifyGoodListResponse;
import cn.aijiamuyingfang.client.domain.goods.Good;
import cn.aijiamuyingfang.client.domain.goods.GoodDetail;
import cn.aijiamuyingfang.client.rest.api.GoodControllerApi;
import cn.aijiamuyingfang.weapp.manager.access.server.rxjava.RxRetrofitClient;
import cn.aijiamuyingfang.weapp.manager.access.server.utils.RxJavaUtils;
import io.reactivex.Observable;
import okhttp3.MultipartBody;

/**
 * [描述]:
 * <p>
 * 客户端调用GoodController的服务
 * </p>
 *
 * @author ShiWei
 * @version 1.0.0
 */
public class GoodControllerClient implements GoodControllerApi {
    private static final GoodControllerApi instance;

    static {
        instance = RxRetrofitClient.createGApi(GoodControllerApi.class);
    }

    @Override
    public Observable<ResponseBean<GetClassifyGoodListResponse>> getClassifyGoodList(String classifyId, List<String> packFilter, List<String> levelFilter, String orderType, String orderValue, int currentPage, int pageSize) {
        return instance.getClassifyGoodList(classifyId, packFilter, levelFilter, orderType, orderValue, currentPage, pageSize).compose(RxJavaUtils.switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<Good>> createGood(MultipartBody goodRequest, String accessToken) {
        return instance.createGood(goodRequest, accessToken).compose(RxJavaUtils.switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<Good>> getGood(String goodId) {
        return instance.getGood(goodId).compose(RxJavaUtils.switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<Void>> deprecateGood(String goodId, String accessToken) {
        return instance.deprecateGood(goodId, accessToken).compose(RxJavaUtils.switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<GoodDetail>> getGoodDetail(String goodId) {
        return instance.getGoodDetail(goodId).compose(RxJavaUtils.switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<Good>> updateGood(String goodId, Good request, String accessToken) {
        return instance.updateGood(goodId, request, accessToken).compose(RxJavaUtils.switchSchedulers());
    }
}
