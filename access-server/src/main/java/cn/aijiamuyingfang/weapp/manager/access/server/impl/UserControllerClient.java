package cn.aijiamuyingfang.weapp.manager.access.server.impl;

import java.util.List;

import cn.aijiamuyingfang.client.rest.api.UserControllerApi;
import cn.aijiamuyingfang.commons.domain.address.RecieveAddress;
import cn.aijiamuyingfang.commons.domain.response.ResponseBean;
import cn.aijiamuyingfang.commons.domain.user.User;
import cn.aijiamuyingfang.weapp.manager.access.server.rxjava.RxRetrofitClient;
import cn.aijiamuyingfang.weapp.manager.access.server.utils.RxTransformerUtils;
import io.reactivex.Observable;

/**
 * [描述]:
 * <p>
 * 客户端调用 UserController的服务
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 */
public class UserControllerClient implements UserControllerApi {
    private static UserControllerApi instance;

    static {
        instance = RxRetrofitClient.createGApi(UserControllerApi.class);
    }

    @Override
    public Observable<ResponseBean<User>> getUser(String token, String userid) {
        return instance.getUser(token, userid).compose(RxTransformerUtils.<ResponseBean<User>>switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<User>> updateUser(String token, String userid, User user) {
        return instance.updateUser(token, userid, user).compose(RxTransformerUtils.<ResponseBean<User>>switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<List<RecieveAddress>>> getUserRecieveAddressList(String token, String userid) {
        return instance.getUserRecieveAddressList(token, userid)
                .compose(RxTransformerUtils.<ResponseBean<List<RecieveAddress>>>switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<RecieveAddress>> addUserRecieveAddress(String token, String userid,
            RecieveAddress request) {
        return instance.addUserRecieveAddress(token, userid, request)
                .compose(RxTransformerUtils.<ResponseBean<RecieveAddress>>switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<RecieveAddress>> getRecieveAddress(String token, String userid, String addressid) {
        return instance.getRecieveAddress(token, userid, addressid)
                .compose(RxTransformerUtils.<ResponseBean<RecieveAddress>>switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<RecieveAddress>> updateRecieveAddress(String token, String userid, String addressid,
            RecieveAddress request) {
        return instance.updateRecieveAddress(token, userid, addressid, request)
                .compose(RxTransformerUtils.<ResponseBean<RecieveAddress>>switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<Void>> deprecateRecieveAddress(String token, String userid, String addressid) {
        return instance.deprecateRecieveAddress(token, userid, addressid)
                .compose(RxTransformerUtils.<ResponseBean<Void>>switchSchedulers());
    }
}
