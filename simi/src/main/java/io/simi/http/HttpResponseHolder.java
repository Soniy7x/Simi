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
    private Exception exception;

    public HttpResponseHolder(String response) {
        this.response = response;
    }

    public HttpResponseHolder(Exception exception) {
        this.exception = exception;
    }

    public String getResponse() {
        return response;
    }

    public Exception getException() {
        return exception;
    }
}
