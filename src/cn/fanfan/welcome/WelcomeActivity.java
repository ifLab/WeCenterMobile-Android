package cn.fanfan.welcome;

import com.loopj.android.http.PersistentCookieStore;
import com.umeng.analytics.MobclickAgent;

import cn.fanfan.common.FanfanSharedPreferences;
import cn.fanfan.main.MainActivity;
import cn.fanfan.main.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class WelcomeActivity extends Activity {

	private TextView lookAround;
	private Button login;
	private Button register;
	private boolean isRegister = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		PersistentCookieStore cookieStore = new PersistentCookieStore(
				WelcomeActivity.this);
		FanfanSharedPreferences fanfanSharedPreferences = new FanfanSharedPreferences(
				WelcomeActivity.this);
		boolean LoginStatus = fanfanSharedPreferences.getLogInStatus(false);
		String uid = fanfanSharedPreferences.getUid("");
		String userName = fanfanSharedPreferences.getUserName("");
		String passwd = fanfanSharedPreferences.getPasswd("");
		if (LoginStatus != false && !uid.equals("") && !passwd.equals("")
				&& !userName.equals("") && cookieStore.getCookies().size() != 0) {
			Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
		}
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.welcome);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		login = (Button) findViewById(R.id.login);
		register = (Button) findViewById(R.id.register);
		lookAround = (TextView) findViewById(R.id.look_around);
		login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(WelcomeActivity.this, LoginActivity.class);
				startActivity(intent);
			}
		});
		register.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isRegister = true;
				Intent intent = new Intent();
				intent.setClass(WelcomeActivity.this, RegisterActivity.class);
				startActivity(intent);
			}
		});
		lookAround.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				FanfanSharedPreferences sharedPreferences = new FanfanSharedPreferences(
						WelcomeActivity.this);
				sharedPreferences.setLogInStatus(false);
				Intent intent = new Intent();
				intent.setClass(WelcomeActivity.this, MainActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		if (isRegister) {

		} else {
			finish();
		}
		super.onStop();
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
}
