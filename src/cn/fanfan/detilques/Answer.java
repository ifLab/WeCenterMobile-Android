package cn.fanfan.detilques;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.Header;
import org.apache.http.client.CookieStore;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

import cn.fanfan.common.GetUserNamImage;
import cn.fanfan.common.GetUserNamImage.onLoaderListener;
import cn.fanfan.common.TextShow;
import cn.fanfan.detilessay.EssayCom;
import cn.fanfan.main.R;
import cn.fanfan.userinfo.UserInfoActivity;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Answer extends Activity implements OnClickListener {
	private AsyncHttpClient client;
	private CookieStore myCookieStore;
	private Dialog aDialog;
	private TextView zanorno;
	private Button addcom;
	private ImageView agree, disagree;
	private int tag = 0;
	private String answer_id;
	private TextView answerdetil, time, name,sign;
	private TextShow textShow;
	private ImageView userimage;
	private GetUserNamImage namImage;
	private String uid;
	private String comment_count;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.answer);
    	ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.show();
		client = new AsyncHttpClient();
		myCookieStore = new PersistentCookieStore(this);
		client.setCookieStore(myCookieStore);
		zanorno = (TextView) findViewById(R.id.zanorno);
		addcom = (Button) findViewById(R.id.addcom);
		sign = (TextView)findViewById(R.id.sign);
		Intent intent = getIntent();
		answer_id = intent.getStringExtra("answerid");
		answerdetil = (TextView) findViewById(R.id.answerdetil);
		time = (TextView) findViewById(R.id.time);
		name = (TextView)findViewById(R.id.username);
		name.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("uid", uid);
				intent.setClass(Answer.this, UserInfoActivity.class);
				startActivity(intent);
			}
		});
		userimage = (ImageView)findViewById(R.id.userimage);
		userimage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("uid", uid);
				intent.setClass(Answer.this, UserInfoActivity.class);
				startActivity(intent);
			}
		});
		namImage = new GetUserNamImage(this);
		addcom.setOnClickListener(this);
		zanorno.setOnClickListener(this);
		GetAnswer();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		System.out.println(tag);
		switch (v.getId()) {
		case R.id.zanorno:
			showDialog();
			break;
		case R.id.agree:
			switch (tag) {
			case 0:
				zanorno.setText(String.valueOf(Integer.valueOf(zanorno.getText().toString())+1));
				tag = 1;
				dozan(1);
				zanstatus();
				aDialog.dismiss();
				break;
			case 1:
				zanorno.setText(String.valueOf(Integer.valueOf(zanorno.getText().toString())-1));
                tag = 0;
                dozan(1);
                zanstatus();
				aDialog.dismiss();
				break;
			case -1:
				zanorno.setText(String.valueOf(Integer.valueOf(zanorno.getText().toString())+1));
				tag = 1;
				dozan(1);
				zanstatus();
				aDialog.dismiss();
				break;
			default:
				break;
			}
			break;
		case R.id.disagree:
			switch (tag) {
			case 0:
				dozan(-1);
				tag = -1;
				zanstatus();
				aDialog.dismiss();
				break;
			case 1:
				zanorno.setText(String.valueOf(Integer.valueOf(zanorno.getText().toString())-1));
				dozan(-1);
				tag = -1;
				zanstatus();
				aDialog.dismiss();
				break;
			case -1:
				dozan(-1);
				tag = 0;
				zanstatus();
				aDialog.dismiss();
				break;
			default:
				break;
			}
			break;
		case R.id.addcom:
			Intent intent = new Intent();
			intent.putExtra("answerid", answer_id);
			intent.putExtra("comcount", comment_count);
			intent.setClass(this, ComList.class);
			startActivity(intent);
		default:
			break;
		}
		
	}

	private void zanstatus() {
		Drawable nav_up;
		switch (tag) {
		case 1:
			nav_up = getResources().getDrawable(R.drawable.ic_vote_checked);
			nav_up.setBounds(0, 0, nav_up.getMinimumWidth(),
					nav_up.getMinimumHeight());
			zanorno.setCompoundDrawables(nav_up, null, null, null);
			break;
		case 0:
			nav_up = getResources().getDrawable(R.drawable.ic_vote_normal);
			nav_up.setBounds(0, 0, nav_up.getMinimumWidth(),
					nav_up.getMinimumHeight());
			zanorno.setCompoundDrawables(nav_up, null, null, null);
			break;
		case -1:
			nav_up = getResources()
					.getDrawable(R.drawable.ic_vote_down_checked);
			nav_up.setBounds(0, 0, nav_up.getMinimumWidth(),
					nav_up.getMinimumHeight());
			zanorno.setCompoundDrawables(nav_up, null, null, null);
			break;
		default:
			break;
		}
	}
    private void dozan(int value){
    	String url = "http://w.hihwei.com/?/question/ajax/answer_vote/";
    	RequestParams params = new RequestParams();
    	params.put("answer_id", answer_id);
    	params.put("value", value);
    	client.post(url, params, new AsyncHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				// TODO Auto-generated method stub
				Toast.makeText(Answer.this, "选择失败", Toast.LENGTH_LONG).show();
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
			    	Toast.makeText(Answer.this,"选择成功" , Toast.LENGTH_LONG).show();
				} else {

                    try {
						String err = jsonObject.getString("err");
						Toast.makeText(Answer.this,err , Toast.LENGTH_LONG).show();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				
			}
    		
    	});
    }
	private void showDialog() {
		aDialog = new Dialog(this);
		LayoutInflater inflater = LayoutInflater.from(this);
		View view = inflater.inflate(R.layout.agranddis, null);
		agree = (ImageView) view.findViewById(R.id.agree);
		disagree = (ImageView) view.findViewById(R.id.disagree);
		aDialog.setTitle("选择");
		aDialog.setCanceledOnTouchOutside(true);
		aDialog.setContentView(view);
		agree.setOnClickListener(this);
		disagree.setOnClickListener(this);
		aDialog.show();
	}

	private void GetAnswer() {
		String url = "http://w.hihwei.com/?/api/question/answer_detail/?id="
				+ answer_id;
		final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		client.get(url, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				String information = new String(arg2);
				try {
					JSONObject jsonObject = new JSONObject(information);
					int errno = jsonObject.getInt("errno");
					if (errno == 1) {
						JSONObject rsm = jsonObject.getJSONObject("rsm");;
						//String question_id = rsm.getString("question_id");
						String answer_content = rsm.getString("answer_content");
						String add_time = rsm.getString("add_time");
						String signature = rsm.getString("signature");
						tag = rsm.getInt("vote_value");
						System.out.println(tag+"   qweqwe");
						zanstatus();
						sign.setText(signature);
						 String agree_count = rsm.getString("agree_count");
						 uid = rsm.getString("uid");
						comment_count = rsm.getString("comment_count");
						addcom.setText("添加评论  "+comment_count);
                        Date date = new Date(Long.valueOf(add_time)*1000);
                        time.setText(format.format(date));
						DisplayMetrics dm = new DisplayMetrics();
						getWindowManager().getDefaultDisplay().getMetrics(dm);
						float screenW = dm.widthPixels;
						textShow = new TextShow(JSONTokener(answer_content), answerdetil,Answer.this
							,screenW);
						textShow.execute();
						zanorno.setText(agree_count);
						namImage.getuserinfo(uid,name,userimage,new onLoaderListener() {
							
							@Override
							public void onPicLoader(Bitmap bitmap, ImageView userimage) {
								// TODO Auto-generated method stub
								if (bitmap != null) {
									userimage.setImageBitmap(bitmap);
								} else {
									userimage.setImageDrawable(Answer.this.getResources()
											.getDrawable(R.drawable.logo));
								}
							}
							
							@Override
							public void onNameLoader(String name, TextView username) {
								// TODO Auto-generated method stub
								username.setText(name);
							}
						});
						
					} else {
                        String err = jsonObject.getString("err");
                        Toast.makeText(Answer.this, err, Toast.LENGTH_LONG).show();
					}
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
               
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				// TODO Auto-generated method stub
				Toast.makeText(Answer.this, "获取失败", Toast.LENGTH_LONG).show();
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
