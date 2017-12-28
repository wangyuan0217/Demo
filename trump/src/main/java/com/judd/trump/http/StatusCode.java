package com.judd.trump.http;


/**
 * @author 王元_Trump
 * @time 2017/12/6 11:36
 * @desc http 自定义code
 */
public enum StatusCode {

    TIME_OUT(10001, "响应超时"),
    ERROR_CONNECT(10002, "连接异常"),
    UNKNOWN_HOST(10003, "请求超时"),
    ERROR_NETWORK(10004, "网络错误"),
    ERROR_DATA_PARSE(10005, "数据解析错误"),
    ERROR_NULL_RESPONSE(10006, "null response"),
    ERROR_UNKOWN(10099, "未知错误");

    private int code;
    private String message;

    StatusCode() {
    }

    StatusCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
