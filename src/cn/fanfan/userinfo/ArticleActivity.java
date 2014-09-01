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

import cn.fanfan.common.Config;
import cn.fanfan.common.GlobalVariables;
import cn.fanfan.detailessay.DetailEssayActivity;
import cn.fanfan.detailquestion.DetailQuestionActivity;
import cn.fanfan.main.R;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class ArticleActivity extends Activity {
	private String TAG = "ArticleActivity";
	private ArrayList<ArticleModel> datas;
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
	private ArticleAdapter adapter;
	private int isArticle;

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
		init();
		datas = new ArrayList<ArticleModel>();
		listView = (ListView) findViewById(R.id.draft_lisview);
		footerLinearLayout = (LinearLayout) LayoutInflater.from(
				ArticleActivity.this).inflate(R.layout.next_page_footer, null);
		footText = (TextView) footerLinearLayout.findViewById(R.id.footer_text);
		listView.addFooterView(footerLinearLayout, "aa", false);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				if (isArticle == GlobalVariables.ARTICLE) {
					intent.setClass(ArticleActivity.this, DetailEssayActivity.class);
					intent.putExtra("eid", datas.get(arg2).getId());
				} else {
					intent.setClass(ArticleActivity.this, DetailQuestionActivity.class);
					intent.putExtra("questionid", datas.get(arg2).getId());
				}
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
						// getInformation(String.valueOf(currentPage));
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
		isArticle = intent.getIntExtra("isArticle", 0);
	}

	private void setData() {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams();
		params.put("uid", uid);
		params.put("page", String.valueOf(currentPage));
		params.put("perpage", String.valueOf(perPage));
		AsyncHttpClient client = new AsyncHttpClient();
		String url;
		if (isArticle == GlobalVariables.ARTICLE) {
			url = Config.getValue("MyAyticle");
		} else {
			url = Config.getValue("MyAsk");
		}
		client.get(url, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				String information = new String(arg2);
				try {
					JSONObject jsonObject = new JSONObject(information);
					int errno = jsonObject.getInt("errno");
					if (errno != 1) {
						String err = jsonObject.getString("err");
						Toast.makeText(ArticleActivity.this, err,
								Toast.LENGTH_SHORT).show();
					} else {
						String rsm = jsonObject.getString("rsm");
						jsonObject = new JSONObject(rsm);
						String total_rows = jsonObject.getString("total_rows");
						if (currentPage == 1) {
							int total_row = Integer.parseInt(total_rows);
							if (total_row % perPage == 0) {
								total_pages = total_row / perPage;
							} else {
								total_pages = total_row / perPage + 1;
							}
						}
						String rows = jsonObject.getString("rows");
						JSONArray jsonArray = new JSONArray(rows);
						for (int i = 0; i < jsonArray.length(); i++) {
							ArticleModel model = new ArticleModel();
							JSONObject jsonObject2 = jsonArray.getJSONObject(i);
							String id = jsonObject2.getString("id");
							String title = jsonObject2.getString("title");
							String message;
							if (isArticle == GlobalVariables.ARTICLE) {
								message = jsonObject2.getString("message");
							} else {
								message = jsonObject2.getString("detail");
							}
							model.setId(id);
							model.setTitle(title);
							model.setMessage(message);
							datas.add(model);
						}
						if (currentPage == 1) {
							if (Integer.parseInt(total_rows) < perPage) {
								footText.setText("没有更多数据了！");
								footerLinearLayout.setVisibility(View.GONE);
							}
							adapter = new ArticleAdapter(ArticleActivity.this,
									datas);
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