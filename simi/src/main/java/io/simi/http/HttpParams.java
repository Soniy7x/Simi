package io.simi.http;

import java.util.concurrent.ConcurrentHashMap;

/**
 * -------------------------------
 * 		    HttpParams
 * -------------------------------
 *
 * createTime: 2015-04-11
 * updateTime: 2015-04-11
 *
 */
public class HttpParams {

    private ConcurrentHashMap<String, String> params = new ConcurrentHashMap<>();

    public void put(String key, Object value) {
        params.put(key, String.valueOf(value));
    }

    public String toString() {
        StringBuffer paramsBuffer = new StringBuffer();
        for (ConcurrentHashMap.Entry<String, String> entry : params.entrySet()) {
            if (paramsBuffer.length() > 0) {
                paramsBuffer.append("&");
            }
            paramsBuffer.append(entry.getKey());
            paramsBuffer.append("=");
            paramsBuffer.append(entry.getValue());
        }
        return paramsBuffer.toString();
    }
}
