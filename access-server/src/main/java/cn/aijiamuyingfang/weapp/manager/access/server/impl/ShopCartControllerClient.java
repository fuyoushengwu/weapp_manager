package cn.aijiamuyingfang.weapp.manager.access.server.impl;

import cn.aijiamuyingfang.client.rest.api.ShopCartControllerApi;
import cn.aijiamuyingfang.commons.domain.response.ResponseBean;
import cn.aijiamuyingfang.commons.domain.shopcart.AddShopCartItemRequest;
import cn.aijiamuyingfang.commons.domain.shopcart.ShopCartItem;
import cn.aijiamuyingfang.commons.domain.shopcart.response.GetShopCartItemListResponse;
import cn.aijiamuyingfang.weapp.manager.access.server.rxjava.RxRetrofitClient;
import cn.aijiamuyingfang.weapp.manager.access.server.utils.RxTransformerUtils;
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
    private static ShopCartControllerApi instance;

    static {
        instance = RxRetrofitClient.createGApi(ShopCartControllerApi.class);
    }

    @Override
    public Observable<ResponseBean<ShopCartItem>> addShopCartItem(String token, String userid,
                                                                  AddShopCartItemRequest requestBean) {
        return instance.addShopCartItem(token, userid, requestBean)
                .compose(RxTransformerUtils.<ResponseBean<ShopCartItem>>switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<GetShopCartItemListResponse>> getShopCartItemList(String token, String userid,
                                                                                     int currentpage, int pagesize) {
        return instance.getShopCartItemList(token, userid, currentpage, pagesize)
                .compose(RxTransformerUtils.<ResponseBean<GetShopCartItemListResponse>>switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<Void>> checkAllShopCartItem(String token, String userid, boolean ischecked) {
        return instance.checkAllShopCartItem(token, userid, ischecked)
                .compose(RxTransformerUtils.<ResponseBean<Void>>switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<Void>> checkShopCartItem(String token, String userid, String shopcartid,
                                                            boolean ischecked) {
        return instance.checkShopCartItem(token, userid, shopcartid, ischecked)
                .compose(RxTransformerUtils.<ResponseBean<Void>>switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<Void>> deleteShopCartItem(String token, String userid, String shopcartid) {
        return instance.deleteShopCartItem(token, userid, shopcartid)
                .compose(RxTransformerUtils.<ResponseBean<Void>>switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<ShopCartItem>> updateShopCartItemCount(String token, String userid,
                                                                          String shopcartid, int count) {
        return instance.updateShopCartItemCount(token, userid, shopcartid, count)
                .compose(RxTransformerUtils.<ResponseBean<ShopCartItem>>switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<Void>> deleteGood(String token, String goodid) {
        return instance.deleteGood(token, goodid).compose(RxTransformerUtils.<ResponseBean<Void>>switchSchedulers());
    }
}
