package io.simi.http;

/**
 * -------------------------------
 * 		  HttpResponseHolder
 * -------------------------------
 *
 * createTime: 2015-04-11
 * updateTime: 2015-04-11
 *
 */
public class HttpResponseHolder {

    private String response;
    private int responseCode;

    public HttpResponseHolder(String response) {
        this.response = response;
    }

    public HttpResponseHolder(int responseCode, String response) {
        this.responseCode = responseCode;
        this.response = response;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public String getResponse() {
        return response;
    }

}
