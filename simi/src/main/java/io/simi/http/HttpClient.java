package io.simi.http;

import android.support.v4.util.ArrayMap;
import android.text.TextUtils;

import io.simi.listener.OnHttpResponseListener;
import io.simi.norm.HTTP;

/**
 * -------------------------------
 * 		    HttpClient
 * -------------------------------
 *
 * createTime: 2015-04-11
 * updateTime: 2015-04-11
 *
 */
public class HttpClient {

    protected static final int CONNECTION_TIMEOUT = 3 * 1000;
    protected static final int SOCKET_TIMEOUT = 3 * 1000;

    private String cookie;
    private boolean isDebugMode = false;
    private int connectionTime = CONNECTION_TIMEOUT;
    private int socketTime = SOCKET_TIMEOUT;
    private ArrayMap<String, String> mHeaderFields = new ArrayMap<>();

    private static HttpClient instance;

    private HttpClient(){}

    public static HttpClient getInstance() {
        if (instance == null) {
            instance = new HttpClient();
        }
        return  instance;
    }

    public void newTask(HTTP methodType, String url, HttpParams params, OnHttpResponseListener httpResponseListener) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        if (params == null || TextUtils.isEmpty(params.toString())) {
            new HttpTask(this, httpResponseListener).execute(new String[]{methodType.toString(), url, null});
        }else {
            if (methodType.equals(HTTP.GET) || methodType.equals(HTTP.DELETE)) {
                new HttpTask(this, httpResponseListener).execute(new String[]{methodType.toString(), url + "?" + params.toString(), null});
            }else {
                new HttpTask(this, httpResponseListener).execute(new String[]{methodType.toString(), url, params.toString()});
            }
        }
    }

    public boolean isDebugMode() {
        return isDebugMode;
    }

    public void setDebugMode(boolean isDebugMode) {
        this.isDebugMode = isDebugMode;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getCookie() {
        return cookie;
    }

    public int getConnectionTime() {
        return connectionTime;
    }

    public void setConnectionTime(int connectionTime) {
        this.connectionTime = connectionTime;
    }

    public int getSocketTime() {
        return socketTime;
    }

    public void setSocketTime(int socketTime) {
        this.socketTime = socketTime;
    }

    public ArrayMap<String, String> getHeaderFields() {
        return mHeaderFields;
    }
}
