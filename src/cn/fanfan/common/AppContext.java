package cn.fanfan.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class AppContext {

	private NetworkInfo networkInfo;
	public static final int CMNET = 3;
	public static final int CMWAP = 2;
	public static final int WIFI = 0;

	public static int getAPNType(Context context) {
		/**
		 * 
		 * 
		 * 获取当前的网络状态 -1：没有网络 1：WIFI网络2：wap网络3：net网络
		 * 
		 * @param context
		 * 
		 * @return
		 */
		int netType = -1;

		ConnectivityManager connMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo == null) {
			return netType;
		}

		int nType = networkInfo.getType();
		if (nType == ConnectivityManager.TYPE_MOBILE) {
			Log.e("networkInfo.getExtraInfo()",
					"networkInfo.getExtraInfo() is "
							+ networkInfo.getExtraInfo());
			if (networkInfo.getExtraInfo().toLowerCase().equals("cmnet")) {
				netType = CMNET;
			} else {
				netType = CMWAP;
			}
		} else if (nType == ConnectivityManager.TYPE_WIFI) {
			netType = WIFI;
		}
		return netType;
	}
}
