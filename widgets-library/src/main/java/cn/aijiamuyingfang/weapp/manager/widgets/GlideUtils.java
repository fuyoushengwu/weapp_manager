package cn.aijiamuyingfang.weapp.manager.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.request.target.Target;

import java.io.InputStream;
import java.util.concurrent.ExecutionException;

import cn.aijiamuyingfang.weapp.manager.access.server.rxjava.RxRetrofitClient;
import cn.aijiamuyingfang.weapp.manager.commons.CommonApp;
import cn.aijiamuyingfang.weapp.manager.commons.R;


/**
 * Created by pc on 2018/3/31.
 */

public final class GlideUtils {
    private GlideUtils() {
    }

    static {
        Glide glide = Glide.get(CommonApp.getApplication());
        glide.register(GlideUrl.class, InputStream.class,
                new OkHttpUrlLoader.Factory(RxRetrofitClient.getHttpClient()));
    }

    public static void load(Context context, String url, ImageView iv) {
        if (iv instanceof EditableImageView) {
            ((EditableImageView) iv).setImageUrl(url);
        }
        Glide.with(context).load(url).placeholder(R.drawable.company_logo).into(iv);
    }

    public static Bitmap getBitmap(Context context, String url) throws ExecutionException, InterruptedException {
        return Glide.with(context).load(url).asBitmap().into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .get();
    }

}
