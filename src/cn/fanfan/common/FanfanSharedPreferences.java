package cn.fanfan.common;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/*
 * 
 */
public class FanfanSharedPreferences {
	private SharedPreferences sharedPreferences;
	private SharedPreferences.Editor editor;

	@SuppressLint("CommitPrefEdits")
	public FanfanSharedPreferences(Context context) {
		super();
		sharedPreferences = context.getSharedPreferences("fanfan",
				Activity.MODE_PRIVATE);
		editor = sharedPreferences.edit();
	}

	public void setUid(String uid) {
		editor.putString("uid", uid).commit();
	}

	public String getUid(String defaultString) {
		return sharedPreferences.getString("uid", defaultString);
	}

	public void setLogInStatus(boolean logInStatus) {
		editor.putBoolean("logInStatus", logInStatus).commit();
	}

	public boolean getLogInStatus(boolean defaultBoolean) {
		return sharedPreferences.getBoolean("logInStatus", false);
	}

	public void setUserName(String userName) {
		editor.putString("userName", userName).commit();
	}

	public String getUserName(String defaultUserName) {
		return sharedPreferences.getString("userName", defaultUserName);
	}
}
