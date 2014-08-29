package cn.fanfan.common;

import java.io.InputStream;
import java.util.ArrayList;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xml.sax.XMLReader;
import cn.fanfan.topic.imageload.FileUtils;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.Editable;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.Html.TagHandler;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.Spanned;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class TextShow extends AsyncTask<String, Integer, Spanned> {

	private String text;
	private TextView textView;
	private FileUtils fileUtils;
	private float screenW;
	private  Context context;
	private ArrayList<String> urlSpans;
	private int tag=0;

	public TextShow(String text, TextView textView, Context context,float screenW) {
		// TODO Auto-generated constructor stub
		this.screenW = screenW;
		this.text = text;
		this.textView = textView;
		this.context = context;
		urlSpans = new ArrayList<String>();
		fileUtils = new FileUtils(context);
		
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
	}

	@Override
	protected Spanned doInBackground(String... arg0) {
		// TODO Auto-generated method stub
		final Spanned spanned;
		ImageGetter imgGetter = new Html.ImageGetter() {
			@SuppressWarnings("deprecation")
			@Override
			public Drawable getDrawable(String source) {
				Drawable d = null;
				Bitmap bm = null;
				String url = source.replaceAll("[^\\w]", "");
				String imageurl = Environment
						.getExternalStorageDirectory()
						+ "/fanfantopic/" + url;
				urlSpans.add(imageurl);
				try {
					if (!fileUtils.isFileExists(url)
							|| fileUtils.getFileSize(url) == 0) {
						InputStream is = null;
						HttpResponse httpResponse = new DefaultHttpClient()
								.execute(new HttpGet(source));
						if (httpResponse.getStatusLine().getStatusCode() == 200) {
							is = httpResponse.getEntity()
							.getContent();
						} else {
                           System.out.println("网络不好");
						}
								
						bm = BitmapFactory.decodeStream(is);
						fileUtils.saveBitmap(source.replaceAll("[^\\w]", ""),
								bm);
						is.close();
					} else {
						//ImageGet imageGet = new ImageGet();
						bm = BitmapFactory.decodeFile(imageurl);
						//bm = imageGet.getBitmap();
					}
					
					d = new BitmapDrawable(bm);
					if (bm.getWidth() >= screenW-100) {
						float so = ((float)(bm.getHeight())/bm.getWidth());
						float h =  (screenW-100)*so;
						d.setBounds(0, 0,(int)(screenW-100),(int)h);
					} else {
						d.setBounds(0, 0,bm.getWidth(),bm.getHeight());
					}
					

				} catch (Exception e) {
					e.printStackTrace();
				}
				return d;
			}
		};
		spanned = Html.fromHtml(text, imgGetter, new ImgTaghand());
		return spanned;
	}

	@Override
	protected void onPostExecute(Spanned result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		textView.setText(result);
		textView.setClickable(true);
		textView.setMovementMethod(LinkMovementMethod.getInstance());
		
	}

	private  class ImgTaghand implements TagHandler {


		@Override
		public void handleTag(boolean arg0, String arg1, Editable arg2,
				XMLReader arg3) {
			// TODO Auto-generated method stub
			if (arg1.equalsIgnoreCase("img")) {               
				arg2.setSpan(new GameSpan(tag), arg2.length()-1, arg2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				tag++;
			}
			

		}
		private class GameSpan extends ClickableSpan implements OnClickListener {  
			private int tag;
            public GameSpan(int tag) {
				// TODO Auto-generated constructor stub
            	this.tag = tag;
			}
	        @Override  
	        public void onClick(View v) {  
	            // 跳转某页面   
	        	Intent intent = new Intent();
	        	intent.putStringArrayListExtra("images", urlSpans);
	        	intent.putExtra("tag",tag);
	        	intent.setClass(context, ShowPic.class);
	        	context.startActivity(intent);
	        }  
	    }
	}
}
