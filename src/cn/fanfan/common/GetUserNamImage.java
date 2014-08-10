package cn.fanfan.common;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.fanfan.main.R;
import cn.fanfan.topic.imageload.ImageDownLoader;
import cn.fanfan.topic.imageload.ImageDownLoader.onImageLoaderListener;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class GetUserNamImage {
	private AsyncHttpClient client;
	private Context context;
	private ImageDownLoader downLoader;

	public GetUserNamImage(Context context) {
		// TODO Auto-generated constructor stub
		downLoader = new ImageDownLoader(context);
		client = new AsyncHttpClient();
		this.context = context;
	}

	public void getuserinfo(String uid, final TextView username,
			final ImageView userimage, final onLoaderListener listener) {
		String url = "http://w.hihwei.com/api/user.php?uid=" + uid;
		client.get(url, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				// TODO Auto-generated method stub
				Toast.makeText(context, "ªÒ»° ß∞‹", Toast.LENGTH_LONG).show();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				String information = new String(arg2);
				try {
					JSONObject jsonObject = new JSONObject(information);
					int errno = jsonObject.getInt("errno");
					if (errno == 1) {
						JSONObject rsm = jsonObject.getJSONObject("rsm");
						String user_name = rsm.getString("user_name");
						String avatar_file = rsm.getString("avatar_file");
						String mImageUrl = Config.getValue("userImageBaseUrl")
								+ avatar_file;
						 Bitmap bitmap = downLoader.getCacheBitmap(mImageUrl.replaceAll("[^\\w]", ""));
							if (bitmap != null) {
								listener.onPicLoader(bitmap, userimage);
							} else {
								userimage.setImageDrawable(context.getResources()
										.getDrawable(R.drawable.logo));
							
						downLoader.getBitmap(mImageUrl,
								new onImageLoaderListener() {

									public void onImageLoader(Bitmap bitmap,
											String url) {
										// TODO Auto-generated method stub
										listener.onPicLoader(bitmap, userimage);
									}
								});
							}
						listener.onNameLoader(user_name, username);
					} else {
						String err = jsonObject.getString("err");
						Toast.makeText(context, err, Toast.LENGTH_LONG).show();
						;
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		});
	}

	public void cancleTask() {
		downLoader.cacelTask();
	}

	public interface onLoaderListener {
		void onPicLoader(Bitmap bitmap, ImageView userimage);

		void onNameLoader(String name, TextView username);
	}
}
