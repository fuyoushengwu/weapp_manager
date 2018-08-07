package cn.aijiamuyingfang.weapp.manager.access.server.impl;

import cn.aijiamuyingfang.client.rest.api.WXSessionControllerApi;
import cn.aijiamuyingfang.commons.domain.response.ResponseBean;
import cn.aijiamuyingfang.commons.domain.wxservice.WXSession;
import cn.aijiamuyingfang.weapp.manager.access.server.rxjava.RxRetrofitClient;
import cn.aijiamuyingfang.weapp.manager.access.server.utils.RxJavaUtils;
import io.reactivex.Observable;

/**
 * [描述]:
 * <p>
 * 客户端调用WXSessionController的服务
 * </p>
 *
 * @author ShiWei
 * @version 1.0.0
 */
public class WXSessionControllerClient implements WXSessionControllerApi {
    private static WXSessionControllerApi instance;

    static {
        instance = RxRetrofitClient.createGApi(WXSessionControllerApi.class);
    }

    @Override
    public Observable<ResponseBean<WXSession>> jscode2Session(String jscode) {
        return instance.jscode2Session(jscode).compose(RxJavaUtils.switchSchedulers());
    }
}
