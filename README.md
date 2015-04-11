# Simi

* [快速开始](#快速开始)
* [API调用样例](#API调用样例)
    * [网络访问](#网络访问)


## 快速开始

使用Android Studio并在build.gradle中添加：

```xml
repositories {
    jcenter()
}

dependencies {
    compile 'io.simi:simi:1.0.0'
}
```

##API调用样例

####网络访问
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
