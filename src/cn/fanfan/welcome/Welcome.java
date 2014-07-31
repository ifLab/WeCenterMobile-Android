package cn.fanfan.welcome;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		FanfanSharedPreferences fanfanSharedPreferences = new FanfanSharedPreferences(Welcome.this);
		boolean LoginStatus = fanfanSharedPreferences.getLogInStatus(false);
		String uid = fanfanSharedPreferences.getUid("");
		String userName = fanfanSharedPreferences.getUserName("");
		if (LoginStatus != false && !uid.equals("") && !userName.equals("")) {
			Intent intent = new Intent(Welcome.this,MainActivity.class);
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
		register = (Button)findViewById(R.id.register);
		lookAround = (TextView)findViewById(R.id.look_around);
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
				Intent intent = new Intent();
				intent.setClass(Welcome.this, Register.class);
				startActivity(intent);
			}
		});
		lookAround.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				FanfanSharedPreferences sharedPreferences = new FanfanSharedPreferences(Welcome.this);
				sharedPreferences.setLogInStatus(false);
				Intent intent = new Intent();
				intent.setClass(Welcome.this, MainActivity.class);
				startActivity(intent);
			}
		});
	}
}
