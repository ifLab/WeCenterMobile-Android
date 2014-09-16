package cn.fanfan.welcome;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.umeng.analytics.MobclickAgent;

import cn.fanfan.common.Config;
import cn.fanfan.common.MyProgressDialog;
import cn.fanfan.main.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends Activity {

	private EditText userNamEditText;
	private EditText maiEditText;
	private EditText passwdEditText;
	private EditText confirmPasswdEditText;
	private Button registerButton;
	private RegisterModel registerModel;
	private MyProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		registerModel = new RegisterModel();
		userNamEditText = (EditText) findViewById(R.id.register_username);
		maiEditText = (EditText) findViewById(R.id.register_mail);
		passwdEditText = (EditText) findViewById(R.id.register_passwd);
		confirmPasswdEditText = (EditText) findViewById(R.id.register_repasswd);
		registerButton = (Button) findViewById(R.id.register_ok);
		registerButton.setOnClickListener(new Click());
	}

	private class Click implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.register_ok:
				registerModel.setUserName(userNamEditText.getText().toString());
				registerModel.setMail(maiEditText.getText().toString());
				registerModel.setPasswd(passwdEditText.getText().toString());
				registerModel.setConfirmPasswd(confirmPasswdEditText.getText()
						.toString());
				if (!registerModel.getMail().matches("^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$")) {
					Toast.makeText(RegisterActivity.this, "邮箱格式不正确", Toast.LENGTH_SHORT).show();
					return;
				}
				if (!registerModel.getPasswd().equals(registerModel.getConfirmPasswd())) {
					Toast.makeText(RegisterActivity.this, "两次密码不一致", Toast.LENGTH_SHORT).show();
					return;
				}
				Login();
				break;

			default:
				break;
			}
		}
	}

	private void Login() {
		RequestParams params = new RequestParams();
		params.put("user_name", registerModel.getUserName());
		params.put("email", registerModel.getMail());
		params.put("password", registerModel.getPasswd());
		AsyncHttpClient client = new AsyncHttpClient();
		String url = Config.getValue("register");
		client.post(url, params,
				new AsyncHttpResponseHandler() {

					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						super.onStart();
						dialog = new MyProgressDialog(RegisterActivity.this, "正在提交", "请稍后...",
								true);
						dialog.show();
					}

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						// TODO Auto-generated method stub
						String result = new String(arg2);
						JSONObject jsonObject;
						try {
							jsonObject = new JSONObject(result);
							String err = jsonObject.getString("error");
							if (err.equals("")) {
								Toast.makeText(RegisterActivity.this,
										"注册成功,请登录!", Toast.LENGTH_SHORT).show();
								finish();
							}else {
								Toast.makeText(RegisterActivity.this,
										err, Toast.LENGTH_SHORT).show();
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						// TODO Auto-generated method stub
						Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
					}
					
					@Override
					public void onFinish() {
						// TODO Auto-generated method stub
						super.onFinish();
						dialog.hideAndCancle();
					}
				});
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
