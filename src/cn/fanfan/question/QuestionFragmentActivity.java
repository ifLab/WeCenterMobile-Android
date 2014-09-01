package cn.fanfan.question;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.client.CookieStore;
import org.json.JSONException;
import org.json.JSONObject;

import cn.fanfan.common.MyProgressDialog;
import cn.fanfan.main.R;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import cn.fanfan.common.Config;
import cn.fanfan.detailquestion.DetailQuestionActivity;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class QuestionFragmentActivity extends FragmentActivity {
	private AsyncHttpClient client;
	private CookieStore myCookieStore;
	private cn.fanfan.common.MyProgressDialog progressDialog;
	private int attach_id;
	private float offset = 0;
	private int currIndex = 0;
	private int bmpW;
	private int itemcount = 3;
	private static Uri photoUri;
	private ViewPager viewPager;
	private List<Fragment> listViews; 
	private ImageView cursor;
	private TextView t1, t2, t3;
	Fragment text1;
	Fragment text2;
	Fragment text3;
	private ActionBar actionBar;
	private static String attach_access_key;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.quespagr);
		actionBar = getActionBar();
		actionBar.setIcon(null);
		actionBar.setTitle("提问");
		actionBar.setDisplayUseLogoEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.show();
		text1 = new QuestionFragment();
		text2 = new DetailFragment();
		text3 = new TagFragment();
		cursor = (ImageView) findViewById(R.id.cursor);
		attach_access_key = md5(getAttachKey());
		client = new AsyncHttpClient();
		myCookieStore = new PersistentCookieStore(this);
		client.setCookieStore(myCookieStore);
		progressDialog = new MyProgressDialog(this, "正在上传", "稍等...", false);
		InitTextView();
		InitImageView();
		InitViewPager();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.quespub, menu);
		return super.onCreateOptionsMenu(menu);
	}

	private void InitTextView() {
		t1 = (TextView) findViewById(R.id.text1);
		t2 = (TextView) findViewById(R.id.text2);
		t3 = (TextView) findViewById(R.id.text3);

		t1.setOnClickListener(new MyOnClickListener(0));
		t2.setOnClickListener(new MyOnClickListener(1));
		t3.setOnClickListener(new MyOnClickListener(2));
	}

	public class MyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			viewPager.setCurrentItem(index);
		}
	};


	private void InitImageView() {
		Bitmap pic = BitmapFactory.decodeResource(getResources(), R.drawable.a);
		bmpW = pic.getWidth();
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		float screenW = dm.widthPixels;
		offset = (screenW / itemcount - bmpW) / 2;
		Matrix matrix = new Matrix();
		;
		matrix.postTranslate(offset, 0);
		cursor.setImageMatrix(matrix);
	}

	private void InitViewPager() {
		viewPager = (ViewPager) findViewById(R.id.pager);
		listViews = new ArrayList<Fragment>();
		listViews.add(text1);
		listViews.add(text2);
		listViews.add(text3);
		viewPager.setAdapter(new MyPageAdapter(getSupportFragmentManager(),
				listViews));
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	public class MyPageAdapter extends FragmentPagerAdapter {
		public List<Fragment> mListViews;

		public MyPageAdapter(FragmentManager fm, List<Fragment> listViews) {
			// TODO Auto-generated constructor stub
			super(fm);
			this.mListViews = listViews;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mListViews.size();
		}

		@Override
		public Fragment getItem(int arg0) {
			// TODO Auto-generated method stub
			Fragment fragment = mListViews.get(arg0);
			return fragment;
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		case R.id.publish:
			try {
				String quesdetil = ((DetailFragment) text2).getTextString()
						.getText().toString();
				String question = ((QuestionFragment) text1).getTextString().getText()
						.toString();
				String questag = ((TagFragment) text3).getTextString().getText()
						.toString();
				RequestParams params = new RequestParams();
				params.put("question_content", question);
				params.put("question_detail", quesdetil);
				params.put("attach_access_key", attach_access_key);
				
				params.put("topics", questag);
				quespost(params);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				Toast.makeText(this, "请完善内容", Toast.LENGTH_LONG).show();
				;
			}

			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@SuppressLint("SimpleDateFormat")
	private String getAttachKey() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"'IMG'yyyyMMddHHmmss");
		return dateFormat.format(date) + Math.random() * 100;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch (requestCode) {
		case 0:
			if (resultCode != RESULT_CANCELED && null != data) {
				Uri uri = data.getData();
				String[] pojo = { MediaStore.Images.Media.DATA };
				Cursor cursor = getContentResolver().query(uri, pojo, null,
						null, null);
				String picPath = null;
				if (cursor != null) {
					int columnIndex = cursor.getColumnIndex(pojo[0]);
					cursor.moveToFirst();
					picPath = cursor.getString(columnIndex);
					cursor.close();
				}
				File photopic = new File(picPath);
				photoUri = Uri.fromFile(photopic);
				picpost(photoUri);

			} else {
				Toast.makeText(this, "选择图片失败", Toast.LENGTH_LONG).show();
			}
			break;
		case 1:
			if (resultCode != RESULT_CANCELED) {
				photoUri = ((DetailFragment) text2).getUri();
				// startPhotoZoom(photoUri);
				if (photoUri != null) {
					picpost(photoUri);
				} else {
					Toast.makeText(this, "拍照失败", Toast.LENGTH_LONG).show();
				}

			} else {
				Toast.makeText(this, "拍照失败", Toast.LENGTH_LONG).show();
			}
			break;
		default:
			break;
		}

	}

	private void picpost(Uri photoUri) {
		File photo = null;
		try {
			photo = new File(Bimp.revitionImageSize(photoUri.getPath()));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		RequestParams params = new RequestParams();

		try {
			params.put("qqfile", photo);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		postpic(params, "question", attach_access_key);

	}

	public void postpic(RequestParams params, String id, String attach) {

		String url = Config.getValue("PostPic") + "?id=" + id
				+ "&attach_access_key=" + attach;
		client.post(url, params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				JSONObject jsonObject = new JSONObject();
				JSONObject rsm = new JSONObject();
				int errno = 0;
				String err = null;
				String result = new String(arg2);
				try {
					jsonObject = new JSONObject(result);
					errno = jsonObject.getInt("errno");
					err = jsonObject.getString("err");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (errno == 1) {
					Toast.makeText(QuestionFragmentActivity.this, "上传成功", Toast.LENGTH_LONG)
							.show();
					try {
						rsm = jsonObject.getJSONObject("rsm");
						attach_id = rsm.getInt("attach_id");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					((DetailFragment) text2).getTextString().append(
							"[attach]" + attach_id + "[/attach]");
					((DetailFragment) text2).showpic(photoUri.getPath());
				}
				if (errno == -1) {
					Toast.makeText(QuestionFragmentActivity.this, err, Toast.LENGTH_LONG)
							.show();
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				// TODO Auto-generated method stub

			}
		});

	}

	public void quespost(RequestParams params) {
		String url = Config.getValue("PostQuestion");
		client.post(url, params, new AsyncHttpResponseHandler() {

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				progressDialog.show();
				super.onStart();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				JSONObject jsonObject = null;
				String errno = null;
				String err = null;
				String result = new String(arg2);
				try {
					jsonObject = new JSONObject(result);
					errno = jsonObject.getString("errno");
					err = jsonObject.getString("err");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				progressDialog.hideAndCancle();
				if (errno.equals("1")) {
					Toast.makeText(QuestionFragmentActivity.this, "发布成功", Toast.LENGTH_LONG)
							.show();
					attach_access_key = md5(getAttachKey());
					try {
						JSONObject rsm = jsonObject.getJSONObject("rsm");
						String question_id = rsm.getString("question_id");
						Intent intent = new Intent();
						intent.putExtra("questionid", question_id);
						intent.setClass(QuestionFragmentActivity.this, DetailQuestionActivity.class);
						startActivity(intent);
						Thread.currentThread();
						Thread.sleep(1000);
						finish();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (errno.equals("-1")) {
					Toast.makeText(QuestionFragmentActivity.this, err, Toast.LENGTH_LONG)
							.show();
				}

			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				// TODO Auto-generated method stub

			}
		});
	}

	public static String md5(String string) {

		byte[] hash;

		try {

			hash = MessageDigest.getInstance("MD5").digest(
					string.getBytes("UTF-8"));

		} catch (NoSuchAlgorithmException e) {

			throw new RuntimeException("Huh, MD5 should be supported?", e);

		} catch (UnsupportedEncodingException e) {

			throw new RuntimeException("Huh, UTF-8 should be supported?", e);

		}

		StringBuilder hex = new StringBuilder(hash.length * 2);

		for (byte b : hash) {

			if ((b & 0xFF) < 0x10)
				hex.append("0");

			hex.append(Integer.toHexString(b & 0xFF));

		}

		return hex.toString();

	}

	public class MyOnPageChangeListener implements OnPageChangeListener {

		float one = offset * 2 + bmpW;

		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
			Animation animation = null;
			if (currIndex == 0) {
				animation = new TranslateAnimation(offset, one * arg0, 0, 0);
			} else {
				animation = new TranslateAnimation(one * currIndex, one * arg0,
						0, 0);
			}
			currIndex = arg0;
			animation.setFillAfter(true);// True:ͼƬͣ�ڶ�������λ��
			animation.setDuration(300);
			cursor.startAnimation(animation);

		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

	}

	protected void onDestroy() {

		FileUtils.deleteDir(FileUtils.SDPATH);
		FileUtils.deleteDir(FileUtils.SDPATH1);
		// ����ͼƬ����
		super.onDestroy();
	}

}
