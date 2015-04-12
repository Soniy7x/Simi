package io.simi.graphics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.util.LruCache;
import android.view.View;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.simi.exception.ByteNotFoundException;

/**
 * -------------------------------
 * 			ImageLoader
 * -------------------------------
 * 
 * createTime: 2015-04-12
 * updateTime: 2015-04-12
 * 
 */
public final class ImageLoader {

    private static final String TAG = "Simi - ImageLoader";
	private static final int LRUCACHE_SIZE = 8 * 1024 * 1024;
    private static HashSet<String> mSet;
    private static Map<String,SoftReference<Bitmap>> mSoftCache;
    private static LruCache<String, Bitmap> mLruCache;
    private static ImageCacheManager mCacheManager;
    private static ExecutorService mExecutorService;
    private static Map<String, List<OnImageLoadListener>> mWaitor;
    private Handler mHandler;  
    
    private static ImageLoader instance;

    static{  
        mSet = new HashSet<String>();  
        mLruCache = new LruCache<String, Bitmap>(LRUCACHE_SIZE);
        mSoftCache = new HashMap<String,SoftReference<Bitmap>>();  
        mCacheManager = new ImageCacheManager(mSoftCache , mLruCache);  
        mWaitor = new HashMap<String, List<OnImageLoadListener>>();
    }  

    private ImageLoader(Context context){  
        mHandler = new Handler();
        startThreadPoolIfNecessary(); 
        setExternal(true, context.getCacheDir().getAbsolutePath());
    }  

    private ImageLoader(Context context, boolean external){  
        mHandler = new Handler();
        startThreadPoolIfNecessary(); 
        setExternal(external, context.getCacheDir().getAbsolutePath());
    }

    public static ImageLoader getInstance(Context context) {
		return getInstance(context, true);
	}

    public static ImageLoader getInstance(Context context, boolean external) {
		if (instance == null) {
			instance = new ImageLoader(context, external);
		}
		return instance;
	}

    public void setExternal(boolean external, String cacheDir){
    	mCacheManager.setExternal(external, cacheDir);
    }

    private static void startThreadPoolIfNecessary(){
        if(mExecutorService == null || mExecutorService.isShutdown() || mExecutorService.isTerminated()){  
            mExecutorService = Executors.newFixedThreadPool(3);
        }
    }  

    public Bitmap loadBitmap(String key){
    	if(TextUtils.isEmpty(key)){  
            Log.w(TAG, "图片键值不得为空");
            return null;
        }
    	Bitmap bitmap = mCacheManager.getBitmapFromMemory(key);  
        if(bitmap != null){  
            return bitmap;
        }
        return null;
    }

    public void putBitmap(final String key, final Bitmap bitmap, boolean service){
    	if (!service) {
    		mCacheManager.putBitmap(key, bitmap); 
		}else {
			mExecutorService.submit(new Runnable(){  
	            @Override  
	            public void run() {  
	                mHandler.post(new Runnable(){  
	                    @Override  
	                    public void run(){  
	                        mCacheManager.putBitmap(key, bitmap); 
	                    }  
	                });  
	            }  
	        });
		}
    }

    public void putBitmap(String key, Drawable drawable, boolean service){
    	putBitmap(key, new Image(drawable).toBitmap(), service);
    }

    public void putBitmap(String key, Image image, boolean service){
    	putBitmap(key, image.toBitmap(), service);
    }

    public void putBitmap(String key, byte[] bs, boolean service) throws ByteNotFoundException {
    	putBitmap(key, new Image(bs).toBitmap(), service);
    }

    public void loadBitmapByHttp(final View view, final String url, final OnImageLoadListener callback){  
    	loadBitmapByHttp(view, url, true, callback);  
    } 

    public void loadBitmapByHttp(final View view, final String url, final boolean cache, final OnImageLoadListener callback){  
    	 Bitmap bitmap = mCacheManager.getBitmapFromMemory(url);  
         if(bitmap != null){  
             if(callback != null){  
                 callback.onImageLoadCompleted(view, bitmap, url);  
             }  
         }else{
         	if(mSet.contains(url)){  
         		if (mWaitor.containsKey(url)) {
 					mWaitor.get(url).add(callback);
 				}else {
 					List<OnImageLoadListener> callbacks = new ArrayList<OnImageLoadListener>();
 					callbacks.add(callback);
 					mWaitor.put(url, callbacks);
 				}
         		return;
         	}  
            mSet.add(url);  
            mExecutorService.submit(new Runnable(){  
                @Override  
                public void run() {  
                    final Bitmap bitmap = mCacheManager.getBitmapFromHttp(url, cache);  
                    mHandler.post(new Runnable(){  
                        @Override  
                        public void run(){  
                            if(callback != null)  
                                callback.onImageLoadCompleted(view, bitmap, url);  
                            mSet.remove(url); 
                            if (mWaitor.containsKey(url)) {
                            	List<OnImageLoadListener> callbacks = mWaitor.get(url);
                            	for (OnImageLoadListener callback : callbacks) {
                            		callback.onImageLoadCompleted(view, bitmap, url);
                            	}
                            }
                        }  
                    });  
                }  
            });  
        }  
    }

    public void loadBitmapByFile(final View view, final String url, final OnImageLoadListener callback){  
    	loadBitmapByFile(view, url, true, callback);  
    } 

    public void loadBitmapByFile(final View view, final String url, final boolean cache, final OnImageLoadListener callback){
    	Bitmap bitmap = mCacheManager.getBitmapFromMemory(url);  
        if(bitmap != null && callback != null){
        	callback.onImageLoadCompleted(view, bitmap, url);
        }else{  
        	if(mSet.contains(url)){  
        		if (mWaitor.containsKey(url)) {
					mWaitor.get(url).add(callback);
				}else {
					List<OnImageLoadListener> callbacks = new ArrayList<OnImageLoadListener>();
					callbacks.add(callback);
					mWaitor.put(url, callbacks);
				}
        		return;
        	} 
        	mSet.add(url);  
            mExecutorService.submit(new Runnable(){  
                @Override  
                public void run() {  
					final Bitmap bitmap = mCacheManager.getBitmapFromFile(url, cache);
					mHandler.post(new Runnable(){  
						@Override  
						public void run(){  
							if(callback != null)  
								callback.onImageLoadCompleted(view, bitmap, url);
							mSet.remove(url);  
							if (mWaitor.containsKey(url)) {
                            	List<OnImageLoadListener> callbacks = mWaitor.get(url);
                            	for (OnImageLoadListener callback : callbacks) {
									callback.onImageLoadCompleted(view, bitmap, url);
								}
							}
						}  
					});  
                }  
            });  
        }  
    }

    public interface OnImageLoadListener{
        public void onImageLoadCompleted(View view, Bitmap bitmap, String imageUrl);  
    }
}