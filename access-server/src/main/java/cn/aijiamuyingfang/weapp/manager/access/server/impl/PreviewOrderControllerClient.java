package cn.aijiamuyingfang.weapp.manager.access.server.impl;

import java.util.List;

import cn.aijiamuyingfang.client.rest.api.PreviewOrderControllerApi;
import cn.aijiamuyingfang.commons.domain.response.ResponseBean;
import cn.aijiamuyingfang.commons.domain.shoporder.PreviewOrder;
import cn.aijiamuyingfang.commons.domain.shoporder.PreviewOrderItem;
import cn.aijiamuyingfang.weapp.manager.access.server.rxjava.RxRetrofitClient;
import cn.aijiamuyingfang.weapp.manager.access.server.utils.RxJavaUtils;
import io.reactivex.Observable;

/**
 * [描述]:
 * <p>
 * 客户端调用 PreviewOrderController的服务
 * </p>
 *
 * @author ShiWei
 * @version 1.0.0
 */
public class PreviewOrderControllerClient implements PreviewOrderControllerApi {
    private static PreviewOrderControllerApi instance;

    static {
        instance = RxRetrofitClient.createGApi(PreviewOrderControllerApi.class);
    }

    @Override
    public Observable<ResponseBean<PreviewOrderItem>> updatePreviewOrderItem(String token, String userid, String itemid,
                                                                             PreviewOrderItem request) {
        return instance.updatePreviewOrderItem(token, userid, itemid, request)
                .compose(RxJavaUtils.switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<Void>> deletePreviewOrderItem(String token, String userid, String itemid) {
        return instance.deletePreviewOrderItem(token, userid, itemid)
                .compose(RxJavaUtils.switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<PreviewOrder>> generatePreviewOrder(String token, String userid,
                                                                       List<String> goodids) {
        return instance.generatePreviewOrder(token, userid, goodids)
                .compose(RxJavaUtils.switchSchedulers());
    }
}
