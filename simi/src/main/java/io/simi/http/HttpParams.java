package io.simi.http;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
            try {
                paramsBuffer.append(URLEncoder.encode(entry.getValue(), "utf-8"));
            } catch (UnsupportedEncodingException e) {
                paramsBuffer.append(entry.getValue());
            }
        }
        return paramsBuffer.toString();
    }
}
