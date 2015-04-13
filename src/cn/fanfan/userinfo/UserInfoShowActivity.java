package cn.fanfan.userinfo;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import bean.User;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.umeng.analytics.MobclickAgent;

import cn.fanfan.attentionuser.AttentionUserActivity;
import cn.fanfan.common.Config;
import cn.fanfan.common.FanfanSharedPreferences;
import cn.fanfan.common.GlobalVariables;
import cn.fanfan.common.NetworkState;
import cn.fanfan.common.image.SmartImageView;
import cn.fanfan.main.MainActivity;
import cn.fanfan.main.R;
import cn.fanfan.topic.TopicFragmentActivity;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

//逻辑实现：获取其他Activity传递的值》根据值初始化界面》从网络获取数据》解析数据并设置到对应的bean》将数据填充到界面
public class UserInfoShowActivity extends Activity implements OnClickListener {
	// 当前用户是否已经关注该用户
	private int haveFrocus = NO;// 1是已关注 。0未关注
	private static final int YES = 1;
	private static final int NO = 0;

	private SmartImageView iv_avatar;
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
	private TextView tvSignature;
	private TextView tv_focusi_person_comment, tv_ifocus_person_comment,
			tv_topic_comment;
	private String uid;
	private LinearLayout lv_topics, lv_replys, lv_search_friens, lv_news,
			lv_asks, lv_focusi_person, lv_ifocus_person, lv_articles;
	private ProgressBar pb_change_follow;
	private int status;
	private AsyncHttpClient mHttpClient;
	private FanfanSharedPreferences ffGetUid;
	private LinearLayout ll_logout;
	private FanfanSharedPreferences sharedPreferences;
	private User mUser;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_information_show);
		LinearLayout hidePart = (LinearLayout) findViewById(R.id.llHidePart);
		hidePart.setVisibility(View.GONE);
		// 添加返回按钮到ActionBar
		ActionBar actionBar = getActionBar();
		actionBar.setIcon(null);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayUseLogoEnabled(false);
		// actionBar.setDisplayShowHomeEnabled(true);
		actionBar.show();
		// Bundle bundle = intent.getExtras();
		// 获取其他activity的传进来的值。
		Intent intent = this.getIntent();
		uid = intent.getStringExtra("uid");
		status = intent
				.getIntExtra("status", GlobalVariables.DISAVAILABLE_EDIT);
		// 判断UID是不是本机上已登录用户，如果是可以编辑并隐藏关注按钮。否则，隐藏编辑按钮显示关注按钮。
		ffGetUid = new FanfanSharedPreferences(this);
		if (uid.equals(ffGetUid.getUid(""))) {
			status = GlobalVariables.AVAILABLE_EDIT;
		}
		init();// 初始化界面
		// 获取网络状态，根据网络状态操作
		if (uid != null) {
			NetworkState networkState = new NetworkState();
			if (networkState.isNetworkConnected(UserInfoShowActivity.this)) {
				getUserInfo();
			} else {
				Toast.makeText(UserInfoShowActivity.this, "无网络，请设置后重试！",
						Toast.LENGTH_LONG).show();
			}

		}
	}

	/**
	 * @param context
	 *            所在要启动的activity
	 * @param uid
	 *            需要查看信息的用户uid，
	 * @param status
	 *            是否是本机已登录用户
	 */
	public static void actionStar(Context context, String uid) {
		Intent mIntent = new Intent(context, UserInfoShowActivity.class);
		mIntent.putExtra("uid", uid);
		context.startActivity(mIntent);
	}

	// 初始化界面
	private void init() {
		// TODO Auto-generated method stub
		iv_avatar = (SmartImageView) findViewById(R.id.iv_avatar);
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

		lv_news = (LinearLayout) findViewById(R.id.lv_news);
		lv_news.setOnClickListener(this);

		lv_search_friens = (LinearLayout) findViewById(R.id.lv_search_friens);
		lv_search_friens.setOnClickListener(this);

		bt_focus = (Button) findViewById(R.id.bt_focus);
		bt_focus.setOnClickListener(this);
		tv_focusi_person_comment = (TextView) findViewById(R.id.tv_focusi_person_comment);
		tv_ifocus_person_comment = (TextView) findViewById(R.id.tv_ifocus_person_comment);
		tv_topic_comment = (TextView) findViewById(R.id.tv_topic_comment);
		tvSignature = (TextView) findViewById(R.id.tvSignature);
		pb_change_follow = (ProgressBar) findViewById(R.id.pb_change_follow);
		ll_logout = (LinearLayout) findViewById(R.id.ll_logout);
		ll_logout.setOnClickListener(this);
		// 判断本机上已登录用户，如果是可以编辑并隐藏关注按钮。否则，隐藏编辑按钮显示关注按钮。
		if (status == GlobalVariables.AVAILABLE_EDIT) {
			bt_focus.setVisibility(View.INVISIBLE);
			ll_logout.setVisibility(View.VISIBLE);
		} else {
			ll_logout.setVisibility(View.GONE);
			tv_focusi_person_comment.setText("关注他的人");
			tv_ifocus_person_comment.setText("他关注的人");
			tv_topic_comment.setText("他关注的话题");
		}
		if (haveFrocus == YES) {
			bt_focus.setBackgroundResource(R.drawable.btn_silver_normal);
			bt_focus.setTextColor(android.graphics.Color.BLACK);
			bt_focus.setText("取消关注");
		}
	}

	// 获取用户数据,并解析
	private void getUserInfo() {
		if (mHttpClient == null) {
			mHttpClient = new AsyncHttpClient();
		}
		PersistentCookieStore mCookieStore = new PersistentCookieStore(this);
		mHttpClient.setCookieStore(mCookieStore);
		RequestParams params = new RequestParams();
		params.put("uid", uid);
		mHttpClient.get(Config.getValue("UserInfoUrl"), params,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] responseBody) {
						// get请求成功后处理json。
						String result = new String(responseBody);
						parseData(result);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							byte[] responseBody, Throwable error) {
						// TODO Auto-generated method stub
						Toast.makeText(UserInfoShowActivity.this,
								"无法获取数据，请重试！", Toast.LENGTH_LONG).show();
					}
				});
	}

	protected void parseData(String result) {
		// TODO Auto-generated method stub
		try {
			JSONObject allResult = new JSONObject(result);
			if (allResult.getInt("errno") == -1) {
				Toast.makeText(this, allResult.getString("err"),
						Toast.LENGTH_SHORT).show();
			} else {
				JSONObject rsm = allResult.getJSONObject("rsm");
				mUser = new Gson().fromJson(rsm.toString(), User.class);
				updateUI();
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("UserInfo ERROR:", "parse data error!");
		}
	}

	// 获取数据并解析后更新界面
	protected void updateUI() {
		if (mUser != null) {
			tv_username.setText(mUser.getUser_name());
			tv_focusi_person.setText(mUser.getFans_count());
			tv_ifocus_person.setText(mUser.getFriend_count());
			tv_topic.setText(mUser.getTopic_focus_count());
			tv_votes.setText(mUser.getAgree_count());
			tv_thanks.setText(mUser.getThanks_count());
			tv_collect.setText(mUser.getAnswer_favorite_count());
			tv_replys.setText(mUser.getAnswer_count());
			tv_asks.setText(mUser.getQuestion_count());
		}
		if (!TextUtils.isEmpty(mUser.getAvatar_file())) {
			String url = Config.getValue("AvatarPrefixUrl")
					+ mUser.getAvatar_file();
			iv_avatar.setImageUrl(url);
		} else {
			iv_avatar.setImageResource(R.drawable.ic_avatar_default);
		}
		if (haveFrocus == YES) {
			bt_focus.setBackgroundResource(R.drawable.btn_silver_normal);
			bt_focus.setTextColor(android.graphics.Color.BLACK);
			bt_focus.setText("取消关注");
		} else {
			bt_focus.setBackgroundResource(R.drawable.btn_green_normal);
			bt_focus.setTextColor(android.graphics.Color.WHITE);
			bt_focus.setText("关注");
		}
	}

	// 所有主界面元素的监听事件的处理
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ll_logout:
			// 退出登录
			sharedPreferences = new FanfanSharedPreferences(
					UserInfoShowActivity.this);
			sharedPreferences.clear();
			PersistentCookieStore cookieStore = new PersistentCookieStore(
					UserInfoShowActivity.this);
			cookieStore.clear();
			Intent mainIntent = new Intent(this, MainActivity.class);
			mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(mainIntent);
			break;
		case R.id.lv_topics:
			Intent intent = new Intent(UserInfoShowActivity.this,
					TopicFragmentActivity.class);
			intent.putExtra("uid", uid);
			startActivity(intent);
			break;
		case R.id.lv_focusi_person:
			Intent intent2 = new Intent(UserInfoShowActivity.this,
					AttentionUserActivity.class);
			intent2.putExtra("userorme", GlobalVariables.ATTENEION_ME);
			intent2.putExtra("uid", uid);
			startActivity(intent2);
			break;
		case R.id.lv_ifocus_person:
			Intent intent1 = new Intent(UserInfoShowActivity.this,
					AttentionUserActivity.class);
			intent1.putExtra("uid", uid);
			intent1.putExtra("userorme", GlobalVariables.ATTENTION_USER);
			startActivity(intent1);
			break;
		case R.id.lv_articles:
			Intent intent3 = new Intent(UserInfoShowActivity.this,
					ArticleActivity.class);
			intent3.putExtra("isArticle", GlobalVariables.ARTICLE);
			intent3.putExtra("uid", uid);
			startActivity(intent3);
			break;
		case R.id.lv_asks:
			Intent intent4 = new Intent(UserInfoShowActivity.this,
					ArticleActivity.class);
			intent4.putExtra("isArticle", GlobalVariables.QUESTION);
			intent4.putExtra("uid", uid);
			startActivity(intent4);
			break;
		case R.id.lv_news:
			Toast.makeText(UserInfoShowActivity.this, "lv_news",
					Toast.LENGTH_SHORT).show();
			break;
		case R.id.lv_search_friens:
			Toast.makeText(UserInfoShowActivity.this, "lv_search_friens",
					Toast.LENGTH_SHORT).show();
			break;
		case R.id.lv_replys:
			Intent intent5 = new Intent(UserInfoShowActivity.this,
					MyAnswerActivity.class);
			intent5.putExtra("uid", uid);
			startActivity(intent5);
			break;
		case R.id.bt_focus:
			if (haveFrocus == YES) {
				haveFrocus = NO;
				bt_focus.setBackgroundResource(R.drawable.btn_green_normal);
				bt_focus.setTextColor(android.graphics.Color.WHITE);
				bt_focus.setText("关注");
			} else {
				haveFrocus = YES;
				bt_focus.setBackgroundResource(R.drawable.btn_silver_normal);
				bt_focus.setTextColor(android.graphics.Color.BLACK);
				bt_focus.setText("取消关注");
			}
			pb_change_follow.setVisibility(View.VISIBLE);
			changeFrocusStatus();
			bt_focus.setClickable(false);
			break;
		default:
			break;
		}
	}

	private void changeFrocusStatus() {
		// TODO Auto-generated method stub
		mHttpClient = new AsyncHttpClient();
		PersistentCookieStore mCookieStore = new PersistentCookieStore(this);
		mHttpClient.setCookieStore(mCookieStore);
		RequestParams followStatus = new RequestParams();
		// 发送关注状态，如果失败提醒用户，并更改frocus按钮相关状态
		followStatus.put("uid", uid);// 所要取消关注的uid
		mHttpClient.get(Config.getValue("ChangeFollowStatus"), followStatus,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1,
							byte[] responseBody) {
						// TODO Auto-generated method stub
						// String responseContent = new String(responseBody);
						bt_focus.setClickable(true);
						pb_change_follow.setVisibility(View.GONE);
					}

					@Override
					public void onFailure(int arg0, Header[] arg1,
							byte[] responseBody, Throwable arg3) {
						// TODO Auto-generated method stub
						String responseContent = new String(responseBody);
						Toast.makeText(UserInfoShowActivity.this,
								responseContent + "关注失败，请重试！",
								Toast.LENGTH_SHORT).show();
						// 更改按钮状态
						bt_focus.setClickable(true);
						if (haveFrocus == YES) {
							haveFrocus = NO;
							bt_focus.setBackgroundResource(R.drawable.btn_green_normal);
							bt_focus.setTextColor(android.graphics.Color.WHITE);
							bt_focus.setText("关注");
						} else {
							haveFrocus = YES;
							bt_focus.setBackgroundResource(R.drawable.btn_silver_normal);
							bt_focus.setTextColor(android.graphics.Color.BLACK);
							bt_focus.setText("取消关注");
						}
					}
				});
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	// 重新返回时再次获取用户信息
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		if (uid != null) {
			NetworkState networkState = new NetworkState();
			if (networkState.isNetworkConnected(UserInfoShowActivity.this)) {
				getUserInfo();
			} else {
				Toast.makeText(UserInfoShowActivity.this, "无网络，请设置后重试！",
						Toast.LENGTH_LONG).show();
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
			Intent intent = new Intent(UserInfoShowActivity.this,
					UserInfoEditActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("uid", uid);
			bundle.putString("avatar_file", mUser.getAvatar_file());
			intent.putExtras(bundle);
			startActivity(intent);
			return true;
		}
		if (id == android.R.id.home) {
			this.finish();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if (mHttpClient != null) {
			mHttpClient.cancelAllRequests(true);
		}

	}

}
