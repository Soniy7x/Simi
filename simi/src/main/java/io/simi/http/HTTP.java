package io.simi.http;

/**
 * -------------------------------
 * 		    HTTP
 * -------------------------------
 *
 * createTime: 2015-04-11
 * updateTime: 2015-04-11
 *
 */
public enum HTTP {

    GET("GET"),
    PUT("PUT"),
    POST("POST"),
    DELETE("DELETE");

    private String method;

    private HTTP(String method) {
        this.method = method;
    }

}
