package cn.aijiamuyingfang.weapp.manager.access.server.impl;

import cn.aijiamuyingfang.client.rest.api.AuthControllerApi;
import cn.aijiamuyingfang.commons.domain.response.ResponseBean;
import cn.aijiamuyingfang.commons.domain.user.Gender;
import cn.aijiamuyingfang.commons.domain.user.User;
import cn.aijiamuyingfang.commons.domain.user.response.TokenResponse;
import cn.aijiamuyingfang.weapp.manager.access.server.rxjava.RxRetrofitClient;
import cn.aijiamuyingfang.weapp.manager.access.server.utils.RxTransformerUtils;
import io.reactivex.Observable;

/**
 * [描述]:
 * <p>
 * 客户端调用AuthController的服务
 * </p>
 *
 * @author ShiWei
 * @version 1.0.0
 */
public class AuthControllerClient implements AuthControllerApi {
    private static AuthControllerApi instance;

    static {
        instance = RxRetrofitClient.createGApi(AuthControllerApi.class);
    }

    @Override
    public Observable<ResponseBean<TokenResponse>> getToken(String jsCode, String nickname, String avatar, Gender gender) {
        return instance.getToken(jsCode, nickname, avatar, gender).compose(RxTransformerUtils.<ResponseBean<TokenResponse>>switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<User>> registerUser(String token, User user) {
        return instance.registerUser(token, user).compose(RxTransformerUtils.<ResponseBean<User>>switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<TokenResponse>> refreshToken(String token) {
        return instance.refreshToken(token).compose(RxTransformerUtils.<ResponseBean<TokenResponse>>switchSchedulers());
    }
}
