package cn.fanfan.detilessay;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.client.CookieStore;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.fanfan.common.Config;
import cn.fanfan.main.R;
import cn.fanfan.topic.imageload.ImageDownLoader;
import cn.fanfan.topic.imageload.ImageDownLoader.onImageLoaderListener;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.umeng.analytics.MobclickAgent;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

public class EssayCom extends Activity implements OnItemClickListener,
		OnScrollListener {

	private AsyncHttpClient client;
	private int totalItem;
	private CookieStore myCookieStore;
	private boolean isFirstEnter;
	private int mFirstVisibleItem;
	private int mVisibleItemCount;
	private Dialog aDialog;
	private ListView comlist;
	private ImageDownLoader imageDownLoader;
	private List<EssatComitem> comitems;
	private EssayComAdapter comAdapter;
	private String id;
	private EditText comment;
	private ImageButton publish;
	private String atuid = null;
	private int voteval = 0;
	private Button zan;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comlist);

		ActionBar actionBar = getActionBar();
		actionBar.setIcon(null);
		actionBar.setTitle("文章评论");
		actionBar.setDisplayUseLogoEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.show();
		Intent intent = getIntent();
		id = intent.getStringExtra("artid");
		client = new AsyncHttpClient();
		myCookieStore = new PersistentCookieStore(this);
		client.setCookieStore(myCookieStore);
		comment = (EditText) findViewById(R.id.comment);
		publish = (ImageButton) findViewById(R.id.publish);
		publish.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				RequestParams params = new RequestParams();
				params.put("article_id", id);
				params.put("message", comment.getText().toString());
				params.put("at_uid", atuid);
				postcom(params);
				comment.setText("");
				refresh();
			}
		});
		comitems = new ArrayList<EssatComitem>();
		imageDownLoader = new ImageDownLoader(this);
		comlist = (ListView) findViewById(R.id.comlist);
		isFirstEnter = true;
		comlist.setOnItemClickListener(this);
		comlist.setOnScrollListener(this);

		Getcom(id);

	}
	public boolean checkKeyboardShowing(){
		comlist.requestFocus();
		InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
		boolean active = imm.isActive(comment);
		imm.hideSoftInputFromWindow(comment.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		
		comment.clearFocus();
		return active;
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		aDialog = new Dialog(this);
		LayoutInflater inflater = LayoutInflater.from(this);
		View view = inflater.inflate(R.layout.dialog, null);
	    zan = (Button) view.findViewById(R.id.zan);
		Button back = (Button) view.findViewById(R.id.backanswer);
		Button cancel = (Button) view.findViewById(R.id.cancel);
		voteval = comitems.get(arg2).getVotevalue();
		System.out.println(voteval);
		zanstatus();
		aDialog.setTitle("选择");
		aDialog.setCanceledOnTouchOutside(true);
		aDialog.setContentView(view);
		zan.setOnClickListener(new Click(arg2));
		back.setOnClickListener(new Click(arg2));
		cancel.setOnClickListener(new Click(arg2));
		aDialog.show();
	}

	class Click implements android.view.View.OnClickListener {
		private int arg2;

		public Click(int arg2) {
			// TODO Auto-generated constructor stub
			this.arg2 = arg2;
		}

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			switch (arg0.getId()) {
			case R.id.zan:
				if (voteval == 1) {
					voteval = 0;
					dozan(comitems.get(arg2).getId(), 0);
				} else {
					voteval = 1;
					dozan(comitems.get(arg2).getId(), 1);
				}
				zanstatus();
				aDialog.hide();
				break;
			case R.id.backanswer:
				atuid = comitems.get(arg2).getUid();
				aDialog.hide();
				break;
			case R.id.cancel:
				aDialog.hide();
				break;
			default:
				break;
			}
		}

	}

	private void zanstatus() {
		if (voteval == 1) {
			zan.setText("我已赞");
		} else {
			zan.setText("赞");
		}
	}

	private void dozan(int id, int value) {
		String url = "http://w.hihwei.com/?/article/ajax/article_vote/";
		RequestParams params = new RequestParams();
		params.put("type", "comment");
		params.put("item_id", id);
		params.put("rating", value);
		client.post(url, params, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				// TODO Auto-generated method stub
				Toast.makeText(EssayCom.this, "赞美失败", Toast.LENGTH_LONG).show();
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
					Toast.makeText(EssayCom.this, "赞美成功", Toast.LENGTH_LONG).show();
					refresh();
				} else {

					try {
						String err = jsonObject.getString("err");
						Toast.makeText(EssayCom.this, err, Toast.LENGTH_LONG)
								.show();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				
			}

		});
	}

	private void Getcom(String id) {
		String url = "http://w.hihwei.com/?/api/article/comment/?id=" + id;
		client.get(url, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				// TODO Auto-generated method stub
               
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				String info = new String(arg2);
				JSONObject jsonObject = null;
				JSONObject jsonObject2 = null;
				JSONObject rsm = null;
				int errno = 0;
				int total_rows = 0;
				try {
					jsonObject = new JSONObject(info);
					errno = jsonObject.getInt("errno");
				} catch (JSONException e) {
					// TODO: handle exception
					e.printStackTrace();
				}

				if (errno == 1) {
					try {
						rsm = jsonObject.getJSONObject("rsm");
						total_rows = rsm.getInt("total_rows");
						if (total_rows != 0) {
							JSONArray rows = rsm.getJSONArray("rows");
							for (int i = 0; i < rows.length(); i++) {
								jsonObject2 = rows.getJSONObject(i);
								EssatComitem comitemno = new EssatComitem();
								comitemno.setUid(jsonObject2.getString("uid"));
								JSONObject user_info = jsonObject2
										.getJSONObject("user_info");
								comitemno.setUsername(user_info
										.getString("user_name"));
								comitemno.setAvatarfile(user_info
										.getString("avatar_file"));
								comitemno.setComcontent(jsonObject2
										.getString("message"));
								comitemno.setAgreecount(jsonObject2
										.getString("votes"));
								comitemno.setVotevalue(jsonObject2
										.getInt("vote_value"));
								comitemno.setId(jsonObject2.getInt("id"));
								if (jsonObject2.getInt("at_uid") != 0) {
									JSONObject atuser = jsonObject2
											.getJSONObject("at_user_info");
									comitemno.setBackname(atuser
											.getString("user_name"));
									comitemno.setBackuid(atuser
											.getString("uid"));
								} else {
									comitemno.setBackname("");
									comitemno.setBackuid("");
								}
								comitems.add(comitemno);
							}
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else {
					try {
						String err = jsonObject.getString("err");
						Toast.makeText(EssayCom.this, err, Toast.LENGTH_LONG)
								.show();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				comAdapter = new EssayComAdapter(EssayCom.this, comitems,
						imageDownLoader);
				comlist.setAdapter(comAdapter);

			}

		});
	}
	private void refresh(){
		comitems.clear();
		checkKeyboardShowing();
		Thread.currentThread();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Getcom(id);
	}

	private void postcom(RequestParams params) {
		String url = "http://w.hihwei.com/?/api/publish/save_comment/";
		client.post(url, params, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				// TODO Auto-generated method stub
				Toast.makeText(EssayCom.this, "评论失败", Toast.LENGTH_LONG).show();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				atuid="";
				Toast.makeText(EssayCom.this, "评论成功", Toast.LENGTH_LONG).show();
			}

		});
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
			showImage(mFirstVisibleItem, mVisibleItemCount);
		} else {
			cancleTask();
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		mFirstVisibleItem = firstVisibleItem;
		mVisibleItemCount = visibleItemCount;
		totalItem = totalItemCount;
		// 因此在这里为首次进入程序开启下载任务。
		if (isFirstEnter && visibleItemCount > 0) {
			showImage(mFirstVisibleItem, mVisibleItemCount);
			isFirstEnter = false;
		}
	}

	private void showImage(int firstVisibleItem, int visibleItemCount) {
		// 注：firstVisibleItem + visibleItemCount-1 = 20 1其中包括了footview，这儿一定要小心！
		for (int i = firstVisibleItem; i < firstVisibleItem + visibleItemCount; i++) {
			String mImageUrl = comitems.get(i).getAvatarfile();
			if (!mImageUrl.equals("")) {
				mImageUrl = Config.getValue("userImageBaseUrl") + mImageUrl;
				// System.err.println(mImageUrl);
				final ImageView mImageView = (ImageView) comlist
						.findViewWithTag(mImageUrl);
				imageDownLoader.getBitmap(mImageUrl,
						new onImageLoaderListener() {

							public void onImageLoader(Bitmap bitmap, String url) {
								// System.out.println(bitmap+")(");
								if (mImageView != null && bitmap != null) {
									mImageView.setImageBitmap(bitmap);
								}
							}
						});
			} else {
				continue;
			}
		}
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

	public void cancleTask() {
		imageDownLoader.cacelTask();
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
