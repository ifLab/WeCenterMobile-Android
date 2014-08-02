package cn.fanfan.detilques;

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

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

import cn.fanfan.common.Config;
import cn.fanfan.common.MyProgressDialog;
import cn.fanfan.main.R;
import cn.fanfan.question.Bimp;
import cn.fanfan.question.FileUtils;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class WriteAnswer extends Activity {
	private AsyncHttpClient client;
    private CookieStore myCookieStore; 
    private MyProgressDialog progressDialog;
    private int attach_id ;
	private ActionBar actionBar;
	private static Uri photoUri ;
	private File photo;
	private float dp;
	private GridView gridview;
	private GridAdapter adapter;
	private String question_id;
	public List<Bitmap> bmp = new ArrayList<Bitmap>();
	public List<String> drr = new ArrayList<String>();
	private String[] items = new String[] { "选择本地图片", "拍照" };
	private HorizontalScrollView selectimg_horizontalScrollView;
	private static String attach_access_key ;
	private EditText editText;
    @SuppressLint("NewApi") @Override
    protected void onCreate(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.writeanswer);
    	actionBar = getActionBar();
    	actionBar.show();
    	Intent intent = getIntent();
    	//question_id = intent.getStringExtra("question_id");
    	question_id = "25";
    	attach_access_key = md5(getAttachKey());
    	dp = getResources().getDimension(R.dimen.dp);
    	editText = (EditText)findViewById(R.id.answerdetil);
    	selectimg_horizontalScrollView = (HorizontalScrollView)findViewById(R.id.selectimg_horizontalScrollView);
		gridview = (GridView)findViewById(R.id.noScrollgridview);
		gridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		client = new AsyncHttpClient();
        myCookieStore = new PersistentCookieStore(this);
		client.setCookieStore(myCookieStore);
		progressDialog = new MyProgressDialog(this, "正在加载中", "请稍后...", false);
		gridviewInit();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	// TODO Auto-generated method stub
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.quespub, menu);
    	return super.onCreateOptionsMenu(menu);
    }
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.postp:
			showDialog();
			break;
		case R.id.publish:
			 RequestParams params = new RequestParams();
			 params.put("question_id", question_id);
			 params.put("answer_content", editText.getText().toString());
			 params.put("attach_access_key", attach_access_key);
			 attach_access_key = md5(getAttachKey());
             postanswer(params);
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
    private void showDialog() {

		new AlertDialog.Builder(this)
				.setTitle("上传图片")
				.setItems(items, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							Intent openAlbumIntent = new Intent(Intent.ACTION_PICK,
									android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
							startActivityForResult(openAlbumIntent, 0);
							break;
						case 1:
	                        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                        	String pString = Environment.getExternalStorageDirectory()+"/fanfantmp";
                        	 final File photodir = new File(pString);
                        	 if (!photodir.exists()) {  
                  
										photodir.mkdirs();
										                        		 
                             }  
                        	
									// TODO Auto-generated method stub
									photo = new File(photodir,getPhotoFileName());
		                        	 photoUri = Uri.fromFile(photo);
		                        	intent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);
		                        	startActivityForResult(intent, 1);
						
							 System.out.println(photoUri.getPath());
							break;
						}
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).show();

	}
    @SuppressLint("SimpleDateFormat")
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    	// TODO Auto-generated method stub
	    	  super.onActivityResult(requestCode, resultCode, data);
	    	  switch (requestCode) {
			case 0:
				if (resultCode!= RESULT_CANCELED && null!= data) {
					Uri uri = data.getData();
					String[] pojo = { MediaStore.Images.Media.DATA };
					Cursor cursor = getContentResolver().query(uri, pojo, null, null,
							null);
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
					Toast.makeText(this,
		        			"取照片失败！", Toast.LENGTH_LONG).show();
				}
				break;
			case 1:
				if (resultCode!=RESULT_CANCELED && photoUri != null) {
					//startPhotoZoom(photoUri);
					picpost(photoUri);
				} else {
					Toast.makeText(this,
		        			"拍照失败！", Toast.LENGTH_LONG).show();
				}
				break;
			default:
				break;
			}
	    	  
	    }
    @SuppressLint("SimpleDateFormat")
    private void picpost(Uri photoUri) {
		   File picphoto = null;
		try {
			picphoto = new File(Bimp.revitionImageSize(photoUri.getPath()));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
			RequestParams params = new RequestParams();
			
			try {
				params.put("qqfile", picphoto);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            postpic(params, "answer", attach_access_key);
			
	}
    public void postpic(RequestParams params,String id,String attach) {
        
    	String url = Config.getValue("PostPic")+"?id="+id+"&attach_access_key="+attach;
		client.post(url, params, new AsyncHttpResponseHandler(){
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				JSONObject jsonObject = new JSONObject() ;
				JSONObject rsm = new JSONObject();
				int errno = 0;
				String err = null;
				String result = new String(arg2);
				System.out.println(result+" qweqweqweqweq");
				try {
				     jsonObject = new JSONObject(result);
					 errno = jsonObject.getInt("errno");
					 err = jsonObject.getString("err");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
  			    if (errno == 1) {
                	Toast.makeText(WriteAnswer.this, "上传成功！", Toast.LENGTH_LONG).show();
				     try {
						rsm = jsonObject.getJSONObject("rsm");
						 attach_id = rsm.getInt("attach_id");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                	editText.append("[attach]"+attach_id+"[/attach]");
                	Bitmap bitmap = Bimp.getLoacalBitmap(photoUri.getPath());
        			bitmap = Bimp.createFramedPhoto(480, 480, bitmap,
        					(int) (dp * 1.6f));
        			drr.add(photoUri.getPath());
        			bmp.add(bitmap);
        			gridviewInit();
				}
                if (errno == -1) {
                	Toast.makeText(WriteAnswer.this, err, Toast.LENGTH_LONG).show();         
                	System.out.println(err);
				}
			}
			 @Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				// TODO Auto-generated method stub
				
			}
		});
		}
    public void postanswer(RequestParams params) {
		String url = Config.getValue("PostAnswer");
		client.post(url, params, new AsyncHttpResponseHandler(){
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				progressDialog.show();
				super.onStart();
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				JSONObject jsonObject;
				int errno = 0;
				String err = null;
				String result = new String(arg2);
				System.out.println(result+" poipoipoi");
				try {
					jsonObject = new JSONObject(result);
					errno = jsonObject.getInt("errno");
					err = jsonObject.getString("err");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				progressDialog.hideAndCancle();
                if (errno == 1) {
                	Toast.makeText(WriteAnswer.this, "回答成功！", Toast.LENGTH_LONG).show();
				}
                if (errno == -1) {
                	Toast.makeText(WriteAnswer.this, err, Toast.LENGTH_LONG).show();
                	System.out.println(err);
				}
			}
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				// TODO Auto-generated method stub
				
			}
		});
	}
    @SuppressLint("SimpleDateFormat") 
	private String getAttachKey() {  
        Date date = new Date(System.currentTimeMillis());  
        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'yyyyMMddHHmmss");  
        return dateFormat.format(date) + Math.random()*100;  
    }  
	 @SuppressLint("SimpleDateFormat") 
	 private String getPhotoFileName() {  
	        Date date = new Date(System.currentTimeMillis());  
	        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");  
	        return dateFormat.format(date) + ".jpg";  
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
	   public void gridviewInit() {
			adapter = new GridAdapter(this);
			adapter.setSelectedPosition(0);
			int size = 0;
			if (bmp.size() < 6) {
				size = bmp.size() + 1;
			} else {
				size = bmp.size();
			}
			LayoutParams params = gridview.getLayoutParams();
			final int width = size * (int) (dp * 9.4f);
			params.width = width;
			gridview.setLayoutParams(params);
			gridview.setColumnWidth((int) (dp * 9.4f));
			gridview.setStretchMode(GridView.NO_STRETCH);
			gridview.setNumColumns(size);
			gridview.setAdapter(adapter);
			gridview.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
						long arg3) {
					// TODO Auto-generated method stub
					/*((InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE))
					.hideSoftInputFromWindow(getActivity()
							.getCurrentFocus().getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);*/
			if (arg2 == bmp.size()) {
				String sdcardState = Environment.getExternalStorageState();
				if (Environment.MEDIA_MOUNTED.equals(sdcardState)) {
					showDialog();
				} else {
					Toast.makeText(WriteAnswer.this, "sdcard已拔出，不能选择照片",
							Toast.LENGTH_SHORT).show();
				}
			} else {
			}
				}
			});

			selectimg_horizontalScrollView.getViewTreeObserver()
					.addOnPreDrawListener(// 绘制完毕
							new OnPreDrawListener() {
								public boolean onPreDraw() {
									selectimg_horizontalScrollView.scrollTo(width,
											0);
									selectimg_horizontalScrollView
											.getViewTreeObserver()
											.removeOnPreDrawListener(this);
									return false;
								}
							});
		}
		
		 protected void onDestroy() {

				FileUtils.deleteDir(FileUtils.SDPATH);
				FileUtils.deleteDir(FileUtils.SDPATH1);
				// 清理图片缓存
				super.onDestroy();
			}
		public class GridAdapter extends BaseAdapter {
			private LayoutInflater listContainer;
			private int selectedPosition = -1;
			private boolean shape;

			public boolean isShape() {
				return shape;
			}

			public void setShape(boolean shape) {
				this.shape = shape;
			}

			public class ViewHolder {
				public ImageView image;
			}

			public GridAdapter(Context context) {
				listContainer = LayoutInflater.from(context);
			}

			public int getCount() {
				if (bmp.size() < 6) {
					return bmp.size() + 1;
				} else {
					return bmp.size();
				}
			}

			public Object getItem(int arg0) {

				return null;
			}

			public long getItemId(int arg0) {

				return 0;
			}

			public void setSelectedPosition(int position) {
				selectedPosition = position;
			}

			public int getSelectedPosition() {
				return selectedPosition;
			}

			/**
			 * ListView Item设置
			 */
			public View getView(int position, View convertView, ViewGroup parent) {
				final int sign = position;
				// 自定义视图
				ViewHolder holder = null;
				if (convertView == null) {
					holder = new ViewHolder();
					// 获取list_item布局文件的视图

					convertView = listContainer.inflate(
							R.layout.item_published_grida, null);

					// 获取控件对象
					holder.image = (ImageView) convertView
							.findViewById(R.id.item_grida_image);
					// 设置控件集到convertView
					convertView.setTag(holder);
				} else {
					holder = (ViewHolder) convertView.getTag();
				}

				if (position == bmp.size()) {
					holder.image.setImageBitmap(BitmapFactory.decodeResource(
							getResources(), R.drawable.icon_addpic_unfocused));
					if (position == 6) {
						holder.image.setVisibility(View.GONE);
					}
				} else {
					holder.image.setImageBitmap(bmp.get(position));
					holder.image.setOnClickListener(new OnClickListener() {

						public void onClick(View v) {
							bmp.get(sign).recycle();
							bmp.remove(sign);
							drr.remove(sign);
							gridviewInit();
						}
					});
				}

				return convertView;
			}
		}
}
