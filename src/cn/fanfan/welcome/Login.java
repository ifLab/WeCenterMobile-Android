package cn.fanfan.welcome;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

import cn.fanfan.common.FanfanSharedPreferences;
import cn.fanfan.common.GlobalVariables;
import cn.fanfan.main.MainActivity;
import cn.fanfan.main.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {
	private EditText userNameEditText;
	private EditText passwdEditText;
	private Button button;
	private AsyncHttpClient client;
	private PersistentCookieStore myCookieStore;
	private RequestParams params;
	private Button noButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		userNameEditText = (EditText) findViewById(R.id.login_username);
		passwdEditText = (EditText) findViewById(R.id.login_passwd);
		button = (Button) findViewById(R.id.login_ok);
		noButton = (Button)findViewById(R.id.login_no);
		client = new AsyncHttpClient();
		myCookieStore = new PersistentCookieStore(Login.this);
		client.setCookieStore(myCookieStore);
		params = new RequestParams();
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				params.put("user_name", userNameEditText.getText().toString());
				params.put("password", passwdEditText.getText().toString());
				login();
			}
		});
		noButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	public void login(){
		client.post(Login.this, "http://w.hihwei.com/?/api/account/login_process/", params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				System.out.println(new String(arg2));
				try {
					JSONObject jsonObject = new JSONObject(new String(arg2));
					int errno = jsonObject.getInt("errno");
					if (errno == -1) {
						String err = jsonObject.getString("err");
						Toast.makeText(Login.this, err, Toast.LENGTH_SHORT).show();
					}else {
						
						//GlobalVariables.COOKIE_Store = myCookieStore;
						String rsm = jsonObject.getString("rsm");
						//System.out.println(rsm);
						JSONObject jsonObject2 = new JSONObject(rsm);
						String uid = jsonObject2.getString("uid");
						String user_name = jsonObject2.getString("user_name");
						String avatar_file = jsonObject2.getString("avatar_file");
						GlobalVariables.USER_NAME = user_name;
						GlobalVariables.uSER_IMAGE_URL = avatar_file;
						
						FanfanSharedPreferences sharedPreferences = new FanfanSharedPreferences(Login.this);
						sharedPreferences.setUid(uid);
						sharedPreferences.setLogInStatus(true);
						sharedPreferences.setUserName(user_name);
						sharedPreferences.setPasswd(passwdEditText.getText().toString());
						
						Intent intent = new Intent(Login.this,MainActivity.class);
						startActivity(intent);
						Toast.makeText(Login.this, "µÇÂ¼³É¹¦", Toast.LENGTH_SHORT).show();
						finish();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				Toast.makeText(Login.this, "µÇÂ¼Ê§°Ü", Toast.LENGTH_SHORT).show();
			}
		});
	}
	
}
