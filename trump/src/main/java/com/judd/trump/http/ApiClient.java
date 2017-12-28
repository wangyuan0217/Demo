package com.judd.trump.http;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.judd.trump.app.Config.BASE_URL;

/**
 * @author 王元_Trump
 * @desc ${DESC}
 * @time 2016/12/12 18:08
 */
public class ApiClient {

    public static Retrofit retrofit = null;

    public static Retrofit getRetrofit() {
        if (retrofit == null)
            retrofit = new Retrofit.Builder()
                    //baseUrl
                    .baseUrl(BASE_URL)

                    //设置OKHttpClient
                    .client(OKHttpFactory.INSTANCE.getOkHttpClient())

                    //gson转化器
                    .addConverterFactory(GsonConverterFactory.create())

                    .build();
        return retrofit;
    }

}
