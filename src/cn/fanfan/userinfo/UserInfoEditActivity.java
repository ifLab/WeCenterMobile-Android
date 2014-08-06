package cn.fanfan.userinfo;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.security.auth.callback.Callback;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cn.fanfan.common.AsyncFileUpLoad;
import cn.fanfan.common.AsyncFileUpLoad.CallBack;
import cn.fanfan.common.AsyncImageGet;
import cn.fanfan.common.CompressAvata;
import cn.fanfan.common.Config;
import cn.fanfan.common.GlobalVariables;
import cn.fanfan.common.NetworkState;
import cn.fanfan.main.R;
import cn.fanfan.question.Bimp;
import android.R.string;
import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class UserInfoEditActivity extends Activity implements OnClickListener,
		DatePickerDialog.OnDateSetListener {
	private int sex;// sex (int，1：男 2：女 3：保密)
	private String birthday, errno, err, job_id, user_name, uid, signature,
			avatarpath, avatar_file;// birthday为unix时间戳
	private ImageView iv_avatar;
	private EditText et_username, et_introduction;
	private LinearLayout lv_birthday, lv_business;
	private TextView tv_sex_f, tv_sex_m, tv_sex_f_background,
			tv_sex_m_background, tv_birthday_info, tv_business_info;
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	private static final int PICK_IMAGE_ACTIVITY_REQUEST_CODE = 300;
	private Uri avatarUri;
	private SelectPicPopupWindow menuWindow;// 点击头像弹出选择拍照或者选择图库的弹出菜单
	private String newAvatarPath;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.userinformation_edit);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.show();
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		uid = bundle.getString("uid");
		avatar_file = bundle.getString("avatar_file");
		init();// 图形界面初始化

		NetworkState networkState = new NetworkState();
		if (networkState.isNetworkConnected(UserInfoEditActivity.this)) {
			getUserProfile();
		} else {
			Toast.makeText(this, "没有网络，请连接后操作！", Toast.LENGTH_SHORT).show();
		}
	}

	// 获取用户资料
	private void getUserProfile() {
		// TODO Auto-generated method stub
		AsyncHttpClient getUserInfo = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("uid", uid);
		getUserInfo.get(Config.getValue("ProfileUrl"), params,
				new AsyncHttpResponseHandler() {

					@Override
					public void onFailure(int arg0, Header[] arg1,
							byte[] responseBody, Throwable arg3) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(int arg0, Header[] arg1,
							byte[] responseBody) {
						// TODO Auto-generated method stub
						String responseContent = new String(responseBody);
						Log.i("getUserInfo", responseContent + "---Success");
						JSONTokener jsonParser = new JSONTokener(
								responseContent);
						try {
							JSONObject result = (JSONObject) jsonParser
									.nextValue();
							errno = result.getString("errno");
							err = result.getString("err");
							Log.i("errno", errno);
							Log.i("err", err);
							JSONArray rsm = new JSONArray();
							rsm = result.getJSONArray("rsm");
							Log.i("getJSONArray", rsm.toString());
							// 解析数组rsm的数据
							JSONObject rsmcontent = (JSONObject) rsm.get(0);
							Log.i("rsmcontent", rsmcontent.toString());
							JSONTokener jsonParser2 = new JSONTokener(
									rsmcontent.toString());
							JSONObject rsmcontents = (JSONObject) jsonParser2
									.nextValue();
							user_name = rsmcontents.getString("user_name");
							sex = Integer.parseInt(rsmcontents.getString("sex"));//
							birthday = rsmcontents.getString("birthday");
							job_id = rsmcontents.getString("job_id");
							signature = rsmcontents.getString("signature");
							Log.i("user_name", user_name);
							Log.i("birthday", birthday);
							Log.i("signature", signature);
							updateUI();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							// 解析异常。
						}
					}
				});
	}

	protected void updateUI() {
		// TODO Auto-generated method stub
		et_username.setText(user_name);
		et_introduction.setText(signature);
		// p
		if (sex == 1) {
			tv_sex_m_background.setBackgroundColor(Color.parseColor("#50FCFF"));
			tv_sex_f_background.setBackgroundColor(Color.parseColor("#DCE0DD"));
		}
		if (sex == 2) {
			tv_sex_f_background.setBackgroundColor(Color.parseColor("#50FCFF"));
			tv_sex_m_background.setBackgroundColor(Color.parseColor("#DCE0DD"));
		}
		// 展示用户生日信息
		if (birthday != "null") {
			String date = TimeStamp2Date(birthday, "yyyy-MM-dd ");
			tv_birthday_info.setText(date);
			Log.i("date", date);
		}

		if (avatar_file != null) {
			AsyncImageGet getAvatar = new AsyncImageGet(
					Config.getValue("AvatarPrefixUrl") + avatar_file, iv_avatar);
			getAvatar.execute();
		}
	}

	// 将unix时间戳Java将Unix时间戳转换成指定格式日期
	public String TimeStamp2Date(String timestampString, String formats) {
		Long timestamp = Long.parseLong(timestampString) * 1000;
		String date = new java.text.SimpleDateFormat(formats)
				.format(new java.util.Date(timestamp));
		return date;
	}

	// 初始化
	private void init() {
		// TODO Auto-generated method stub
		iv_avatar = (ImageView) findViewById(R.id.iv_avatar);
		et_username = (EditText) findViewById(R.id.et_uername);
		et_introduction = (EditText) findViewById(R.id.et_introduction);
		lv_birthday = (LinearLayout) findViewById(R.id.lv_birthday);
		lv_business = (LinearLayout) findViewById(R.id.lv_business);
		tv_sex_f = (TextView) findViewById(R.id.tv_sex_f);
		tv_sex_m = (TextView) findViewById(R.id.tv_sex_m);
		tv_sex_f_background = (TextView) findViewById(R.id.tv_sex_f_background);
		tv_sex_m_background = (TextView) findViewById(R.id.tv_sex_m_background);
		tv_birthday_info = (TextView) findViewById(R.id.tv_birthday_info);
		tv_business_info = (TextView) findViewById(R.id.tv_business_info);
		iv_avatar.setOnClickListener(this);
		et_username.setOnClickListener(this);
		et_introduction.setOnClickListener(this);
		lv_birthday.setOnClickListener(this);
		lv_business.setOnClickListener(this);
		tv_sex_f_background.setOnClickListener(this);
		tv_sex_m_background.setOnClickListener(this);
		tv_sex_m.setOnClickListener(this);
		tv_sex_f.setOnClickListener(this);
		tv_birthday_info.setOnClickListener(this);
		tv_business_info.setOnClickListener(this);
	}

	/* 主界面的view的监听 及处理 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_avatar:
			menuWindow = new SelectPicPopupWindow(UserInfoEditActivity.this,
					itemsOnClick);
			// 显示窗口
			menuWindow.showAtLocation(UserInfoEditActivity.this
					.findViewById(R.id.infoedit_layout), Gravity.BOTTOM
					| Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
			break;
		case R.id.et_uername:
			user_name = et_username.getText().toString();
			Log.i("user_name", user_name);
			break;
		case R.id.et_introduction:
			signature = et_introduction.getText().toString();
			Log.i("signature", signature);
			break;
		case R.id.tv_sex_m:
			sex = 1;
			tv_sex_m_background.setBackgroundColor(Color.parseColor("#50FCFF"));
			tv_sex_f_background.setBackgroundColor(Color.parseColor("#DCE0DD"));
			break;
		case R.id.tv_sex_f:
			sex = 2;
			tv_sex_f_background.setBackgroundColor(Color.parseColor("#50FCFF"));
			tv_sex_m_background.setBackgroundColor(Color.parseColor("#DCE0DD"));
			break;
		case R.id.lv_birthday:
			DialogFragment newFragment = new DatePickerFragment();
			newFragment.show(getFragmentManager(), "datePicker");
			break;
		case R.id.tv_birthday_info:
			DialogFragment newFragment2 = new DatePickerFragment();
			newFragment2.show(getFragmentManager(), "datePicker");
			break;
		case R.id.lv_business:
			Toast.makeText(this, "手机端暂不支持更改，请登录网站更改！", Toast.LENGTH_LONG)
					.show();
			break;

		default:
			break;
		}
	}

	/* popUpWindows（弹出选择拍照或者从图库选择照片的菜单）各个item 的监听及处理 */
	private OnClickListener itemsOnClick = new OnClickListener() {

		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_pick_photo:// 打开相册
				menuWindow.dismiss();
				Intent openAlbumIntent = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(openAlbumIntent,
						PICK_IMAGE_ACTIVITY_REQUEST_CODE);// 打开相册
				break;
			case R.id.btn_take_photo:// 打开相机
				menuWindow.dismiss();
				Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
				String path = Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED) ? Environment
						.getExternalStorageDirectory().getPath() + "/fanfan"
						: null + "/fanfan";
				File foldFile = new File(path);
				if (!foldFile.exists()) {
					foldFile.mkdir();
				}
				avatarpath = path + File.separator + uid + "avatarImage.jpg";
				File avatarFile = new File(path + File.separator + uid
						+ "avatarImage.jpg");
				avatarUri = Uri.fromFile(avatarFile);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, avatarUri);
				startActivityForResult(intent,
						CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);// 打开相机
				break;
			case R.id.btn_cancel:
				menuWindow.dismiss();
				break;
			default:
				break;
			}

		}

	};

	/* 调用系统图库和相机照相后的处理 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch (requestCode) {
		case CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE:
			if (resultCode == RESULT_OK) {
				// 拍照头像照片成功后上传头像
				upLoadAnim();
				// CompressAvata compressAvata = new
				// CompressAvata(avatarpath);// 压缩处理。
				// compressAvata.getCompressAvatarPath()
				try {
					newAvatarPath = Bimp.revitionImageSize(avatarpath);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					Toast.makeText(UserInfoEditActivity.this, "选择照片失败，请重新选择。",
							Toast.LENGTH_LONG).show();
					e.printStackTrace();
				}
				if (newAvatarPath != null) {
					AsyncFileUpLoad asyncFileUpLoad = new AsyncFileUpLoad(
							UserInfoEditActivity.this,
							Config.getValue("AvatarUploadUrl"), newAvatarPath,
							new CallBack() {
								/* 上传后对结果操作，如果成功下载头像，失败输出err信息 */
								@Override
								public void callBack(String preview,
										String err, String errno) {
									GlobalVariables.uSER_IMAGE_URL = preview;
									Log.i("callbackinfo", preview);
									Log.i("err", err);
									Log.i("errno", errno);
									if (errno == "x") {
										Toast.makeText(
												UserInfoEditActivity.this,
												"网络有点不好哦，再来一次吧！",
												Toast.LENGTH_LONG).show();
									} else {
										iv_avatar.clearAnimation();
										AsyncImageGet getAvatarPreview = new AsyncImageGet(
												preview, iv_avatar);
										getAvatarPreview.execute();
									}

								}
							});
				}

			} else if (resultCode == RESULT_CANCELED) {
				// User cancelled the Pick avatar
			} else {
				// Pick avatar failed, advise user
			}
			break;
		case PICK_IMAGE_ACTIVITY_REQUEST_CODE:
			if (resultCode == RESULT_OK) {
				// 从相册选择头像照片后操作
				if (data != null) {
					avatarUri = data.getData();
					Log.i("avatarUri", avatarUri.getPath());
					/* 根据uri在media数据库查询到真实的文件路径 */

					String[] proj = { MediaStore.Images.Media.DATA };
					CursorLoader loader = new CursorLoader(
							UserInfoEditActivity.this, avatarUri, proj, null,
							null, null);
					Cursor cursor = loader.loadInBackground();
					int column_index = cursor
							.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
					cursor.moveToFirst();
					avatarpath = cursor.getString(column_index);

					// 获取完路径
					upLoadAnim();// 上传时的动画
					try {
						newAvatarPath = Bimp.revitionImageSize(avatarpath);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						Toast.makeText(UserInfoEditActivity.this,
								"选择照片失败，请重新选择。", Toast.LENGTH_LONG).show();
						e.printStackTrace();
					}
					if (avatarpath != null) {

					}
					AsyncFileUpLoad asyncFileUpLoad = new AsyncFileUpLoad(
							UserInfoEditActivity.this,
							Config.getValue("AvatarUploadUrl"), newAvatarPath,
							new CallBack() {
								/* 上传后对结果操作，如果成功下载头像，失败输出err信息 */
								@Override
								public void callBack(String preview,
										String err, String errno) {
									// TODO Auto-generated method stub
									Log.i("callbackinfo", preview);
									Log.i("err", err);
									Log.i("errno", errno);
									if (errno == "x") {
										Toast.makeText(
												UserInfoEditActivity.this,
												"网络有点不好哦，再来一次吧！",
												Toast.LENGTH_LONG).show();
									} else {
										iv_avatar.clearAnimation();
										AsyncImageGet getAvatarPreview = new AsyncImageGet(
												preview, iv_avatar);
										getAvatarPreview.execute();
									}
								}
							});
				}

			} else if (resultCode == RESULT_CANCELED) {
				// User cancelled the image capture
			} else {
				// Image capture failed, advise user
			}
			break;

		default:
			break;
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	private void upLoadAnim() {
		iv_avatar.setImageResource(R.drawable.ic_loading);
		RotateAnimation animation = new RotateAnimation(0, 359,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		LinearInterpolator linearInterpolator = new LinearInterpolator();
		animation.setDuration(1000);
		animation.setInterpolator(linearInterpolator);
		animation.setRepeatCount(-1);
		iv_avatar.startAnimation(animation);
	}

	/* datepicker的回调 处理用户设定生日后的操作 */
	@Override
	public void onDateSet(DatePicker view, int year, int month, int day) {
		// Do something with the date chosen by the user
		Log.i("year", Integer.toString(year));
		Log.i("month", Integer.toString((month + 1)));
		Log.i("day", Integer.toString(day));
		tv_birthday_info.setText(Integer.toString(year) + "-"
				+ Integer.toString((month + 1)) + "-" + Integer.toString(day));
		String dateString = Integer.toString(year) + "-"
				+ Integer.toString((month + 1)) + "-" + Integer.toString(day)
				+ " " + "12:00:00";

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		try {
			Date date = sdf.parse(dateString);
			birthday = Long.toString(date.getTime() / 1000);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("时间转换成 unix时间戳 出错");
		}

		Log.i("time", birthday);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.edit_complete, menu);
		return super.onCreateOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.edit_complete) {
			Log.i("完成", "完成");
			((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
					.hideSoftInputFromWindow(et_username.getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
			upDateProfile();
			this.finish();
		}
		if (id == android.R.id.home) {
			this.finish();
		}
		return super.onOptionsItemSelected(item);
	}

	private void upDateProfile() {
		// TODO Auto-generated method stub
		signature = et_introduction.getText().toString();
		user_name = et_username.getText().toString();
		AsyncHttpClient upLoadProfile = new AsyncHttpClient();
		RequestParams UpParams = new RequestParams();
		UpParams.put("uid", uid);
		UpParams.put("user_name", user_name);
		UpParams.put("sex", sex);
		UpParams.put("signature", signature);
		UpParams.put("job_id", job_id);
		UpParams.put("birthday", birthday);
		upLoadProfile.post(Config.getValue("ProfileSettingUrl"), UpParams,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1,
							byte[] responseBody) {
						// TODO Auto-generated method stub
						String responseContent = new String(responseBody);
						Log.i("upDateProfile", responseContent + "---Success");
						JSONTokener jsonParser = new JSONTokener(
								responseContent);
						try {
							JSONObject result = (JSONObject) jsonParser
									.nextValue();
							errno = result.getString("errno");
							err = result.getString("err");
							Log.i("errno", errno);
							Log.i("err", err);
							adviseUesr(err);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							// 解析异常。
						}
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						// TODO Auto-generated method stub
						Log.i("上传更改失败！", "x-x");
					}
				});
	}

	private void adviseUesr(String err) {
		if (err != "null") {
			Toast.makeText(this, err, Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(this, "修改成功！", Toast.LENGTH_LONG).show();
		}
	}
}
