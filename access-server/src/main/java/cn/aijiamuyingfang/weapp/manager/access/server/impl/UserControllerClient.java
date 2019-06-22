package cn.aijiamuyingfang.weapp.manager.access.server.impl;

import java.util.List;

import cn.aijiamuyingfang.client.commons.domain.ResponseBean;
import cn.aijiamuyingfang.client.domain.user.RecieveAddress;
import cn.aijiamuyingfang.client.domain.user.User;
import cn.aijiamuyingfang.client.domain.user.response.GetUserPhoneResponse;
import cn.aijiamuyingfang.client.rest.api.UserControllerApi;
import cn.aijiamuyingfang.weapp.manager.access.server.rxjava.RxRetrofitClient;
import cn.aijiamuyingfang.weapp.manager.access.server.utils.RxJavaUtils;
import io.reactivex.Observable;

/**
 * [描述]:
 * <p>
 * 客户端调用 UserController的服务
 * </p>
 *
 * @author ShiWei
 * @version 1.0.0
 */
public class UserControllerClient implements UserControllerApi {
    private static final UserControllerApi instance;

    static {
        instance = RxRetrofitClient.createGApi(UserControllerApi.class);
    }

    @Override
    public Observable<ResponseBean<User>> getUser(String username, String accessToken) {
        return instance.getUser(username, accessToken).compose(RxJavaUtils.switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<User>> registerUser(User user, String accessToken) {
        return instance.registerUser(user, accessToken).compose(RxJavaUtils.switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<GetUserPhoneResponse>> getUserPhone(String username, String accessToken) {
        return instance.getUserPhone(username, accessToken).compose(RxJavaUtils.switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<User>> updateUser(String username, User user, String accessToken) {
        return instance.updateUser(username, user, accessToken).compose(RxJavaUtils.switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<List<RecieveAddress>>> getUserRecieveAddressList(String username, String accessToken) {
        return instance.getUserRecieveAddressList(username, accessToken).compose(RxJavaUtils.switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<RecieveAddress>> addUserRecieveAddress(String username, RecieveAddress request, String accessToken) {
        return instance.addUserRecieveAddress(username, request, accessToken).compose(RxJavaUtils.switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<RecieveAddress>> getRecieveAddress(String username, String addressId, String accessToken) {
        return instance.getRecieveAddress(username, addressId, accessToken).compose(RxJavaUtils.switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<RecieveAddress>> updateRecieveAddress(String username, String addressId, RecieveAddress request, String accessToken) {
        return instance.updateRecieveAddress(username, addressId, request, accessToken).compose(RxJavaUtils.switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<Void>> deprecateRecieveAddress(String username, String addressId, String accessToken) {
        return instance.deprecateRecieveAddress(username, addressId, accessToken).compose(RxJavaUtils.switchSchedulers());
    }
}
