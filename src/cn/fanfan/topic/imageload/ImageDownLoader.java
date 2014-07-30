package cn.fanfan.topic.imageload;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.fanfan.common.ImageGet;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.LruCache;

public class ImageDownLoader {
	private LruCache<String, Bitmap> memoryCache;
	private FileUtils fileUtils;
	private ExecutorService mImageThreadPool = null;

	public ImageDownLoader(Context context) {
		// TODO Auto-generated constructor stub
		int maxMemory = (int) Runtime.getRuntime().maxMemory();
		int mCacheSize = maxMemory / 8;
		memoryCache = new LruCache<String, Bitmap>(mCacheSize) {
			@Override
			protected int sizeOf(String key, Bitmap value) {
				// TODO Auto-generated method stub
				return value.getRowBytes() * value.getHeight();
			}
		};
		fileUtils = new FileUtils(context);
	}

	public ExecutorService getThreadPool() {
		if (mImageThreadPool == null) {
			synchronized (ExecutorService.class) {
				if (mImageThreadPool == null) {
					mImageThreadPool = Executors.newFixedThreadPool(2);
				}
			}
		}
		return mImageThreadPool;
	}

	public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
		if (getBitmapFromMemCache(key) == null && bitmap != null) {
			memoryCache.put(key, bitmap);
		}
	}

	public Bitmap getBitmapFromMemCache(String key) {
		return memoryCache.get(key);
	}

	@SuppressLint("HandlerLeak")
	public void getBitmap(final String url,final onImageLoaderListener listener){
		final String subUrl = url.replaceAll("[^\\w]", "");
		Bitmap bitmap = getCacheBitmap(subUrl);
		if (bitmap != null) {
			//System.out.println("load from memory");
			listener.onImageLoader(bitmap, url);
		}else {
			final Handler handler = new Handler(){
				@Override
				public void handleMessage(Message msg) {
					// TODO Auto-generated method stub
					super.handleMessage(msg);
					//System.out.println("load from internet");
					listener.onImageLoader((Bitmap)msg.obj, url);
				}
			};
			getThreadPool().execute(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Bitmap bitmap = getBitmapFromUrl(url);
					Message msg = handler.obtainMessage();
					msg.obj = bitmap;
					handler.sendMessage(msg);
					try {
						fileUtils.saveBitmap(subUrl, bitmap);
					} catch (Exception e) {
						// TODO: handle exception
					}
					addBitmapToMemoryCache(subUrl, bitmap);
				}
			});
		}
	}
	
	public Bitmap getCacheBitmap(String url){
		if (getBitmapFromMemCache(url) != null) {
			//System.out.println("load from cache");
			return getBitmapFromMemCache(url);
		}else if (fileUtils.isFileExists(url) && fileUtils.getFileSize(url)!=0) {
			Bitmap bitmap = fileUtils.getBitmap(url);
			addBitmapToMemoryCache(url, bitmap);
			//System.out.println("load from sd¿¨£¡");
			return bitmap;
		}
		return null;
	}
	
	private Bitmap getBitmapFromUrl(String url){
		Bitmap bitmap = null;
		/*HttpURLConnection connection = null;
		try {
			URL imageUrl = new URL(url);
			connection = (HttpURLConnection)imageUrl.openConnection();
			connection.setConnectTimeout(10 * 1000);
			connection.setReadTimeout(10*1000);
			connection.setDoInput(true);
			connection.setDoOutput(true);
			bitmap = BitmapFactory.decodeStream(connection.getInputStream());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if (connection != null) {
				connection.disconnect();
			}
		}*/
		ImageGet imageGet = new ImageGet();
		bitmap = imageGet.LoadPictureFromUrl(url);
		return bitmap;
	}
	
	public synchronized void cacelTask(){
		if (mImageThreadPool != null) {
			mImageThreadPool.shutdownNow();
			mImageThreadPool = null;
		}
	}
	
	public interface onImageLoaderListener {
		void onImageLoader(Bitmap bitmap, String url);
	}
}
