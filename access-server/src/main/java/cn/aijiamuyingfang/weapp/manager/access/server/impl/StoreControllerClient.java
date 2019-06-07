package cn.aijiamuyingfang.weapp.manager.access.server.impl;

import java.util.List;

import cn.aijiamuyingfang.client.domain.ResponseBean;
import cn.aijiamuyingfang.client.domain.store.Store;
import cn.aijiamuyingfang.client.domain.store.StoreAddress;
import cn.aijiamuyingfang.client.domain.store.response.GetDefaultStoreIdResponse;
import cn.aijiamuyingfang.client.domain.store.response.GetInUseStoreListResponse;
import cn.aijiamuyingfang.client.rest.api.StoreControllerApi;
import cn.aijiamuyingfang.weapp.manager.access.server.rxjava.RxRetrofitClient;
import cn.aijiamuyingfang.weapp.manager.access.server.utils.RxJavaUtils;
import io.reactivex.Observable;
import okhttp3.MultipartBody;

/**
 * [描述]:
 * <p>
 * 客户端调用StoreController的服务
 * </p>
 *
 * @author ShiWei
 * @version 1.0.0
 */
public class StoreControllerClient implements StoreControllerApi {
    private static final StoreControllerApi instance;

    static {
        instance = RxRetrofitClient.createGApi(StoreControllerApi.class);
    }

    @Override
    public Observable<ResponseBean<GetInUseStoreListResponse>> getInUseStoreList(int currentPage, int pageSize) {
        return instance.getInUseStoreList(currentPage, pageSize).compose(RxJavaUtils.switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<Store>> createStore(MultipartBody storeRequest, String accessToken) {
        return instance.createStore(storeRequest, accessToken).compose(RxJavaUtils.switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<Store>> getStore(String storeId) {
        return instance.getStore(storeId).compose(RxJavaUtils.switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<StoreAddress>> getStoreAddressByAddressId(String addressId) {
        return instance.getStoreAddressByAddressId(addressId).compose(RxJavaUtils.switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<Store>> updateStore(String storeId, Store storeRequest, String accessToken) {
        return instance.updateStore(storeId, storeRequest, accessToken).compose(RxJavaUtils.switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<Void>> deprecateStore(String storeId, String accessToken) {
        return instance.deprecateStore(storeId, accessToken).compose(RxJavaUtils.switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<GetDefaultStoreIdResponse>> getDefaultStoreId() {
        return instance.getDefaultStoreId().compose(RxJavaUtils.switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<List<String>>> getStoresCity() {
        return instance.getStoresCity().compose(RxJavaUtils.switchSchedulers());
    }
}
