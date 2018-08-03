package cn.aijiamuyingfang.weapp.manager.access.server.impl;

import java.util.List;

import cn.aijiamuyingfang.client.rest.api.StoreControllerApi;
import cn.aijiamuyingfang.commons.domain.goods.Store;
import cn.aijiamuyingfang.commons.domain.goods.response.GetDefaultStoreIdResponse;
import cn.aijiamuyingfang.commons.domain.goods.response.GetInUseStoreListResponse;
import cn.aijiamuyingfang.commons.domain.response.ResponseBean;
import cn.aijiamuyingfang.weapp.manager.access.server.rxjava.RxRetrofitClient;
import cn.aijiamuyingfang.weapp.manager.access.server.utils.RxTransformerUtils;
import io.reactivex.Observable;
import okhttp3.MultipartBody;

/**
 * [描述]:
 * <p>
 * 客户端调用StoreController的服务
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 */
public class StoreControllerClient implements StoreControllerApi {
    private static StoreControllerApi instance;

    static {
        instance = RxRetrofitClient.createGApi(StoreControllerApi.class);
    }

    @Override
    public Observable<ResponseBean<GetInUseStoreListResponse>> getInUseStoreList(String token, int currentpage,
            int pagesize) {
        return instance.getInUseStoreList(token, currentpage, pagesize)
                .compose(RxTransformerUtils.<ResponseBean<GetInUseStoreListResponse>>switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<Store>> createStore(String token, MultipartBody storeRequest) {
        return instance.createStore(token, storeRequest).compose(RxTransformerUtils.<ResponseBean<Store>>switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<Store>> getStore(String token, String storeid) {
        return instance.getStore(token, storeid).compose(RxTransformerUtils.<ResponseBean<Store>>switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<Store>> updateStore(String token, String storeid, Store storeRequest) {
        return instance.updateStore(token, storeid, storeRequest)
                .compose(RxTransformerUtils.<ResponseBean<Store>>switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<Void>> deprecateStore(String token, String storeid) {
        return instance.deprecateStore(token, storeid).compose(RxTransformerUtils.<ResponseBean<Void>>switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<GetDefaultStoreIdResponse>> getDefaultStoreId(String token) {
        return instance.getDefaultStoreId(token)
                .compose(RxTransformerUtils.<ResponseBean<GetDefaultStoreIdResponse>>switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<List<String>>> getStoresCity(String token) {
        return instance.getStoresCity(token).compose(RxTransformerUtils.<ResponseBean<List<String>>>switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<Void>> addStoreClassify(String token, String storeid, String classifyid) {
        return instance.addStoreClassify(token, storeid, classifyid)
                .compose(RxTransformerUtils.<ResponseBean<Void>>switchSchedulers());
    }
}
