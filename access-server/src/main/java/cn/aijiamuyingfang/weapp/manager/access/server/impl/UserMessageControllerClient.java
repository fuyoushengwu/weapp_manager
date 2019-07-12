package cn.aijiamuyingfang.weapp.manager.access.server.impl;

import cn.aijiamuyingfang.vo.message.PagableUserMessageList;
import cn.aijiamuyingfang.vo.message.UserMessage;
import cn.aijiamuyingfang.vo.response.ResponseBean;
import cn.aijiamuyingfang.client.rest.api.UserMessageControllerApi;
import cn.aijiamuyingfang.weapp.manager.access.server.rxjava.RxRetrofitClient;
import cn.aijiamuyingfang.weapp.manager.access.server.utils.RxJavaUtils;
import io.reactivex.Observable;

/**
 * [描述]:
 * <p>
 * 客户端调用UserMessageController的服务
 * </p>
 *
 * @author ShiWei
 * @version 1.0.0
 */
public class UserMessageControllerClient implements UserMessageControllerApi {
    private static final UserMessageControllerApi instance;

    static {
        instance = RxRetrofitClient.createGApi(UserMessageControllerApi.class);
    }

    @Override
    public Observable<ResponseBean<Integer>> getUserUnReadMessageCount(String username, String accessToken) {
        return instance.getUserUnReadMessageCount(username, accessToken).compose(RxJavaUtils.switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<PagableUserMessageList>> getUserMessageList(String username, int currentPage, int pageSize, String accessToken) {
        return instance.getUserMessageList(username, currentPage, pageSize, accessToken).compose(RxJavaUtils.switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<UserMessage>> createMessage(String username, UserMessage message, String accessToken) {
        return instance.createMessage(username, message, accessToken).compose(RxJavaUtils.switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<Void>> deleteMessage(String username, String messageId, String accessToken) {
        return instance.deleteMessage(username, messageId, accessToken).compose(RxJavaUtils.switchSchedulers());
    }
}
