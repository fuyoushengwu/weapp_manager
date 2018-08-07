package cn.aijiamuyingfang.weapp.manager.access.server.impl;

import cn.aijiamuyingfang.client.rest.api.UserMessageControllerApi;
import cn.aijiamuyingfang.commons.domain.response.ResponseBean;
import cn.aijiamuyingfang.commons.domain.user.UserMessage;
import cn.aijiamuyingfang.commons.domain.user.response.GetMessagesListResponse;
import cn.aijiamuyingfang.weapp.manager.access.server.rxjava.RxRetrofitClient;
import cn.aijiamuyingfang.weapp.manager.access.server.utils.RxJavaUtils;
import io.reactivex.Observable;

/**
 * [描述]:
 * <p>
 * 客户端调用UserMessageController的服务
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 */
public class UserMessageControllerClient implements UserMessageControllerApi {
    private static UserMessageControllerApi instance;

    static {
        instance = RxRetrofitClient.createGApi(UserMessageControllerApi.class);
    }

    @Override
    public Observable<ResponseBean<Integer>> getUserUnReadMessageCount(String token, String userid) {
        return instance.getUserUnReadMessageCount(token, userid)
                .compose(RxJavaUtils.switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<GetMessagesListResponse>> getUserMessageList(String token, String userid,
            int currentpage, int pagesize) {
        return instance.getUserMessageList(token, userid, currentpage, pagesize)
                .compose(RxJavaUtils.switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<UserMessage>> createMessage(String token, String userid, UserMessage message) {
        return instance.createMessage(token, userid, message)
                .compose(RxJavaUtils.switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<Void>> deleteMessage(String token, String userid, String messageid) {
        return instance.deleteMessage(token, userid, messageid)
                .compose(RxJavaUtils.switchSchedulers());
    }
}
