package cn.aijiamuyingfang.weapp.manager.access.server.impl;

import cn.aijiamuyingfang.client.domain.ResponseBean;
import cn.aijiamuyingfang.client.domain.shopcart.ShopCart;
import cn.aijiamuyingfang.client.domain.shopcart.response.GetShopCartListResponse;
import cn.aijiamuyingfang.client.domain.shoporder.request.CreateShopCartRequest;
import cn.aijiamuyingfang.client.rest.api.ShopCartControllerApi;
import cn.aijiamuyingfang.weapp.manager.access.server.rxjava.RxRetrofitClient;
import cn.aijiamuyingfang.weapp.manager.access.server.utils.RxJavaUtils;
import io.reactivex.Observable;

/**
 * [描述]:
 * <p>
 * 客户端调用 ShopCartController的服务
 * </p>
 *
 * @author ShiWei
 * @version 1.0.0
 */
public class ShopCartControllerClient implements ShopCartControllerApi {
    private static final ShopCartControllerApi instance;

    static {
        instance = RxRetrofitClient.createGApi(ShopCartControllerApi.class);
    }

    @Override
    public Observable<ResponseBean<ShopCart>> addShopCart(String username, CreateShopCartRequest requestBean, String accessToken) {
        return instance.addShopCart(username, requestBean, accessToken).compose(RxJavaUtils.switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<GetShopCartListResponse>> getShopCartList(String username, int currentPage, int pageSize, String accessToken) {
        return instance.getShopCartList(username, currentPage, pageSize, accessToken).compose(RxJavaUtils.switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<Void>> checkAllShopCart(String username, boolean checked, String accessToken) {
        return instance.checkAllShopCart(username, checked, accessToken).compose(RxJavaUtils.switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<Void>> checkShopCart(String username, String shopCartId, boolean checked, String accessToken) {
        return instance.checkShopCart(username, shopCartId, checked, accessToken).compose(RxJavaUtils.switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<Void>> deleteShopCart(String username, String shopCartId, String accessToken) {
        return instance.deleteShopCart(username, shopCartId, accessToken).compose(RxJavaUtils.switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<ShopCart>> updateShopCartCount(String username, String shopCartId, int count, String accessToken) {
        return instance.updateShopCartCount(username, shopCartId, count, accessToken).compose(RxJavaUtils.switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<Void>> deleteGood(String goodId, String accessToken) {
        return instance.deleteGood(goodId, accessToken).compose(RxJavaUtils.switchSchedulers());
    }
}
