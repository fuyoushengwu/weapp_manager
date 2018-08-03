package cn.aijiamuyingfang.weapp.manager.access.server.impl;

import java.util.List;
import java.util.Map;

import cn.aijiamuyingfang.client.rest.api.ShopOrderControllerApi;
import cn.aijiamuyingfang.commons.domain.goods.Good;
import cn.aijiamuyingfang.commons.domain.response.ResponseBean;
import cn.aijiamuyingfang.commons.domain.shoporder.SendType;
import cn.aijiamuyingfang.commons.domain.shoporder.ShopOrder;
import cn.aijiamuyingfang.commons.domain.shoporder.ShopOrderStatus;
import cn.aijiamuyingfang.commons.domain.shoporder.request.CreateUserShoprderRequest;
import cn.aijiamuyingfang.commons.domain.shoporder.request.UpdateShopOrderStatusRequest;
import cn.aijiamuyingfang.commons.domain.shoporder.response.ConfirmUserShopOrderFinishedResponse;
import cn.aijiamuyingfang.commons.domain.shoporder.response.GetFinishedPreOrderListResponse;
import cn.aijiamuyingfang.commons.domain.shoporder.response.GetPreOrderGoodListResponse;
import cn.aijiamuyingfang.commons.domain.shoporder.response.GetShopOrderListResponse;
import cn.aijiamuyingfang.commons.domain.shoporder.response.GetUserShopOrderListResponse;
import cn.aijiamuyingfang.weapp.manager.access.server.rxjava.RxRetrofitClient;
import cn.aijiamuyingfang.weapp.manager.access.server.utils.RxTransformerUtils;
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
    private static ShopOrderControllerApi instance;

    static {
        instance = RxRetrofitClient.createGApi(ShopOrderControllerApi.class);
    }

    @Override
    public Observable<ResponseBean<GetUserShopOrderListResponse>> getUserShopOrderList(String token, String userid,
                                                                                       List<ShopOrderStatus> status, List<SendType> sendtype, int currentpage, int pagesize) {
        return instance.getUserShopOrderList(token, userid, status, sendtype, currentpage, pagesize)
                .compose(RxTransformerUtils.<ResponseBean<GetUserShopOrderListResponse>>switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<GetShopOrderListResponse>> getShopOrderList(String token,
                                                                               List<ShopOrderStatus> status, List<SendType> sendtype, int currentpage, int pagesize) {
        return instance.getShopOrderList(token, status, sendtype, currentpage, pagesize)
                .compose(RxTransformerUtils.<ResponseBean<GetShopOrderListResponse>>switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<Void>> updateShopOrderStatus(String token, String shoporderid,
                                                                UpdateShopOrderStatusRequest requestBean) {
        return instance.updateShopOrderStatus(token, shoporderid, requestBean)
                .compose(RxTransformerUtils.<ResponseBean<Void>>switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<Void>> delete100DaysFinishedShopOrder(String token, String shoporderid) {
        return instance.delete100DaysFinishedShopOrder(token, shoporderid)
                .compose(RxTransformerUtils.<ResponseBean<Void>>switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<Void>> deleteUserShopOrder(String token, String userid, String shoporderid) {
        return instance.deleteUserShopOrder(token, userid, shoporderid)
                .compose(RxTransformerUtils.<ResponseBean<Void>>switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<ConfirmUserShopOrderFinishedResponse>> confirmUserShopOrderFinished(String token,
                                                                                                       String userid, String shoporderid) {
        return instance.confirmUserShopOrderFinished(token, userid, shoporderid)
                .compose(RxTransformerUtils.<ResponseBean<ConfirmUserShopOrderFinishedResponse>>switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<Void>> updateUserShopOrderRecieveAddress(String token, String userid,
                                                                            String shoporderid, String addressid) {
        return instance.updateUserShopOrderRecieveAddress(token, userid, shoporderid, addressid)
                .compose(RxTransformerUtils.<ResponseBean<Void>>switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<GetFinishedPreOrderListResponse>> getFinishedPreOrderList(String token,
                                                                                             int currentpage, int pagesize) {
        return instance.getFinishedPreOrderList(token, currentpage, pagesize)
                .compose(RxTransformerUtils.<ResponseBean<GetFinishedPreOrderListResponse>>switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<ShopOrder>> getUserShopOrder(String token, String userid, String shoporderid) {
        return instance.getUserShopOrder(token, userid, shoporderid)
                .compose(RxTransformerUtils.<ResponseBean<ShopOrder>>switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<ShopOrder>> createUserShopOrder(String token, String userid,
                                                                   CreateUserShoprderRequest requestBean) {
        return instance.createUserShopOrder(token, userid, requestBean)
                .compose(RxTransformerUtils.<ResponseBean<ShopOrder>>switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<Map<String, Double>>> getUserShopOrderStatusCount(String token, String userid) {
        return instance.getUserShopOrderStatusCount(token, userid)
                .compose(RxTransformerUtils.<ResponseBean<Map<String, Double>>>switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<GetPreOrderGoodListResponse>> getPreOrderGoodList(String token, int currentpage,
                                                                                     int pagesize) {
        return instance.getPreOrderGoodList(token, currentpage, pagesize)
                .compose(RxTransformerUtils.<ResponseBean<GetPreOrderGoodListResponse>>switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<Void>> updatePreOrder(String token, String goodid, Good request) {
        return instance.updatePreOrder(token, goodid, request)
                .compose(RxTransformerUtils.<ResponseBean<Void>>switchSchedulers());
    }
}
