package cn.fanfan.common;

import java.io.File;
import java.io.FileNotFoundException;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;
import cn.fanfan.main.R;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

public class AsyncFileUpLoad {
	public String preview = null, errno, err;
	private Context context;
	private static TipsToast tipsToast;
	private AsyncHttpClient client;
	// private PersistentCookieStore myCookieStore;
	private File file;
	private String url, responseContent;

	public AsyncFileUpLoad(Context context, String url, String filePath,
			CallBack callBack) {
		Log.i("url", url);
		Log.i("filePath", filePath);
		this.context = context;
		this.url = url;
		file = new File(filePath);
		NetworkState networkState = new NetworkState();

		client = new AsyncHttpClient();
		PersistentCookieStore mCookieStore = new PersistentCookieStore(context);
		client.setCookieStore(mCookieStore);
		if (networkState.isNetworkConnected(context)) {
			Login();
			upLoad(callBack);
		} else {
			showTips(R.drawable.tips_error, R.string.net_break);
		}

	}

	/* 获取到cookie后上传头像 并对上传后信息做处理 */
	private void upLoad(final CallBack callBack) {
		RequestParams params1 = new RequestParams();
		try {
			params1.put("user_avatar", file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// client.setTimeout(5000);
		client.post(url, params1, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				// TODO Auto-generated method stub
				responseContent = new String(responseBody);
				Log.i("upLoadOnSuccess", responseContent + "---Success");
				JSONTokener jsonParser = new JSONTokener(responseContent);
				try {
					JSONObject result = (JSONObject) jsonParser.nextValue();
					errno = result.getString("errno");
					err = result.getString("err");
					Log.i("errno", errno);
					Log.i("err", err);
					JSONObject rsm = new JSONObject();
					rsm = result.getJSONObject("rsm");
					JSONTokener jsonParser2 = new JSONTokener(rsm.toString());
					JSONObject rsmcontent = (JSONObject) jsonParser2
							.nextValue();
					preview = rsmcontent.getString("preview");
					Log.i("preview", preview);
					callBack.callBack(preview, err, errno);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Toast.makeText(context, err, Toast.LENGTH_LONG).show();
				}

			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				// TODO Auto-generated method stub
				responseContent = new String(responseBody);
				preview = null;
				err = null;
				errno = "x";
				// callBack.callBack(preview, err, errno);
				Log.i("upLoadOnFailure", responseContent + "---Failure");
			}
		});
	}

	/* 先再次登录获得刷新cookie 。 */
	private void Login() {
		RequestParams params = new RequestParams();
		FanfanSharedPreferences user = new FanfanSharedPreferences(context);
		params.put("user_name", user.getUserName(""));// 待改
		params.put("password", user.getPasswd(""));// 待改
		client.post(context,
				"http://w.hihwei.com/?/api/account/login_process/", params,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] responseBody) {
						responseContent = new String(responseBody);
						Log.i("LoginSuccess", responseContent + "---Success");
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							byte[] responseBody, Throwable error) {
						// TODO Auto-generated method stub
						responseContent = new String(responseBody);
						Log.i("LoninOnFailure", responseContent + "---Failure");
						showTips(R.drawable.tips_error, R.string.login_wrong);
					}
				});
	}

	/* 网络异常将弹出的提醒 */
	private void showTips(int iconResId, int msgResId) {
		if (tipsToast != null) {
			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
				tipsToast.cancel();
			}
		} else {
			tipsToast = TipsToast.makeText(context, msgResId,
					TipsToast.LENGTH_SHORT);
		}
		tipsToast.show();
		tipsToast.setIcon(iconResId);
		tipsToast.setText(msgResId);
	}

	public interface CallBack {
		public void callBack(String preview, String err, String errno);
	}
}
