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

public class Welcome extends Activity {

	private TextView lookAround;
	private Button login;
	private Button register;
	private boolean isRegister = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		PersistentCookieStore cookieStore = new PersistentCookieStore(
				Welcome.this);
		FanfanSharedPreferences fanfanSharedPreferences = new FanfanSharedPreferences(
				Welcome.this);
		boolean LoginStatus = fanfanSharedPreferences.getLogInStatus(false);
		String uid = fanfanSharedPreferences.getUid("");
		String userName = fanfanSharedPreferences.getUserName("");
		String passwd = fanfanSharedPreferences.getPasswd("");
		if (LoginStatus != false && !uid.equals("") && !passwd.equals("")
				&& !userName.equals("") && cookieStore.getCookies().size() != 0) {
			Intent intent = new Intent(Welcome.this, MainActivity.class);
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
				intent.setClass(Welcome.this, Login.class);
				startActivity(intent);
			}
		});
		register.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isRegister = true;
				Intent intent = new Intent();
				intent.setClass(Welcome.this, Register.class);
				startActivity(intent);
			}
		});
		lookAround.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				FanfanSharedPreferences sharedPreferences = new FanfanSharedPreferences(
						Welcome.this);
				sharedPreferences.setLogInStatus(false);
				Intent intent = new Intent();
				intent.setClass(Welcome.this, MainActivity.class);
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
