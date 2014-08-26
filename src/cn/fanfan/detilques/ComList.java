package cn.fanfan.detilques;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import cn.fanfan.common.GetUserNamImage;
import cn.fanfan.common.GetUserNamImage.onLoaderListener;
import cn.fanfan.detilessay.EssayCom;
import cn.fanfan.main.R;
import cn.fanfan.topic.imageload.ImageDownLoader.onImageLoaderListener;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
	private String answerid,comcount;
	private EditText comment;
	private ImageButton publish;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comlist);

    	ActionBar actionBar = getActionBar();
    	actionBar.setIcon(null);
    	actionBar.setTitle("回答评论");
    	actionBar.setDisplayUseLogoEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.show();		
		client = new AsyncHttpClient();
		myCookieStore = new PersistentCookieStore(this);
		client.setCookieStore(myCookieStore);
		comment = (EditText) findViewById(R.id.comment);
		publish = (ImageButton) findViewById(R.id.publish);
		comment.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if (arg1) {
					publish.setClickable(true);
				} else {
					publish.setClickable(false);
				}
				
			}
		});
		publish.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				RequestParams params = new RequestParams();
				params.put("answer_id", answerid);
				params.put("message", comment.getText().toString());
				postcom(params);
				comitems.clear();
				checkKeyboardShowing();
				Thread.currentThread();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Getcom(answerid);
			}

		});
		comitems = new ArrayList<Comitem>();
		Intent intent = getIntent();
		answerid = intent.getStringExtra("answerid");
		comcount = intent.getStringExtra("comcount");
		comlist = (ListView) findViewById(R.id.comlist);
		getUserNamImage = new GetUserNamImage(this);
		isFirstEnter = true;
		comlist.setOnItemClickListener(this);
		comlist.setOnScrollListener(this);
		if (!comcount.equals("0")) {
			Getcom(answerid);
		}
			System.out.println(answerid);
			
		
		
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
		Button zan = (Button) view.findViewById(R.id.zan);
		zan.setVisibility(View.GONE);
		Button back = (Button) view.findViewById(R.id.backanswer);
		Button cancel = (Button) view.findViewById(R.id.cancel);
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
				aDialog.hide();
				break;
			case R.id.backanswer:
				comment.setText("@"+comitems.get(arg2).getUsername()+":");
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
				final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				String info = new String(arg2);
				JSONObject jsonObject2 = null;
				JSONObject jsonObject = null;
				JSONArray rsm = null;
				int errno = 0;
				try {
					jsonObject = new JSONObject(info);
					errno = jsonObject.getInt("errno");
				} catch (JSONException e) {
					// TODO: handle exception
					e.printStackTrace();
				}
					
					if (errno == 1) {
						try {
							rsm = jsonObject.getJSONArray("rsm");							
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
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
								Date date = new Date(Long.valueOf(timestring)*1000);
								comitemno.setTime(format.format(date));
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
									Date date = new Date(Long.valueOf(timestring)*1000);
									comitem.setTime(format.format(date));
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
					} else {
                        try {
							String err = jsonObject.getString("err");
							Toast.makeText(ComList.this, err,Toast.LENGTH_LONG).show();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                        
					}
					
				

				
			}

		});
	}
    private void postcom(RequestParams params){
    	String url = "http://w.hihwei.com/?/question/ajax/save_answer_comment/?answer_id="+answerid;
    	client.post(url, params, new AsyncHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				// TODO Auto-generated method stub
				Toast.makeText(ComList.this, "评论失败", Toast.LENGTH_LONG).show();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				
				Toast.makeText(ComList.this, "评论成功", Toast.LENGTH_LONG).show();
				comment.setText("");
			}
    		
    	});
    }
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
			showImage(mFirstVisibleItem, mVisibleItemCount);
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
			showImage(mFirstVisibleItem, mVisibleItemCount);
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
