package cn.aijiamuyingfang.weapp.manager.access.server.impl;

import java.util.List;

import cn.aijiamuyingfang.client.commons.domain.ResponseBean;
import cn.aijiamuyingfang.client.domain.previeworder.PreviewOrder;
import cn.aijiamuyingfang.client.domain.previeworder.PreviewOrderItem;
import cn.aijiamuyingfang.client.rest.api.PreviewOrderControllerApi;
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
    private static final PreviewOrderControllerApi instance;

    static {
        instance = RxRetrofitClient.createGApi(PreviewOrderControllerApi.class);
    }

    @Override
    public Observable<ResponseBean<PreviewOrderItem>> updatePreviewOrderItem(String username, String previewItemId, PreviewOrderItem request, String accessToken) {
        return instance.updatePreviewOrderItem(username, previewItemId, request, accessToken).compose(RxJavaUtils.switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<Void>> deletePreviewOrderItem(String username, String previewItemId, String accessToken) {
        return instance.deletePreviewOrderItem(username, previewItemId, accessToken).compose(RxJavaUtils.switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<PreviewOrder>> generatePreviewOrder(String username, List<String> goodIdList, String accessToken) {
        return instance.generatePreviewOrder(username, goodIdList, accessToken).compose(RxJavaUtils.switchSchedulers());
    }
}
