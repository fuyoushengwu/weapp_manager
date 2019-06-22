package cn.aijiamuyingfang.weapp.manager.access.server.impl;

import java.util.List;

import cn.aijiamuyingfang.client.commons.domain.ResponseBean;
import cn.aijiamuyingfang.client.domain.classify.Classify;
import cn.aijiamuyingfang.client.rest.api.ClassifyControllerApi;
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
    private static final ClassifyControllerApi instance;

    static {
        instance = RxRetrofitClient.createGApi(ClassifyControllerApi.class);
    }

    @Override
    public Observable<ResponseBean<List<Classify>>> getTopClassifyList() {
        return instance.getTopClassifyList().compose(RxJavaUtils.switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<Classify>> getClassify(String classifyId) {
        return instance.getClassify(classifyId).compose(RxJavaUtils.switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<Void>> deleteClassify(String classifyId, String accessToken) {
        return instance.deleteClassify(classifyId, accessToken).compose(RxJavaUtils.switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<Classify>> createTopClassify(Classify request, String accessToken) {
        return instance.createTopClassify(request, accessToken).compose(RxJavaUtils.switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<List<Classify>>> getSubClassifyList(String classifyId) {
        return instance.getSubClassifyList(classifyId).compose(RxJavaUtils.switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<Classify>> createSubClassify(String classifyId, MultipartBody classifyRequest, String accessToken) {
        return instance.createSubClassify(classifyId, classifyRequest, accessToken).compose(RxJavaUtils.switchSchedulers());
    }

    @Override
    public Observable<ResponseBean<Void>> addClassifyGood(String classifyId, String goodId, String accessToken) {
        return instance.addClassifyGood(classifyId, goodId, accessToken).compose(RxJavaUtils.switchSchedulers());
    }
}
