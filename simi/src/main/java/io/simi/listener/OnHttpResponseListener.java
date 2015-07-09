package io.simi.listener;

/**
 * -------------------------------
 * 	   OnHttpResponseListener
 * -------------------------------
 *
 * createTime: 2015-04-11
 * updateTime: 2015-04-12
 *
 */
public interface OnHttpResponseListener {

    public void onSuccess(String response);
    public void onFailure(int responseCode, Exception exception);
    public void onError(Exception exception);
    public void onStart();
    public void onFinish();

}
