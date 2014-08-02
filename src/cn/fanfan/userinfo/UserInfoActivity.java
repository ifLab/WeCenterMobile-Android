package cn.fanfan.userinfo;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

import cn.fanfan.attentionuser.AttentionUser;
import cn.fanfan.common.AsyncImageGet;
import cn.fanfan.common.Config;
import cn.fanfan.common.FanfanSharedPreferences;
import cn.fanfan.common.GlobalVariables;
import cn.fanfan.common.NetworkState;
import cn.fanfan.common.TipsToast;
import cn.fanfan.main.R;
import cn.fanfan.topic.TopicActivity;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class UserInfoActivity extends Activity implements OnClickListener {
	private Boolean haveFrocus = true;
	private static TipsToast tipsToast;
	private ImageView iv_avatar;
	private Button bt_focus;
	private TextView tv_username;
	private TextView tv_topic;
	private TextView tv_ifocus_person;
	private TextView tv_focusi_person;
	private TextView tv_thanks;
	private TextView tv_votes;
	private TextView tv_collect;
	private TextView tv_replys;
	private TextView tv_asks;
	private TextView tv_articles, tv_news;
	private TextView tv_focusi_person_comment, tv_ifocus_person_comment,
			tv_topic_comment;
	private String uid;
	protected String errno;
	protected String err;
	protected String user_name;
	protected String avatar_file = "null";
	protected String fans_count;
	protected String friend_count;
	protected String question_count;
	protected String answer_count;
	protected String topic_focus_count;
	protected String agree_count;
	protected String thanks_count;
	protected String answer_favorite_count;
	private LinearLayout lv_topics, lv_replys, lv_search_friens, lv_news,
			lv_asks, lv_focusi_person, lv_ifocus_person, lv_articles;
	private int status;
	private AsyncHttpClient asyncHttpClient;
	private FanfanSharedPreferences ffGetUid;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.userinformation_main);
		// 添加返回按钮到ActionBar
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.show();
		// Bundle bundle = intent.getExtras();
		// 获取其他activity的传进来的值。
		Intent intent = this.getIntent();
		uid = intent.getStringExtra("uid");
		status = intent
				.getIntExtra("status", GlobalVariables.DISAVAILABLE_EDIT);
		// 判断UID是不是本机上已登录用户，如果是可以编辑并隐藏关注按钮。否则，隐藏编辑按钮显示关注按钮。
		ffGetUid = new FanfanSharedPreferences(this);
		Log.i("getUid", ffGetUid.getUid("nullxx"));
		Log.i("UID", uid);
		if (uid.equals(ffGetUid.getUid(""))) {
			status = GlobalVariables.AVAILABLE_EDIT;
		}
		System.out.print(status);
		init();// 初始化界面
		// 获取网络状态，根据网络状态操作
		if (uid != null) {
			NetworkState networkState = new NetworkState();
			if (networkState.isNetworkConnected(UserInfoActivity.this)) {
				getUserInfo();
			} else {
				showTips(R.drawable.tips_error, R.string.net_break);
			}

		}
	}

	// 初始化界面
	private void init() {
		// TODO Auto-generated method stub
		iv_avatar = (ImageView) findViewById(R.id.iv_avatar);
		tv_username = (TextView) findViewById(R.id.tv_username);
		lv_topics = (LinearLayout) findViewById(R.id.lv_topics);
		lv_topics.setOnClickListener(this);
		tv_topic = (TextView) findViewById(R.id.tv_topic);
		lv_ifocus_person = (LinearLayout) findViewById(R.id.lv_ifocus_person);
		lv_ifocus_person.setOnClickListener(this);
		tv_ifocus_person = (TextView) findViewById(R.id.tv_ifocus_person);
		lv_focusi_person = (LinearLayout) findViewById(R.id.lv_focusi_person);
		lv_focusi_person.setOnClickListener(this);
		tv_focusi_person = (TextView) findViewById(R.id.tv_focusi_person);

		tv_thanks = (TextView) findViewById(R.id.tv_thanks);
		tv_votes = (TextView) findViewById(R.id.tv_votes);
		tv_collect = (TextView) findViewById(R.id.tv_collect);

		lv_replys = (LinearLayout) findViewById(R.id.lv_replys);
		lv_replys.setOnClickListener(this);
		tv_replys = (TextView) findViewById(R.id.tv_replys);
		lv_asks = (LinearLayout) findViewById(R.id.lv_asks);
		lv_asks.setOnClickListener(this);
		tv_asks = (TextView) findViewById(R.id.tv_asks);
		lv_articles = (LinearLayout) findViewById(R.id.lv_articles);
		lv_articles.setOnClickListener(this);
		tv_articles = (TextView) findViewById(R.id.tv_articles);

		lv_news = (LinearLayout) findViewById(R.id.lv_news);
		lv_news.setOnClickListener(this);
		tv_news = (TextView) findViewById(R.id.tv_news);

		lv_search_friens = (LinearLayout) findViewById(R.id.lv_search_friens);
		lv_search_friens.setOnClickListener(this);

		bt_focus = (Button) findViewById(R.id.bt_focus);
		bt_focus.setOnClickListener(this);
		tv_focusi_person_comment = (TextView) findViewById(R.id.tv_focusi_person_comment);
		tv_ifocus_person_comment = (TextView) findViewById(R.id.tv_ifocus_person_comment);
		tv_topic_comment = (TextView) findViewById(R.id.tv_topic_comment);
		// 判断本机上已登录用户，如果是可以编辑并隐藏关注按钮。否则，隐藏编辑按钮显示关注按钮。
		if (status == GlobalVariables.AVAILABLE_EDIT) {
			bt_focus.setVisibility(View.INVISIBLE);
		} else {
			tv_focusi_person_comment.setText("关注他的人");
			tv_ifocus_person_comment.setText("他关注的人");
			tv_topic_comment.setText("他关注的话题");
		}
		if (haveFrocus) {
			bt_focus.setBackgroundResource(R.drawable.btn_silver_normal);
			bt_focus.setTextColor(android.graphics.Color.BLACK);
			bt_focus.setText("取消关注");
		}
	}

	// 获取用户数据,并解析
	private void getUserInfo() {
		// TODO Auto-generated method stub
		AsyncHttpClient getUserInfo = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("uid", uid);
		getUserInfo.get(Config.getValue("UserInfoUrl"), params,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] responseBody) {
						// TODO Auto-generated method stub
						// get请求成功后处理json。
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
							JSONObject rsm = new JSONObject();
							rsm = result.getJSONObject("rsm");
							JSONTokener jsonParser2 = new JSONTokener(rsm
									.toString());
							JSONObject rsmcontent = (JSONObject) jsonParser2
									.nextValue();
							user_name = rsmcontent.getString("user_name");
							avatar_file = rsmcontent.getString("avatar_file");
							Log.i("avatar_file", avatar_file);
							fans_count = rsmcontent.getString("fans_count");
							friend_count = rsmcontent.getString("friend_count");
							question_count = rsmcontent
									.getString("question_count");
							topic_focus_count = rsmcontent
									.getString("topic_focus_count");
							agree_count = rsmcontent.getString("agree_count");
							thanks_count = rsmcontent.getString("thanks_count");
							answer_favorite_count = rsmcontent
									.getString("answer_favorite_count");
							answer_count = rsmcontent.getString("answer_count");
							Log.i("answer_favorite_count",
									answer_favorite_count);

							// 处理完JSON后updateUI展示用户资料
							updateUI(avatar_file);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();

						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							byte[] responseBody, Throwable error) {
						// TODO Auto-generated method stub
						showTips(R.drawable.tips_error, R.string.get_user_info);
					}
				});
	}

	// 获取数据并解析后更新界面
	protected void updateUI(String avatarurl) {
		// TODO Auto-generated method stub
		tv_username.setText(user_name);
		tv_focusi_person.setText(fans_count);
		tv_ifocus_person.setText(friend_count);
		tv_topic.setText(topic_focus_count);
		tv_votes.setText(agree_count);
		tv_thanks.setText(thanks_count);
		tv_collect.setText(answer_favorite_count);
		tv_replys.setText(answer_count);
		tv_asks.setText(question_count);
		// 下载用户头像
		if (avatarurl != "null") {
			AsyncImageGet getAvatar = new AsyncImageGet(
					Config.getValue("AvatarPrefixUrl") + avatarurl, iv_avatar);
			getAvatar.execute();
		}
	}

	// 所有主界面元素的监听事件的处理
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.lv_topics:
			Intent intent = new Intent(UserInfoActivity.this,
					TopicActivity.class);
			intent.putExtra("uid", uid);
			startActivity(intent);
			break;
		case R.id.lv_focusi_person:
			Intent intent2 = new Intent(UserInfoActivity.this,
					AttentionUser.class);
			intent2.putExtra("userorme", GlobalVariables.ATTENEION_ME);
			intent2.putExtra("uid", uid);
			startActivity(intent2);
			break;
		case R.id.lv_ifocus_person:
			Intent intent1 = new Intent(UserInfoActivity.this,
					AttentionUser.class);
			intent1.putExtra("uid", uid);
			intent1.putExtra("userorme", GlobalVariables.ATTENTION_USER);
			startActivity(intent1);
			break;
		case R.id.lv_articles:
			Toast.makeText(UserInfoActivity.this, "lv_articles",
					Toast.LENGTH_SHORT).show();
			break;
		case R.id.lv_asks:
			Toast.makeText(UserInfoActivity.this, "lv_asks", Toast.LENGTH_SHORT)
					.show();
			break;
		case R.id.lv_news:
			Toast.makeText(UserInfoActivity.this, "lv_news", Toast.LENGTH_SHORT)
					.show();
			break;
		case R.id.lv_search_friens:
			Toast.makeText(UserInfoActivity.this, "lv_search_friens",
					Toast.LENGTH_SHORT).show();
			break;
		case R.id.lv_replys:
			Toast.makeText(UserInfoActivity.this, "lv_replys",
					Toast.LENGTH_SHORT).show();
			break;
		case R.id.bt_focus:
			if (haveFrocus) {
				haveFrocus = false;
				bt_focus.setBackgroundResource(R.drawable.btn_green_normal);
				bt_focus.setTextColor(android.graphics.Color.WHITE);
				bt_focus.setText("关注");
			} else {
				haveFrocus = true;
				bt_focus.setBackgroundResource(R.drawable.btn_silver_normal);
				bt_focus.setTextColor(android.graphics.Color.BLACK);
				bt_focus.setText("取消关注");
			}
			changeFrocusStatus();
			break;
		default:
			break;
		}
	}

	private void changeFrocusStatus() {
		// TODO Auto-generated method stub
		asyncHttpClient = new AsyncHttpClient();
		PersistentCookieStore mCookieStore = new PersistentCookieStore(this);
		asyncHttpClient.setCookieStore(mCookieStore);
		RequestParams followStatus = new RequestParams();
		// 发送关注状态，如果失败提醒用户，并更改frocus按钮相关状态
		followStatus.put("uid", uid);// 所要取消关注的uid
		asyncHttpClient.get(Config.getValue("ChangeFollowStatus"),
				followStatus, new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1,
							byte[] responseBody) {
						// TODO Auto-generated method stub
						String responseContent = new String(responseBody);
						Log.i("changeFrocusStatus", responseContent
								+ "---Success");
					}

					@Override
					public void onFailure(int arg0, Header[] arg1,
							byte[] responseBody, Throwable arg3) {
						// TODO Auto-generated method stub
						String responseContent = new String(responseBody);
						Log.i("changeFrocusStatus", responseContent
								+ "---Failure");
						// 更改按钮状态
						if (haveFrocus) {
							haveFrocus = false;
							bt_focus.setBackgroundResource(R.drawable.btn_green_normal);
							bt_focus.setTextColor(android.graphics.Color.WHITE);
							bt_focus.setText("关注");
						} else {
							haveFrocus = true;
							bt_focus.setBackgroundResource(R.drawable.btn_silver_normal);
							bt_focus.setTextColor(android.graphics.Color.BLACK);
							bt_focus.setText("取消关注");
						}
					}
				});
	}

	// 提示模块
	private void showTips(int iconResId, int msgResId) {
		if (tipsToast != null) {
			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
				tipsToast.cancel();
			}
		} else {
			tipsToast = TipsToast.makeText(getApplication().getBaseContext(),
					msgResId, TipsToast.LENGTH_SHORT);
		}
		tipsToast.show();
		tipsToast.setIcon(iconResId);
		tipsToast.setText(msgResId);
	}

	// 重新返回时再次获取用户信息
	protected void onResume() {
		super.onResume();
		if (uid != null) {
			NetworkState networkState = new NetworkState();
			if (networkState.isNetworkConnected(UserInfoActivity.this)) {
				getUserInfo();
			} else {
				showTips(R.drawable.tips_error, R.string.net_break);
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (status == GlobalVariables.AVAILABLE_EDIT) {
			getMenuInflater().inflate(R.menu.userinforedit, menu);
		}

		return super.onCreateOptionsMenu(menu);
	}

	// 对ActinBar编辑按钮的处理
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.edit) {
			Intent intent = new Intent(UserInfoActivity.this,
					UserInfoEditActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("uid", uid);
			bundle.putString("avatar_file", avatar_file);
			intent.putExtras(bundle);
			startActivity(intent);
			return true;
		}
		if (id == android.R.id.home) {
			this.finish();
		}
		return super.onOptionsItemSelected(item);
	}
}
