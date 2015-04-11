package io.simi.http;

/**
 * -------------------------------
 * 	   OnHttpResponseListener
 * -------------------------------
 *
 * createTime: 2015-04-11
 * updateTime: 2015-04-11
 *
 */
public interface OnHttpResponseListener {

    public void onSuccess(String response);
    public void onFailure(Exception exception);
    public void onStart();
    public void onFinish();

}
