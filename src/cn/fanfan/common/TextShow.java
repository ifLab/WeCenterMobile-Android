package cn.fanfan.common;

import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import cn.fanfan.topic.imageload.FileUtils;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.Spanned;
import android.widget.TextView;

public class TextShow extends AsyncTask<String, Integer, Spanned> {

	private String text;
	private TextView textView;
	private FileUtils fileUtils;
	private float screenW;

	public TextShow(String text, TextView textView, Context context,float screenW) {
		// TODO Auto-generated constructor stub
		this.screenW = screenW;
		this.text = text;
		this.textView = textView;
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
                           System.out.println("ÍøÂç²»ºÃ");
						}
								
						bm = BitmapFactory.decodeStream(is);
						fileUtils.saveBitmap(source.replaceAll("[^\\w]", ""),
								bm);
						is.close();
					} else {
						//ImageGet imageGet = new ImageGet();
						bm = BitmapFactory.decodeFile(Environment
								.getExternalStorageDirectory()
								+ "/fanfantopic/" + url);
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
		spanned = Html.fromHtml(text, imgGetter, null);
		return spanned;
	}

	@Override
	protected void onPostExecute(Spanned result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		textView.setText(result);
	}

}
