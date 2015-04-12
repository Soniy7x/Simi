package io.simi.graphics;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.util.LruCache;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import io.simi.utils.StringUtils;

/**
 * -------------------------------
 * 		 ImageCacheManager
 * -------------------------------
 *
 * createTime: 2015-04-12
 * updateTime: 2015-04-12
 *
 */
public final class ImageCacheManager {

    private static final String TAG = "Simi - ImageCache";

    private Map<String, SoftReference<Bitmap>> mSoftCache;
    private LruCache<String, Bitmap> mLruCache;
    private boolean external = false;
    private String cachedDir;

    public ImageCacheManager(Map<String, SoftReference<Bitmap>> imageCache, LruCache<String, Bitmap> mLruCache){
        this(imageCache, mLruCache, false, null);
    }

    public ImageCacheManager(Map<String, SoftReference<Bitmap>> imageCache, LruCache<String, Bitmap> mLruCache, boolean external, String cachedDir){
        this.mSoftCache = imageCache;
        this.mLruCache = mLruCache;
        this.external = external;
        this.cachedDir = cachedDir;
    }

    public void setExternal(boolean external, String cacheDir){
        this.external = external;
        this.cachedDir = cacheDir;
    }

    public void putBitmap(String key, Bitmap bitmap){
        if (bitmap == null) {
            Log.w(TAG, "图片为空，无法进行缓存");
            return;
        }
        mSoftCache.put(key, new SoftReference<Bitmap>(bitmap));
        mLruCache.put(key, bitmap);
        if (external) {
            String fileName = StringUtils.encryptMD5(key);
            String filePath = this.cachedDir + "/" +fileName;
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(filePath);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.close();
            } catch (Exception e) {
                Log.w(TAG, "图片缓存到SD卡失败");
            }
        }
    }

    public Bitmap getBitmapFromHttp(String url, boolean cache){
        Bitmap bitmap = null;
        try{
            URL u = new URL(url);
            HttpURLConnection conn = (HttpURLConnection)u.openConnection();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            if(cache){
                mSoftCache.put(url, new SoftReference<Bitmap>(bitmap));
                mLruCache.put(url, bitmap);
                if(external){
                    String fileName = StringUtils.encryptMD5(url);
                    String filePath = this.cachedDir + "/" +fileName;
                    FileOutputStream fos = new FileOutputStream(filePath);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    if (fos != null) {
                        fos.close();
                    }
                }
            }
            is.close();
            conn.disconnect();
            return bitmap;
        }catch(IOException e){
            return null;
        }
    }

    public Bitmap getBitmapFromFile(String path, boolean cache){
        Bitmap bitmap = null;
        try{
            bitmap = new Image(path).toBitmap();
            if(cache){
                mSoftCache.put(path, new SoftReference<Bitmap>(bitmap));
                mLruCache.put(path, bitmap);
                if(external){
                    String fileName = StringUtils.encryptMD5(path);
                    String filePath = this.cachedDir + "/" +fileName;
                    FileOutputStream fos = new FileOutputStream(filePath);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    fos.close();
                }
            }
            return bitmap;
        }catch(IOException e){
            return null;
        }
    }

    public Bitmap getBitmapFromMemory(String url){
        Bitmap bitmap = null;
        if(mSoftCache.containsKey(url)){
            synchronized(mSoftCache){
                SoftReference<Bitmap> bitmapRef = mSoftCache.get(url);
                if(bitmapRef != null){
                    bitmap = bitmapRef.get();
                    return bitmap;
                }
            }
        }
        bitmap = mLruCache.get(url);
        if (bitmap != null) {
            return bitmap;
        }
        if(external){
            bitmap = getBitmapFromExternal(url);
            if(bitmap != null)
                mSoftCache.put(url, new SoftReference<Bitmap>(bitmap));
        }
        return bitmap;
    }

    private Bitmap getBitmapFromExternal(String url){
        Bitmap bitmap = null;
        String fileName = StringUtils.encryptMD5(url);
        if(fileName == null)
            return null;
        String filePath = cachedDir + "/" + fileName;
        try {
            FileInputStream fis = new FileInputStream(filePath);
            bitmap = BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            bitmap = null;
        }
        return bitmap;
    }
}
