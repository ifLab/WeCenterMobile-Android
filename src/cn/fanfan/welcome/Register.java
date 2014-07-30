package cn.fanfan.welcome;

import java.io.IOException;
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;

import cn.fanfan.common.NetLoad;
import cn.fanfan.main.R;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Register extends Activity {

	private EditText userNamEditText;
	private EditText maiEditText;
	private EditText passwdEditText;
	private EditText confirmPasswdEditText;
	private Button registerButton;
	private RegisterModel registerModel;

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
				(new AsyncRegister()).execute();
				break;

			default:
				break;
			}
		}
	}
	
	private class AsyncRegister extends AsyncTask<String, String, String>{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			NetLoad netLoad = new NetLoad();
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("user_name", registerModel.getUserName());
			map.put("email", registerModel.getMail());
			map.put("password", registerModel.getPasswd());
			String result = "!";
			try {
				result = netLoad.PostInformation("http://w.hihwei.com/api/register.php", map);
				System.out.println(result);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
	}
}
