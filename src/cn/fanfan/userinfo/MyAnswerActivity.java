package cn.fanfan.userinfo;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.umeng.analytics.MobclickAgent;

import cn.fanfan.detilques.Answer;
import cn.fanfan.main.R;
import cn.fanfan.topic.BestAnswerModel;
import cn.fanfan.topic.TopicDetailSecondAdapter;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

public class MyAnswerActivity extends Activity {
	private String TAG = "AnswerId";
	private ListView listView;
	private int mFirstVisibleItem;
	private int mVisibleItemCount;
	private int totalItem;
	private int currentPage = 1;
	private LinearLayout footerLinearLayout;
	private TextView footText;
	private String uid;
	private int perPage = 10;
	private int total_pages = 1;
	private ArrayList<BestAnswerModel> datas;
	private TopicDetailSecondAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.draft);
		ActionBar actionBar = getActionBar();
		actionBar.setIcon(null);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayUseLogoEnabled(false);
		// actionBar.setDisplayShowHomeEnabled(true);
		actionBar.show();
		datas = new ArrayList<BestAnswerModel>();
		listView = (ListView) findViewById(R.id.draft_lisview);
		footerLinearLayout = (LinearLayout) LayoutInflater.from(
				MyAnswerActivity.this).inflate(R.layout.next_page_footer, null);
		footText = (TextView) footerLinearLayout.findViewById(R.id.footer_text);
		listView.addFooterView(footerLinearLayout, "aa", false);
		listView.setDividerHeight(10);
		init();
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MyAnswerActivity.this, Answer.class);
				intent.putExtra("answerid",
						String.valueOf(datas.get(arg2).getAnswer_id()));
				startActivity(intent);
			}
		});
		listView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
						&& (mFirstVisibleItem + mVisibleItemCount == totalItem)) {
					if (currentPage <= total_pages) {
						setData();
					} else {
						footText.setText("没有更多数据了!");
						footerLinearLayout.setVisibility(View.GONE);
					}
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				mFirstVisibleItem = firstVisibleItem;
				mVisibleItemCount = visibleItemCount;
				totalItem = totalItemCount;
			}
		});
		setData();
	}

	private void init() {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
		uid = intent.getStringExtra("uid");
	}

	private void setData() {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams();
		params.put("uid", uid);
		params.put("page", String.valueOf(currentPage));
		params.put("perpage", String.valueOf(perPage));
		AsyncHttpClient client = new AsyncHttpClient();
		client.get("http://w.hihwei.com/api/my_answer.php", params,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						// TODO Auto-generated method stub
						String information = new String(arg2);
						System.out.println("success");
						try {
							JSONObject jsonObject = new JSONObject(information);
							int errno = jsonObject.getInt("errno");
							if (errno != 1) {
								String err = jsonObject.getString("err");
								Toast.makeText(MyAnswerActivity.this, err,
										Toast.LENGTH_SHORT).show();
							} else {
								String rsm = jsonObject.getString("rsm");
								jsonObject = new JSONObject(rsm);
								String total_rows = jsonObject
										.getString("total_rows");
								if (currentPage == 1) {
									int total_row = Integer
											.parseInt(total_rows);
									if (total_row % perPage == 0) {
										total_pages = total_row / perPage;
									} else {
										total_pages = total_row / perPage + 1;
									}
								}
								String rows = jsonObject.getString("rows");
								JSONArray jsonArray = new JSONArray(rows);
								for (int i = 0; i < jsonArray.length(); i++) {
									BestAnswerModel data = new BestAnswerModel();
									JSONObject jsonObject2 = jsonArray
											.getJSONObject(i);
									String answer_id = jsonObject2
											.getString("answer_id");
									String question_id = jsonObject2
											.getString("question_id");
									String answer_content = jsonObject2
											.getString("answer_content");
									String agree_count = jsonObject2
											.getString("agree_count");
									String avatar_file = jsonObject2
											.getString("avatar_file");
									String question_title = jsonObject2
											.getString("question_title");
									data.setQuestion_id(Integer
											.valueOf(question_id));
									data.setAnswer_id(Integer
											.valueOf(answer_id));
									data.setAnswer_content(answer_content);
									data.setAgree_count(Integer
											.parseInt(agree_count));
									data.setAvatar_file(avatar_file);
									data.setQuestion_content(question_title);
									datas.add(data);
								}
								if (currentPage == 1) {
									if (Integer.parseInt(total_rows) < perPage) {
										footText.setText("没有更多数据了！");
										footerLinearLayout
												.setVisibility(View.GONE);
									}
									adapter = new TopicDetailSecondAdapter(
											MyAnswerActivity.this, datas);
									listView.setAdapter(adapter);
									currentPage++;
								} else {
									adapter.notifyDataSetChanged();
									currentPage++;
								}
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
						System.out.println("faliure");
						System.out.println(new String(arg2));
					}
				});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		if (item.getItemId() == android.R.id.home) {
			this.finish();
		}
		return super.onOptionsItemSelected(item);
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