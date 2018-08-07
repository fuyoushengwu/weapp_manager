package cn.aijiamuyingfang.weapp.manager.access.server.impl;

import cn.aijiamuyingfang.client.rest.api.TemplateMsgControllerApi;
import cn.aijiamuyingfang.commons.domain.response.ResponseBean;
import cn.aijiamuyingfang.commons.domain.wxservice.TemplateMsg;
import cn.aijiamuyingfang.weapp.manager.access.server.rxjava.RxRetrofitClient;
import cn.aijiamuyingfang.weapp.manager.access.server.utils.RxJavaUtils;
import io.reactivex.Observable;

/**
 * [描述]:
 * <p>
 * 客户端调用TemplateMsgController的服务
 * </p>
 *
 * @author ShiWei
 * @version 1.0.0
 */
public class TemplateMsgControllerClient implements TemplateMsgControllerApi {
    private static TemplateMsgControllerApi instance;

    static {
        instance = RxRetrofitClient.createGApi(TemplateMsgControllerApi.class);
    }

    @Override
    public Observable<ResponseBean<Void>> sendPreOrderMsg(String token, String openid, TemplateMsg msgData) {
        return instance.sendPreOrderMsg(token, openid, msgData)
                .compose(RxJavaUtils.switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<Void>> sendPickupMsg(String token, String openid, TemplateMsg msgData) {
        return instance.sendPickupMsg(token, openid, msgData)
                .compose(RxJavaUtils.switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<Void>> sendThirdSendMsg(String token, String openid, TemplateMsg msgData) {
        return instance.sendThirdSendMsg(token, openid, msgData)
                .compose(RxJavaUtils.switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<Void>> sendOwnSendMsg(String token, String openid, TemplateMsg msgData) {
        return instance.sendOwnSendMsg(token, openid, msgData)
                .compose(RxJavaUtils.switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<Void>> sendOrderOverTimeMsg(String token, String openid, TemplateMsg msgData) {
        return instance.sendOrderOverTimeMsg(token, openid, msgData)
                .compose(RxJavaUtils.switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<Void>> sendOrderConfirmMsg(String token, String openid, TemplateMsg msgData) {
        return instance.sendOrderConfirmMsg(token, openid, msgData)
                .compose(RxJavaUtils.switchSchedulers());
    }
}
