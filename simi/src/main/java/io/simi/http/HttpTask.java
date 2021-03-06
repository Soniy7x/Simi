package io.simi.http;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.simi.listener.OnHttpResponseListener;
import io.simi.norm.HTTP;

/**
 * -------------------------------
 * 	        HttpTask
 * -------------------------------
 *
 * createTime: 2015-04-11
 * updateTime: 2015-04-11
 *
 */
public class HttpTask extends AsyncTask<String, Void, HttpResponseHolder>{

    private static final String TAG = "Simi - HttpTask";

    private HttpClient httpClient;
    private OnHttpResponseListener httpResponseListener;

    public HttpTask(HttpClient httpClient, OnHttpResponseListener httpResponseListener) {
        this.httpClient = httpClient;
        this.httpResponseListener = httpResponseListener;
    }

    @Override
    protected HttpResponseHolder doInBackground(String... params) {
        int responseCode = 0;
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(params[1]).openConnection();
            connection.setRequestProperty("Accept-Charset", "UTF-8");
            connection.setInstanceFollowRedirects(false);
            connection.setReadTimeout(httpClient.getSocketTime());
            connection.setConnectTimeout(httpClient.getConnectionTime());
            connection.setRequestMethod(params[0]);
            if (httpClient.getHeaderFields().size() > 0) {
                for (Map.Entry<String, String> entry : httpClient.getHeaderFields().entrySet()) {
                    connection.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }
            if (!TextUtils.isEmpty(httpClient.getCookie())) {
                connection.setRequestProperty("cookie", httpClient.getCookie());
            }
            if (!TextUtils.isEmpty(params[2]) && (params[0].equals(HTTP.POST.toString()) || params[0].equals(HTTP.PUT.toString()))) {
                connection.setDoOutput(true);
                PrintWriter writer = new PrintWriter(connection.getOutputStream());
                writer.print(params[2]);
                writer.flush();
                writer.close();
            }
            responseCode = connection.getResponseCode();
            StringBuilder response = new StringBuilder();
            if (responseCode > 300) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getErrorStream(), "utf-8"));
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line).append("\n");
                }
                reader.close();
            } else {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line).append("\n");
                }
                reader.close();
                Map<String, List<String>> map = connection.getHeaderFields();
                if (map != null && map.containsKey("Set-Cookie")) {
                    String cookie = map.get("Set-Cookie").get(0);
                    if (!TextUtils.isEmpty(cookie)) {
                        httpClient.setCookie(cookie.substring(0, cookie.indexOf(";")));
                    }
                }
            }
            if (httpClient.isDebugMode()) {
                Log.v(TAG, "---------------------------------------------------------------------");
                Log.v(TAG, "请求时间：" + new SimpleDateFormat("HH时mm分ss秒", Locale.CHINESE).format(new Date()) + "发起异步网络访问");
                Log.v(TAG, "接口地址：" + params[1]);
                Log.v(TAG, "访问方式：" + params[0]);
                Log.v(TAG, "传递参数：" + (TextUtils.isEmpty(params[2]) ? "" : params[2]));
                Log.v(TAG, "响应标识：" + responseCode);
                Log.v(TAG, "返回内容：" + response.toString());
                Log.v(TAG, "---------------------------------------------------------------------");
            }
            if (responseCode > 300) {
                return new HttpResponseHolder(responseCode, response.toString());
            } else {
                return new HttpResponseHolder(response.toString());
            }
        } catch (Exception e) {
            if (httpClient.isDebugMode()) {
                Log.v(TAG, "---------------------------------------------------------------------");
                Log.v(TAG, "请求时间：" + new SimpleDateFormat("HH时mm分ss秒", Locale.CHINESE).format(new Date()) + "发起异步网络访问");
                Log.v(TAG, "接口地址：" + params[1]);
                Log.v(TAG, "访问方式：" + params[0]);
                Log.v(TAG, "传递参数：" + (TextUtils.isEmpty(params[2]) ? "" : params[2]));
                Log.v(TAG, "响应标识：" + responseCode);
                Log.v(TAG, "异常信息：" + e.toString());
                Log.v(TAG, "---------------------------------------------------------------------");
            }
            return new HttpResponseHolder(505, e.toString());
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        httpResponseListener.onStart();
    }

    @Override
    protected void onPostExecute(HttpResponseHolder httpResponseHolder) {
        super.onPostExecute(httpResponseHolder);
        if (httpResponseHolder.getResponseCode() != 0) {
            if (httpResponseHolder.getResponseCode() == 505) {
                httpResponseListener.onError(new Exception(httpResponseHolder.getResponse()));
            } else {
                httpResponseListener.onFailure(httpResponseHolder.getResponseCode(), httpResponseHolder.getResponse());
            }
        }else {
            httpResponseListener.onSuccess(httpResponseHolder.getResponse());
        }
        httpResponseListener.onFinish();
    }
}
