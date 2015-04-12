# Simi

* [快速开始](#快速开始)
* [API](#API)
    * [网络访问](#网络访问)
       * [异步请求 + v1.0.0](#异步请求)
    * [图片相关](#图片相关)
       * [基础转换 + v1.0.1](#基础转换)
       * [缓存下载 + v1.0.1](#缓存下载)
    * [工具集合](#工具集合)
       * [字符处理 + v1.0.1](#字符处理)

## 快速开始

使用Android Studio并在build.gradle中添加：

```xml
repositories {
    jcenter()
}

dependencies {
    compile 'io.simi:simi:1.0.1'
}
```

##API

####网络访问

######异步请求
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

####图片相关

######基础转换
```xml
//创建图片实例
Image image;
image = new Image(getResources(), R.drawable.xxx);
image = new Image(drawable);
image = new Image(bitmap);
image = new Image(byte[]);
image = new Image(fileName)；

//输出Bitmap
image.toBitmap();

//输出Drawable
image.toDrawable();

//输出Byte[]
image.toJPGByte();
image.toPNGByte();

//图片加圆角
image.transformRadius(4);

//图片裁剪圆形
image.transformRound();

//生成静态图片背景
Image.createDrawable(0xFFFFFFFF, 255, 4);
```

######缓存下载
```xml
//创建单一实例
ImageLoader mLoader = ImageLoader.getInstance(this);

//下载网络图片并缓存
mLoader.loadBitmapByHttp(view, url, new ImageLoader.OnImageLoadListener() {
   @Override
   public void onImageLoadCompleted(View view, Bitmap bitmap, String url) {
      //to do something...
      //for example:
      //    ImageView imageView = (ImageView)view;
      //    imageView.setImageBitmap(bitmap);
   }
});

//下载本地图片并缓存
mLoader.loadBitmapByFile(view, fileName, new ImageLoader.OnImageLoadListener() {
   @Override
   public void onImageLoadCompleted(View view, Bitmap bitmap, String fileName) {
      //to do something...
   }
});

//手动添加图片到缓存(同步)
//key可以任意不为空的字符串
//xxx支持类型有Drawable, Bitmap, byte[], Image
mLoader.putBitmap(key, xxx, true);

//手动添加图片到缓存(异步)
mLoader.putBitmap(key, xxx, false);

//取出手动添加到缓存的图片
mLoader.loadBitmap(key);
```

####工具集合

######字符处理
```xml
//字符串MD5加密
StringUtils.encryptMD5(string);

//字符串SHA256加密
StringUtils.encryptSHA256(string);

//检查字符串是否是银行卡号
StringUtils.isBankCardCode(string);

//检查字符串是否是出生日期
StringUtils.isDateOfBirth(string);

//检查字符串是否是电子邮箱
StringUtils.isEmail(string);

//检查字符串是否是纯数字
StringUtils.isNumber(string);
```
