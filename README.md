# Simi

* [How to use](#howtouse)
* [Components](#components)
    * [HTTP](#http)
## How to use

If you want use this library, you only have to download Simi project, import it into your workspace and add the project as a library in your android project settings.

If you prefer it, you can use the gradle dependency, you have to add these lines in your build.gradle file:

```xml
repositories {
    jcenter()
}

dependencies {
    compile 'io.simi:simi:1.0.0'
}
```

##Components

####HTTP
```xml
//生成异步网络访问参数实例
HttpParams params = new HttpParams();
//添加参数，支持基础类型与String、List等
params.put("name", "Selly");
//生成异步网络客户端实例
HttpClient client = new HttpClient();
//开启调试模式
client.setDebugMode(true);
//发起POST类型访问
cliend.newTask(HTTP.POST, "http://xxx.com/register", params, new OnHttpResponseListener() {
    @Override
    public void onSuccess(String response) {}

    @Override
    public void onFailure(Exception exception) {}

    @Override
    public void onStart() {}

    @Override
    public void onFinish() {}
});
```
