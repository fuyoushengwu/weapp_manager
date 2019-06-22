package cn.aijiamuyingfang.weapp.manager.access.server.impl;


import cn.aijiamuyingfang.client.commons.domain.ResponseBean;
import cn.aijiamuyingfang.client.domain.coupon.GoodVoucher;
import cn.aijiamuyingfang.client.domain.coupon.VoucherItem;
import cn.aijiamuyingfang.client.domain.coupon.response.GetGoodVoucherListResponse;
import cn.aijiamuyingfang.client.domain.coupon.response.GetUserVoucherListResponse;
import cn.aijiamuyingfang.client.domain.coupon.response.GetVoucherItemListResponse;
import cn.aijiamuyingfang.client.rest.api.CouponControllerApi;
import cn.aijiamuyingfang.weapp.manager.access.server.rxjava.RxRetrofitClient;
import cn.aijiamuyingfang.weapp.manager.access.server.utils.RxJavaUtils;
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
    private static final CouponControllerApi instance;

    static {
        instance = RxRetrofitClient.createGApi(CouponControllerApi.class);
    }

    @Override
    public Observable<ResponseBean<GetUserVoucherListResponse>> getUserVoucherList(String username, int currentPage, int pageSize, String accessToken) {
        return instance.getUserVoucherList(username, currentPage, pageSize, accessToken).compose(RxJavaUtils.switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<GetGoodVoucherListResponse>> getGoodVoucherList(int currentPage, int pageSize) {
        return instance.getGoodVoucherList(currentPage, pageSize).compose(RxJavaUtils.switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<GoodVoucher>> getGoodVoucher(String voucherId) {
        return instance.getGoodVoucher(voucherId).compose(RxJavaUtils.switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<GoodVoucher>> createGoodVoucher(GoodVoucher request, String accessToken) {
        return instance.createGoodVoucher(request, accessToken).compose(RxJavaUtils.switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<Void>> deprecateGoodVoucher(String voucherId, String accessToken) {
        return instance.deprecateGoodVoucher(voucherId, accessToken).compose(RxJavaUtils.switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<GetVoucherItemListResponse>> getVoucherItemList(int currentPage, int pageSize) {
        return instance.getVoucherItemList(currentPage, pageSize).compose(RxJavaUtils.switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<VoucherItem>> getVoucherItem(String voucherItemId) {
        return instance.getVoucherItem(voucherItemId).compose(RxJavaUtils.switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<VoucherItem>> createVoucherItem(VoucherItem request, String accessToken) {
        return instance.createVoucherItem(request, accessToken).compose(RxJavaUtils.switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<Void>> deprecateVoucherItem(String voucherItemId, String accessToken) {
        return instance.deprecateVoucherItem(voucherItemId, accessToken).compose(RxJavaUtils.switchSchedulers());
    }

}
