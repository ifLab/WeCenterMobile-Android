package cn.fanfan.detilques;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.client.CookieStore;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;

import cn.fanfan.common.Config;
import cn.fanfan.common.GetUserNamImage;
import cn.fanfan.common.GetUserNamImage.onLoaderListener;
import cn.fanfan.main.R;
import cn.fanfan.topic.imageload.ImageDownLoader.onImageLoaderListener;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Intents.Insert;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;

public class ComList extends Activity implements OnItemClickListener,
		OnScrollListener {
	private AsyncHttpClient client;
	private int totalItem;
	private CookieStore myCookieStore;
	private boolean isFirstEnter;
	private int mFirstVisibleItem;
	private int mVisibleItemCount;
	private Dialog aDialog;
	private ListView comlist;
	private GetUserNamImage getUserNamImage;
	private List<Comitem> comitems;
	private ComListAdapter comListAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comlist);
    	ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.show();
		client = new AsyncHttpClient();
		myCookieStore = new PersistentCookieStore(this);
		client.setCookieStore(myCookieStore);
		comitems = new ArrayList<Comitem>();
		comlist = (ListView) findViewById(R.id.comlist);
		getUserNamImage = new GetUserNamImage(this);
		isFirstEnter = true;
		comlist.setOnItemClickListener(this);
		comlist.setOnScrollListener(this);
		Getcom("3");
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		showDialog();
	}

	private void showDialog() {
		aDialog = new Dialog(this);
		LayoutInflater inflater = LayoutInflater.from(this);
		View view = inflater.inflate(R.layout.dialog, null);
		Button zan = (Button) view.findViewById(R.id.zan);
		Button back = (Button) view.findViewById(R.id.backanswer);
		Button cancel = (Button) view.findViewById(R.id.cancel);
		aDialog.setTitle("选择");
		aDialog.setCanceledOnTouchOutside(true);
		aDialog.setContentView(view);
		zan.setOnClickListener(new Click());
		back.setOnClickListener(new Click());
		cancel.setOnClickListener(new Click());
		aDialog.show();
	}

	class Click implements android.view.View.OnClickListener {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			switch (arg0.getId()) {
			case R.id.zan:
				aDialog.hide();
				break;
			case R.id.backanswer:
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

	private void Getcom(String id) {
		String url = "http://w.hihwei.com/api/answer_comment.php?id=" + id;
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
				JSONObject jsonObject2 = null;
				JSONObject jsonObject;
				JSONArray rsm = null;
				try {
					jsonObject = new JSONObject(info);
					rsm = jsonObject.getJSONArray("rsm");
				} catch (JSONException e) {
					// TODO: handle exception
				}
				for (int i = 0; i < rsm.length(); i++) {
					try {
						jsonObject2 = rsm.getJSONObject(i);
						JSONObject object = jsonObject2
								.getJSONObject("at_user");
						Comitem comitemno = new Comitem();
						comitemno.setUid(jsonObject2.getString("uid"));
						comitemno.setUsername(jsonObject2.getString("user_name"));
						comitemno.setComcontent(jsonObject2.getString("content"));
						String timestring = jsonObject2.getString("add_time");
						comitemno.setBackname(object.getString("user_name"));
						comitemno.setBackuid(object.getString("uid"));
						comitems.add(comitemno);

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
						
						try {
							Comitem comitem = new Comitem();
							comitem.setUid(jsonObject2.getString("uid"));
							comitem.setUsername(jsonObject2
									.getString("user_name"));
							comitem.setComcontent(jsonObject2
									.getString("content"));
							String timestring = jsonObject2
									.getString("add_time");
							comitem.setBackuid("");
							comitem.setBackname("");
							comitems.add(comitem);
						} catch (JSONException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

					}
				}
				comListAdapter = new ComListAdapter(ComList.this, comitems);
				comlist.setAdapter(comListAdapter);
			}

		});
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
			//showImage(mFirstVisibleItem, mVisibleItemCount);
		} else {
			getUserNamImage.cancleTask();
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
			//showImage(mFirstVisibleItem, mVisibleItemCount);
			isFirstEnter = false;
		}
	}

	private void showImage(int firstVisibleItem, int visibleItemCount) {
		// 注：firstVisibleItem + visibleItemCount-1 = 20 1其中包括了footview，这儿一定要小心！
		for (int i = firstVisibleItem; i < firstVisibleItem + visibleItemCount; i++) {
			final int j = i;
			final ImageView image = (ImageView) comlist
					.findViewWithTag(comitems.get(i).getUid() + "image");
			final TextView name = (TextView) comlist.findViewWithTag(comitems
					.get(i).getUid() + "name");
			getUserNamImage.getuserinfo(comitems.get(i).getUid(), name, image,
					new onLoaderListener() {

						@Override
						public void onPicLoader(Bitmap bitmap,
								ImageView userimage) {
							// TODO Auto-generated method stub
							if (userimage.getTag()!=null && userimage.getTag().equals(comitems.get(j).getUid()+"image")) {
								if (bitmap != null) {
									userimage.setImageBitmap(bitmap);
								} else {
									userimage.setImageDrawable(ComList.this.getResources()
											.getDrawable(R.drawable.logo));;
								}
						}
							}


						@Override
						public void onNameLoader(String name, TextView username) {
							// TODO Auto-generated method stub

						}
					});
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
}
