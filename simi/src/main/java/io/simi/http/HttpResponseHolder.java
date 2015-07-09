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
    private Exception exception;

    public HttpResponseHolder(String response) {
        this.response = response;
    }

    public HttpResponseHolder(int responseCode, Exception exception) {
        this.responseCode = responseCode;
        this.exception = exception;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public String getResponse() {
        return response;
    }

    public Exception getException() {
        return exception;
    }
}
