package cn.aijiamuyingfang.weapp.manager.access.server.impl;


import java.util.List;
import java.util.Map;

import cn.aijiamuyingfang.client.domain.ResponseBean;
import cn.aijiamuyingfang.client.domain.previeworder.response.GetFinishedPreOrderListResponse;
import cn.aijiamuyingfang.client.domain.previeworder.response.GetPreOrderGoodListResponse;
import cn.aijiamuyingfang.client.domain.shoporder.SendType;
import cn.aijiamuyingfang.client.domain.shoporder.ShopOrder;
import cn.aijiamuyingfang.client.domain.shoporder.ShopOrderStatus;
import cn.aijiamuyingfang.client.domain.shoporder.request.CreateShopOrderRequest;
import cn.aijiamuyingfang.client.domain.shoporder.request.UpdateShopOrderStatusRequest;
import cn.aijiamuyingfang.client.domain.shoporder.response.ConfirmShopOrderFinishedResponse;
import cn.aijiamuyingfang.client.domain.shoporder.response.GetShopOrderListResponse;
import cn.aijiamuyingfang.client.domain.shoporder.response.GetShopOrderVoucherListResponse;
import cn.aijiamuyingfang.client.rest.api.ShopOrderControllerApi;
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
    public Observable<ResponseBean<GetShopOrderListResponse>> getUserShopOrderList(String username, List<ShopOrderStatus> status, List<SendType> sendType, int currentPage, int pageSize, String accessToken) {
        return instance.getUserShopOrderList(username, status, sendType, currentPage, pageSize, accessToken).compose(RxJavaUtils.switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<GetShopOrderVoucherListResponse>> getUserShopOrderVoucherList(String username, List<String> goodIdList, String accessToken) {
        return instance.getUserShopOrderVoucherList(username, goodIdList, accessToken).compose(RxJavaUtils.switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<GetShopOrderListResponse>> getShopOrderList(List<ShopOrderStatus> status, List<SendType> sendType, int currentPage, int pageSize, String accessToken) {
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
    public Observable<ResponseBean<GetFinishedPreOrderListResponse>> getFinishedPreOrderList(int currentPage, int pageSize, String accessToken) {
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
    public Observable<ResponseBean<GetPreOrderGoodListResponse>> getPreOrderGoodList(int currentPage, int pageSize, String accessToken) {
        return instance.getPreOrderGoodList(currentPage, pageSize, accessToken).compose(RxJavaUtils.switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<Void>> updatePreOrder(String goodId, String accessToken) {
        return instance.updatePreOrder(goodId, accessToken).compose(RxJavaUtils.switchSchedulers());
    }
}
