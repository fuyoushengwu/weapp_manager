package cn.aijiamuyingfang.weapp.manager.access.server.impl;

import java.util.List;

import cn.aijiamuyingfang.client.rest.api.ClassifyControllerApi;
import cn.aijiamuyingfang.commons.domain.goods.Classify;
import cn.aijiamuyingfang.commons.domain.response.ResponseBean;
import cn.aijiamuyingfang.weapp.manager.access.server.rxjava.RxRetrofitClient;
import cn.aijiamuyingfang.weapp.manager.access.server.utils.RxJavaUtils;
import io.reactivex.Observable;
import okhttp3.MultipartBody;

/**
 * [描述]:
 * <p>
 * 客户端调用ClassifyController的服务
 * </p>
 *
 * @author ShiWei
 * @version 1.0.0
 */
public class ClassifyControllerClient implements ClassifyControllerApi {
    private static ClassifyControllerApi instance;

    static {
        instance = RxRetrofitClient.createGApi(ClassifyControllerApi.class);
    }

    @Override
    public Observable<ResponseBean<List<Classify>>> getTopClassifyList(String token) {
        return instance.getTopClassifyList(token).compose(RxJavaUtils.switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<Classify>> getClassify(String token, String classifyid) {
        return instance.getClassify(token, classifyid).compose(RxJavaUtils.switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<Void>> deleteClassify(String token, String classifyid) {
        return instance.deleteClassify(token, classifyid).compose(RxJavaUtils.switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<Classify>> createTopClassify(String token, Classify request) {
        return instance.createTopClassify(token, request).compose(RxJavaUtils.switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<List<Classify>>> getSubClassifyList(String token, String classifyid) {
        return instance.getSubClassifyList(token, classifyid).compose(RxJavaUtils.switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<Classify>> createSubClassify(String token, String classifyid, MultipartBody classifyRequest) {
        return instance.createSubClassify(token, classifyid, classifyRequest).compose(RxJavaUtils.switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<Void>> addClassifyGood(String token, String classifyid, String goodid) {
        return instance.addClassifyGood(token, classifyid, goodid).compose(RxJavaUtils.switchSchedulers());
    }
}
