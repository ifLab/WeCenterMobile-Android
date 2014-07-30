package cn.fanfan.common;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

/* 功能：异步下载图片 
 * 
 * 1加网络权限，SD读写权限；
 *  <uses-permission android:name="android.permission.INTERNET"/>
 <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
 <uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS"/>
 AsyncImageGet第一个参数：所要获取图片的地址，第二个参数：图片下载完后所要填充的ImageView
 */
/*eg:
 AsyncImageGet asyncImageGet = new AsyncImageGet(url, myImageView);
 asyncImageGet.execute();
 */
public class AsyncImageGet extends AsyncTask<String, Integer, Bitmap> {

	private ImageView imageView;
	private String url;

	public AsyncImageGet(String url, ImageView imageView) {
		// TODO Auto-generated constructor stub
		this.imageView = imageView;
		this.url = url;
	}

	@Override
	protected Bitmap doInBackground(String... params) {
		// TODO Auto-generated method stub
		ImageGet imageGet = new ImageGet();
		Bitmap bitmap = imageGet.LoadPictureFromUrl(url);
		return bitmap;
	}

	@Override
	protected void onPostExecute(Bitmap result) {
		// TODO Auto-generated method stub
		imageView.setImageBitmap(result);
		super.onPostExecute(result);
	}
}
