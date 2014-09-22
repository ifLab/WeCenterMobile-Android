package cn.fanfan.detilessay;

import java.util.Date;

import org.apache.http.Header;
import org.apache.http.client.CookieStore;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

import cn.fanfan.common.Config;
import cn.fanfan.common.TextShow;
import cn.fanfan.detilques.Answer;
import cn.fanfan.detilques.ComList;
import cn.fanfan.detilques.Detilques;
import cn.fanfan.detilques.TopicAbout;
import cn.fanfan.main.R;
import cn.fanfan.topic.imageload.ImageDownLoader;
import cn.fanfan.topic.imageload.ImageDownLoader.onImageLoaderListener;
import cn.fanfan.userinfo.UserInfoActivity;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DetilEssay extends Activity implements OnClickListener {
	private CookieStore myCookieStore;
	private AsyncHttpClient client;
	private TextView agree, name, sign;
	private TextView disagree, arttitle, artdetail;
	private TextView addcom, time;
	private ImageView userimage;
	private int tagagree = 0;
	private String articleid;
	private LinearLayout layout;
	private String uid;

	// private ComListView listView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detilessay);
		ActionBar actionBar = getActionBar();
		actionBar.setIcon(null);
		actionBar.setTitle("文章详细");
		actionBar.setDisplayUseLogoEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.show();
		client = new AsyncHttpClient();
		myCookieStore = new PersistentCookieStore(this);
		client.setCookieStore(myCookieStore);
		Intent intent = getIntent();
		articleid = intent.getStringExtra("eid");
		//articleid = "2";
		layout =(LinearLayout)findViewById(R.id.linktopic);
		agree = (TextView) findViewById(R.id.agree);
		disagree = (TextView) findViewById(R.id.disagree);
		addcom = (TextView) findViewById(R.id.addcom);
		name = (TextView) findViewById(R.id.username);
		sign = (TextView) findViewById(R.id.usersign);
		userimage = (ImageView) findViewById(R.id.userimage);
		arttitle = (TextView) findViewById(R.id.title);
		artdetail = (TextView) findViewById(R.id.essaydetil);
		//time = (TextView) findViewById(R.id.time);
		agree.setOnClickListener(this);
		disagree.setOnClickListener(this);
		addcom.setOnClickListener(this);
		userimage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent1 = new Intent();
				intent1.putExtra("uid", uid);
				intent1.setClass(DetilEssay.this,UserInfoActivity.class);
				startActivity(intent1);
			}
		});
		Getinfo();
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {

		case R.id.agree:
			if (tagagree == 0 ||tagagree == -1) {
				dozan(1,1);
			} else {
                dozan(0,1);
			}
		     break;
		case R.id.disagree:
			if (tagagree == 0 || tagagree == 1) {
				dozan(-1,-1);
			} else {
                dozan(0,-1);
			}
		     break;
		case R.id.addcom:
			Intent intent = new Intent();
			intent.putExtra("artid", articleid);
			intent.setClass(this, EssayCom.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
	private void zanno(int id){
		switch (id) {
		case 1:

			switch (tagagree) {
			case 1:
				agree.setText(String.valueOf(Integer.valueOf(agree.getText().toString())-1));
				tagagree = 0;
				zanorno();
				break;
			case 0:
				agree.setText(String.valueOf(Integer.valueOf(agree.getText().toString())+1));
				tagagree = 1;
				zanorno();
				break;
			case -1:
				agree.setText(String.valueOf(Integer.valueOf(agree.getText().toString())+1));
				tagagree = 1;
				zanorno();
				break;

			default:
				break;
			}

			break;
		case -1:
			switch (tagagree) {
			case 1:
				agree.setText(String.valueOf(Integer.valueOf(agree.getText().toString())-1));
				tagagree = -1;
				zanorno();
				break;
			case 0:
				tagagree = -1;
				zanorno();
				break;
			case -1:
				tagagree = 0;
				zanorno();
				break;

			default:
				break;
			}
			break;

		default:
			break;
		}
	}

	private void zanorno() {
		Drawable nav_up;
		Drawable nav_down;
		switch (tagagree) {
		case 1:
			nav_up = getResources().getDrawable(R.drawable.ic_vote_checked);
			nav_up.setBounds(0, 0, nav_up.getMinimumWidth(),
					nav_up.getMinimumHeight());
			agree.setCompoundDrawables(nav_up, null, null, null);
			nav_down = getResources().getDrawable(
					R.drawable.ic_vote_down_normal);
			nav_down.setBounds(0, 0, nav_down.getMinimumWidth(),
					nav_down.getMinimumHeight());
			disagree.setCompoundDrawables(nav_down, null, null, null);
			break;
		case 0:
			nav_up = getResources().getDrawable(R.drawable.ic_vote_normal);
			nav_up.setBounds(0, 0, nav_up.getMinimumWidth(),
					nav_up.getMinimumHeight());
			agree.setCompoundDrawables(nav_up, null, null, null);
			nav_down = getResources().getDrawable(
					R.drawable.ic_vote_down_normal);
			nav_down.setBounds(0, 0, nav_down.getMinimumWidth(),
					nav_down.getMinimumHeight());
			disagree.setCompoundDrawables(nav_down, null, null, null);
			break;
		case -1:
			nav_up = getResources().getDrawable(R.drawable.ic_vote_normal);
			nav_up.setBounds(0, 0, nav_up.getMinimumWidth(),
					nav_up.getMinimumHeight());
			agree.setCompoundDrawables(nav_up, null, null, null);
			nav_down = getResources().getDrawable(
					R.drawable.ic_vote_down_checked);
			nav_down.setBounds(0, 0, nav_down.getMinimumWidth(),
					nav_down.getMinimumHeight());
			disagree.setCompoundDrawables(nav_down, null, null, null);
			break;
		default:
			break;
		}
	}

	private void Getinfo() {
		String url = "http://w.hihwei.com/?/api/article/article/?id="
				+ articleid;
		client.get(url, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				// TODO Auto-generated method stub
				Toast.makeText(DetilEssay.this, "获取失败", Toast.LENGTH_LONG)
						.show();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				String info = new String(arg2);
				try {
					JSONObject jsonObject = new JSONObject(info);
					int errno = jsonObject.getInt("errno");
					if (errno == 1) {
						JSONObject rsm = jsonObject.getJSONObject("rsm");
						JSONObject artinfo = rsm.getJSONObject("article_info");
						arttitle.setText(artinfo.getString("title"));
						String message = artinfo.getString("message");
						String votes = artinfo.getString("votes");
						uid = artinfo.getString("uid");
						agree.setText(votes);
						final String article_topics = rsm.getString("article_topics");
						layout.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								Intent intent = new Intent();
								intent.putExtra("topic", article_topics);
								intent.setClass(DetilEssay.this,
										TopicAbout.class);
								startActivity(intent);
							}
						});
						DisplayMetrics dm = new DisplayMetrics();
						getWindowManager().getDefaultDisplay().getMetrics(dm);
						float screenW = dm.widthPixels;
						TextShow show = new TextShow(JSONTokener(message),
								artdetail, DetilEssay.this, screenW);
						//show.execute();
						name.setText(artinfo.getString("user_name"));
						sign.setText(artinfo.getString("signature"));
						tagagree = artinfo.getInt("vote_value");
						zanorno();
						//Date date = new Date();
						ImageDownLoader imageDownLoader = new ImageDownLoader(
								DetilEssay.this);
						imageDownLoader.getBitmap(
								Config.getValue("userImageBaseUrl")+artinfo.getString("avatar_file"),
								new onImageLoaderListener() {

									@Override
									public void onImageLoader(Bitmap bitmap,
											String url) {
										// TODO Auto-generated method stub
										userimage.setImageBitmap(bitmap);
									}
								});

					} else {
						String err = jsonObject.getString("err");
						Toast.makeText(DetilEssay.this, err, Toast.LENGTH_LONG)
								.show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		});
	}

	private void dozan(int value,final int id) {
		String url = "http://w.hihwei.com/?/article/ajax/article_vote/";
		RequestParams params = new RequestParams();
		params.put("type", "article");
		params.put("item_id", articleid);
		params.put("rating", value);
		client.post(url, params, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				// TODO Auto-generated method stub
				Toast.makeText(DetilEssay.this, "选择失败", Toast.LENGTH_LONG)
						.show();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				String info = new String(arg2);
				System.out.println(info);
				JSONObject jsonObject = null;
				int errno = 0;
				try {
					jsonObject = new JSONObject(info);
					errno = jsonObject.getInt("errno");
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				if (errno == 1) {
					zanno(id);
					Toast.makeText(DetilEssay.this, "选择成功", Toast.LENGTH_LONG)
							.show();
				} else {

					try {
						String err = jsonObject.getString("err");
						Toast.makeText(DetilEssay.this, err, Toast.LENGTH_LONG)
								.show();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}

		});
	}

	public static String JSONTokener(String in) {
		// consume an optional byte order mark (BOM) if it exists
		if (in != null && in.startsWith("\ufeff")) {
			in = in.substring(1);
		}
		return in;
	}
	  public boolean onOptionsItemSelected(MenuItem item) {
			// TODO Auto-generated method stub
			switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				break;

			default:
				break;
			}
			return super.onOptionsItemSelected(item);
		}
}
