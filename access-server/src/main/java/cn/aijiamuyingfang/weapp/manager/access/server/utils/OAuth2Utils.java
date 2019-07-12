package cn.aijiamuyingfang.weapp.manager.access.server.utils;

import android.util.Log;

import java.io.IOException;

import cn.aijiamuyingfang.client.oauth2.Constants;
import cn.aijiamuyingfang.client.oauth2.OAuth2Client;
import cn.aijiamuyingfang.client.oauth2.OAuthResponse;
import cn.aijiamuyingfang.vo.exception.OAuthException;
import cn.aijiamuyingfang.vo.response.ResponseCode;
import cn.aijiamuyingfang.weapp.manager.access.server.rxjava.RxRetrofitClient;

import static cn.aijiamuyingfang.client.commons.constant.ClientRestConstants.DEFAULT_BASE_URL;


/**
 * OAuth2工具类
 */
public final class OAuth2Utils {
    private static final String TAG = OAuth2Utils.class.getName();
    private static final OAuth2Client oauth2Client;
    private static OAuthResponse oAuthResponse;

    static {
        OAuth2Client.Builder builder = new OAuth2Client.Builder("weapp-manager", "weapp-manager", DEFAULT_BASE_URL +"/oauth/token")
                .scope("read,write")
                .okHttpClient(RxRetrofitClient.getHttpClient());
        oauth2Client = builder.build();
    }

    private OAuth2Utils() {
    }

    /**
     * 获取AccessToken
     *
     * @param userName
     * @param password
     * @return
     * @throws IOException
     */
    public static OAuthResponse getAccessToken(String userName, String password) throws IOException {
        if (null == oAuthResponse) {
            oauth2Client.setUsername(userName);
            oauth2Client.setPassword(password);
            oauth2Client.setGrantType(Constants.GRANT_TYPE_PASSWORD);
            oAuthResponse = oauth2Client.requestAccessToken();
            if (!oAuthResponse.isSuccessful()) {
                Log.e(TAG, "get access token failed", oAuthResponse.getOAuthError().getCause());
                throw new OAuthException(ResponseCode.BAD_REQUEST, oAuthResponse.getOAuthError().getDescirption());
            }
        }
        return oAuthResponse;
    }

    /**
     * 更新AccessToken
     *
     * @return
     * @throws IOException
     */
    public static OAuthResponse refreshToken() throws IOException {
        if (null == oAuthResponse) {
            throw new IllegalStateException("should get access token before refresh");
        }
        oauth2Client.setGrantType(Constants.GRANT_TYPE_REFRESH);
        oAuthResponse = oauth2Client.refreshAccessToken(oAuthResponse.getRefreshToken());
        if (!oAuthResponse.isSuccessful()) {
            Log.e(TAG, "refresh access token failed", oAuthResponse.getOAuthError().getCause());
            throw new OAuthException(ResponseCode.BAD_REQUEST, oAuthResponse.getOAuthError().getDescirption());
        }
        return oAuthResponse;
    }
}
