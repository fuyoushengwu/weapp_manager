package cn.aijiamuyingfang.weapp.manager.access.server.impl;


import java.util.List;
import java.util.Map;

import cn.aijiamuyingfang.vo.preorder.PagablePreOrderGoodList;
import cn.aijiamuyingfang.vo.response.ResponseBean;
import cn.aijiamuyingfang.client.rest.api.ShopOrderControllerApi;
import cn.aijiamuyingfang.vo.shoporder.ConfirmShopOrderFinishedResponse;
import cn.aijiamuyingfang.vo.shoporder.CreateShopOrderRequest;
import cn.aijiamuyingfang.vo.shoporder.PagableShopOrderList;
import cn.aijiamuyingfang.vo.shoporder.SendType;
import cn.aijiamuyingfang.vo.shoporder.ShopOrder;
import cn.aijiamuyingfang.vo.shoporder.ShopOrderStatus;
import cn.aijiamuyingfang.vo.shoporder.ShopOrderVoucher;
import cn.aijiamuyingfang.vo.shoporder.UpdateShopOrderStatusRequest;
import cn.aijiamuyingfang.weapp.manager.access.server.rxjava.RxRetrofitClient;
import cn.aijiamuyingfang.weapp.manager.access.server.utils.RxJavaUtils;
import io.reactivex.Observable;

/**
 * [描述]:
 * <p>
 * 客户端调用 ShopOrderController的服务
 * </p>
 *
 * @author ShiWei
 * @version 1.0.0
 */
public class ShopOrderControllerClient implements ShopOrderControllerApi {
    private static final ShopOrderControllerApi instance;

    static {
        instance = RxRetrofitClient.createGApi(ShopOrderControllerApi.class);
    }

    @Override
    public Observable<ResponseBean<PagableShopOrderList>> getUserShopOrderList(String username, List<ShopOrderStatus> status, List<SendType> sendType, int currentPage, int pageSize, String accessToken) {
        return instance.getUserShopOrderList(username, status, sendType, currentPage, pageSize, accessToken).compose(RxJavaUtils.switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<List<ShopOrderVoucher>>> getUserShopOrderVoucherList(String username, List<String> goodIdList, String accessToken) {
        return instance.getUserShopOrderVoucherList(username, goodIdList, accessToken).compose(RxJavaUtils.switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<PagableShopOrderList>> getShopOrderList(List<ShopOrderStatus> status, List<SendType> sendType, int currentPage, int pageSize, String accessToken) {
        return instance.getShopOrderList(status, sendType, currentPage, pageSize, accessToken).compose(RxJavaUtils.switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<Void>> updateShopOrderStatus(String shopOrderId, UpdateShopOrderStatusRequest requestBean, String accessToken) {
        return instance.updateShopOrderStatus(shopOrderId, requestBean, accessToken).compose(RxJavaUtils.switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<Void>> delete100DaysFinishedShopOrder(String shopOrderId, String accessToken) {
        return instance.delete100DaysFinishedShopOrder(shopOrderId, accessToken).compose(RxJavaUtils.switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<Void>> deleteUserShopOrder(String username, String shopOrderId, String accessToken) {
        return instance.deleteUserShopOrder(username, shopOrderId, accessToken).compose(RxJavaUtils.switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<ConfirmShopOrderFinishedResponse>> confirmUserShopOrderFinished(String username, String shopOrderId, String accessToken) {
        return instance.confirmUserShopOrderFinished(username, shopOrderId, accessToken).compose(RxJavaUtils.switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<Void>> updateUserShopOrderRecieveAddress(String username, String shopOrderId, String addressId, String accessToken) {
        return instance.updateUserShopOrderRecieveAddress(username, shopOrderId, addressId, accessToken).compose(RxJavaUtils.switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<PagableShopOrderList>> getFinishedPreOrderList(int currentPage, int pageSize, String accessToken) {
        return instance.getFinishedPreOrderList(currentPage, pageSize, accessToken).compose(RxJavaUtils.switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<ShopOrder>> getUserShopOrder(String username, String shopOrderId, String accessToken) {
        return instance.getUserShopOrder(username, shopOrderId, accessToken).compose(RxJavaUtils.switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<ShopOrder>> createUserShopOrder(String username, CreateShopOrderRequest requestBean, String accessToken) {
        return instance.createUserShopOrder(username, requestBean, accessToken).compose(RxJavaUtils.switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<Map<String, Double>>> getUserShopOrderStatusCount(String username, String accessToken) {
        return instance.getUserShopOrderStatusCount(username, accessToken).compose(RxJavaUtils.switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<PagablePreOrderGoodList>> getPreOrderGoodList(int currentPage, int pageSize, String accessToken) {
        return instance.getPreOrderGoodList(currentPage, pageSize, accessToken).compose(RxJavaUtils.switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<Void>> updatePreOrder(String goodId, String accessToken) {
        return instance.updatePreOrder(goodId, accessToken).compose(RxJavaUtils.switchSchedulers());
    }
}
