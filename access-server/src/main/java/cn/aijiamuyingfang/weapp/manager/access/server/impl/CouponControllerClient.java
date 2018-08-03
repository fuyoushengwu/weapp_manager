package cn.aijiamuyingfang.weapp.manager.access.server.impl;

import java.util.List;

import cn.aijiamuyingfang.client.rest.api.CouponControllerApi;
import cn.aijiamuyingfang.commons.domain.coupon.GoodVoucher;
import cn.aijiamuyingfang.commons.domain.coupon.VoucherItem;
import cn.aijiamuyingfang.commons.domain.coupon.response.GetGoodVoucherListResponse;
import cn.aijiamuyingfang.commons.domain.coupon.response.GetShopOrderVoucherListResponse;
import cn.aijiamuyingfang.commons.domain.coupon.response.GetUserVoucherListResponse;
import cn.aijiamuyingfang.commons.domain.coupon.response.GetVoucherItemListResponse;
import cn.aijiamuyingfang.commons.domain.response.ResponseBean;
import cn.aijiamuyingfang.weapp.manager.access.server.rxjava.RxRetrofitClient;
import cn.aijiamuyingfang.weapp.manager.access.server.utils.RxTransformerUtils;
import io.reactivex.Observable;

/**
 * [描述]:
 * <p>
 * 客户端调用CouponController的服务
 * </p>
 *
 * @author ShiWei
 * @version 1.0.0
 */
public class CouponControllerClient implements CouponControllerApi {
    private static CouponControllerApi instance;

    static {
        instance = RxRetrofitClient.createGApi(CouponControllerApi.class);
    }

    @Override
    public Observable<ResponseBean<GetUserVoucherListResponse>> getUserVoucherList(String token, String userid,
                                                                                   int currentpage, int pagesize) {
        return instance.getUserVoucherList(token, userid, currentpage, pagesize)
                .compose(RxTransformerUtils.<ResponseBean<GetUserVoucherListResponse>>switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<GetShopOrderVoucherListResponse>> getUserShopOrderVoucherList(String token,
                                                                                                 String userid, List<String> goodids) {
        return instance.getUserShopOrderVoucherList(token, userid, goodids)
                .compose(RxTransformerUtils.<ResponseBean<GetShopOrderVoucherListResponse>>switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<GetGoodVoucherListResponse>> getGoodVoucherList(String token, int currentpage,
                                                                                   int pagesize) {
        return instance.getGoodVoucherList(token, currentpage, pagesize)
                .compose(RxTransformerUtils.<ResponseBean<GetGoodVoucherListResponse>>switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<GoodVoucher>> getGoodVoucher(String token, String voucherid) {
        return instance.getGoodVoucher(token, voucherid)
                .compose(RxTransformerUtils.<ResponseBean<GoodVoucher>>switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<GoodVoucher>> createGoodVoucher(String token, GoodVoucher request) {
        return instance.createGoodVoucher(token, request)
                .compose(RxTransformerUtils.<ResponseBean<GoodVoucher>>switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<Void>> deprecateGoodVoucher(String token, String voucherid) {
        return instance.deprecateGoodVoucher(token, voucherid)
                .compose(RxTransformerUtils.<ResponseBean<Void>>switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<GetVoucherItemListResponse>> getVoucherItemList(String token, int currentpage,
                                                                                   int pagesize) {
        return instance.getVoucherItemList(token, currentpage, pagesize)
                .compose(RxTransformerUtils.<ResponseBean<GetVoucherItemListResponse>>switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<VoucherItem>> getVoucherItem(String token, String itemid) {
        return instance.getVoucherItem(token, itemid)
                .compose(RxTransformerUtils.<ResponseBean<VoucherItem>>switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<VoucherItem>> createVoucherItem(String token, VoucherItem request) {
        return instance.createVoucherItem(token, request).compose(RxTransformerUtils.<ResponseBean<VoucherItem>>switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<Void>> deprecateVoucherItem(String token, String itemid) {
        return instance.deprecateVoucherItem(token, itemid).compose(RxTransformerUtils.<ResponseBean<Void>>switchSchedulers());
    }
}
