package com.judd.trump.http;


import android.text.TextUtils;

import com.judd.trump.BuildConfig;
import com.judd.trump.app.Config;
import com.judd.trump.app.TApp;
import com.socks.library.KLog;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

import static com.judd.trump.app.Config.PREF_KEY_COOKIE;

/**
 * @author 王元_Trump
 * @desc ${DESC}
 * @time 2016/10/28 13:07
 */
enum OKHttpFactory {

    INSTANCE;

    private OkHttpClient okHttpClient;

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    OKHttpFactory() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        // set cache
//        builder.cache(getCache());

//         set cache interceptor
//        builder.addInterceptor(getCacheInterceptor());

        //request 统一参数管理
//        builder.addInterceptor(getRequestInterceptor());

        //for debug log
        if (BuildConfig.DEBUG)
            builder.interceptors().add(getHttpLoggingInterceptor());

        //cookie
        builder.interceptors().add(new ReadCookiesInterceptor());
        builder.interceptors().add(new SaveCookiesInterceptor());
        //添加公共参数
//        builder.interceptors().add(getRequestInterceptor());

        //cookie
//        CookieManager cookieManager = new CookieManager();
//        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
//        builder.cookieJar(new JavaNetCookieJar(cookieManager));

        //for time out
        builder.readTimeout(Config.HTTP_TIMEOUT_READ, TimeUnit.SECONDS);
        builder.writeTimeout(Config.HTTP_TIMEOUT_WTITE, TimeUnit.SECONDS);
        builder.connectTimeout(Config.HTTP_TIMEOUT_CONNECTION, TimeUnit.SECONDS);

        //need retry
        builder.retryOnConnectionFailure(true);

        okHttpClient = builder.build();
    }

    private Cache getCache() {
        return new Cache(TApp.getInstance().getCacheDir(), 10 * 1024 * 1024);
    }

    private HttpLoggingInterceptor getHttpLoggingInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor =
                new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(String message) {
                        if (TextUtils.isEmpty(message))
                            return;
                        String temp = message.substring(0, 1);
                        if (message.startsWith("POST http://")
                                || message.startsWith("GET http://")
                                || message.startsWith("--> GET http://")
                                || message.startsWith("--> POST http://")) {
                            //请求url
                            KLog.json(message);
                        } else if (message.contains("=")
                                && !message.contains("Set-Cookie")
                                && !message.contains("Cache-Control")
                                && !message.contains("Content-Type")
                                && !message.contains("Keep-Alive")
                                && !message.contains("<--")) {
                            //post时 请求参数
                            KLog.json(message);
                        } else if ("{".equals(temp) || "[".equals(temp)) {
                            //请求结果
                            KLog.json(message);
                        }
                    }
                });
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;
    }

    //向请求中添加cookie
    class ReadCookiesInterceptor implements Interceptor {

        public ReadCookiesInterceptor() {
            super();
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            final Request.Builder builder = chain.request().newBuilder();
            builder.addHeader("Cookie", TApp.getPref().getString(PREF_KEY_COOKIE, ""));

            //from http://blog.csdn.net/blackice1015/article/details/51018815
            //EOFException
//            if (Build.VERSION.SDK != null && Build.VERSION.SDK_INT > 13)
//                builder.addHeader("Connection", "close");
//            }
            return chain.proceed(builder.build());
        }
    }

    class SaveCookiesInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Response originalResponse = chain.proceed(chain.request());
            if (!originalResponse.headers("Set-Cookie").isEmpty()) {
                TApp.getPref().put(Config.PREF_KEY_COOKIE,
                        originalResponse.headers().get("Set-Cookie").split(";")[0]);
            }
            return originalResponse;
        }
    }
}