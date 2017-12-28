package com.judd.trump.http;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * @author 王元_Trump
 * @desc Retrofit自定义callback
 * @time 2016/12/12 18:17
 */
public abstract class MyCallback<T> implements Callback<BaseMode<T>> {

    @Override
    public void onResponse(Call<BaseMode<T>> call, Response<BaseMode<T>> response) {
        BaseMode baseMode = response.body();
        if (baseMode == null) {
            onFailure(StatusCode.ERROR_UNKOWN.getCode(), StatusCode.ERROR_NULL_RESPONSE.getMessage(), null);
            return;
        }
        if (200 == baseMode.getCode()) {
            onSuccess(response.body().getData(), baseMode.getMessage(), baseMode.getCode());
        } else {
            onFailure(baseMode.getCode(), baseMode.getMessage(), response.body().getData());
        }
    }

    @Override
    public void onFailure(Call<BaseMode<T>> call, Throwable t) {
        if (t instanceof SocketTimeoutException) {
            onFailure(StatusCode.TIME_OUT.getCode(), StatusCode.TIME_OUT.getMessage(), null);
        } else if (t instanceof ConnectException) {
            onFailure(StatusCode.ERROR_CONNECT.getCode(), StatusCode.ERROR_CONNECT.getMessage(), null);
        } else if (t instanceof UnknownHostException) {
            onFailure(StatusCode.UNKNOWN_HOST.getCode(), StatusCode.UNKNOWN_HOST.getMessage(), null);
        } else if (t instanceof UnknownHostException) {
            onFailure(StatusCode.ERROR_NETWORK.getCode(), StatusCode.ERROR_NETWORK.getMessage(), null);
        } else if (t instanceof IllegalStateException) {
            onFailure(StatusCode.ERROR_DATA_PARSE.getCode(), StatusCode.ERROR_DATA_PARSE.getMessage(), null);
        } else {
            onFailure(StatusCode.ERROR_UNKOWN.getCode(), StatusCode.ERROR_UNKOWN.getMessage(), null);
        }
    }

    public abstract void onSuccess(T model, String message, int code);

    public abstract void onFailure(int responseStatus, String errMsg, T model);
}
