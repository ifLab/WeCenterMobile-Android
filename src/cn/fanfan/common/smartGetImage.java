package cn.fanfan.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

public class smartGetImage {
	private ImageView imageView;
	private String url;
	private Bitmap bitmap;

	public smartGetImage(Context context, String url, ImageView imageView) {
		ImageFileUtils imageFileUtils = new ImageFileUtils(context);
		if (imageFileUtils.isFileExists(url.replace("[^\\w]", ""))) {
			imageView.setImageBitmap(imageFileUtils.getBitmap(url.replace(
					"[^\\w]", "")));
		} else {
			AsyncImageGet asyncImageGet = new AsyncImageGet(url, imageView);
			asyncImageGet.execute();
		}
	}
}
