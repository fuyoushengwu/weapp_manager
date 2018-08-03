package cn.aijiamuyingfang.weapp.manager.commons.utils;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;

import cn.aijiamuyingfang.commons.utils.StringUtils;

public final class JsonUtils {
    private JsonUtils() {
    }

    private static final String TAG = JsonUtils.class.getName();

    // 线程局部变量，每个线程有自己的ObjectMapper变量，空间换取时间。
    private static final ThreadLocal<Gson> threadLocal = new ThreadLocal<Gson>() {
        @Override
        protected Gson initialValue() {
            GsonBuilder builder = new GsonBuilder();
            builder.setDateFormat("yyyy-MM-dd HH:mm:ss");
            return builder.create();
        }
    };

    public static String getJson(Context context, String fileName) {
        StringBuilder sb = new StringBuilder();
        try (InputStream inputStream = context.getAssets().open(fileName)) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            Log.e(TAG, "get json failed", e);
        }
        return sb.toString();
    }

    /**
     * json字符串转化为javaBean对象
     *
     * @param json      json格式字符串
     * @param beanClass 要转换成的JavaBean类型
     * @return
     */
    public static <T> T json2Bean(String json, Class<T> beanClass) {
        if (StringUtils.isEmpty(json) || null == beanClass) {
            return null;
        }
        Gson gson = threadLocal.get();
        return gson.fromJson(json, beanClass);
    }

    /**
     * json字符串转换为javaBean的链表
     *
     * @param json
     * @param beanClass
     * @return
     */
    public static <T> List<T> json2List(String json, Class<T> beanClass) {
        if (StringUtils.isEmpty(json) || null == beanClass) {
            return Collections.emptyList();
        }
        Gson gson = threadLocal.get();
        return gson.fromJson(json, TypeToken.getParameterized(List.class, beanClass).getType());
    }


}