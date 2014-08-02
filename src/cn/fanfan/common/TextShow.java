package cn.fanfan.common;


import java.io.InputStream;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.Spanned;
import android.widget.TextView;

public class TextShow extends AsyncTask<String, Integer, Spanned> {

	private String text;
	private TextView textView;
	private float screenW;

	public TextShow(String text,TextView textView,float screenW) {
		// TODO Auto-generated constructor stub
		this.text = text;
		this.textView = textView;
		this.screenW = screenW;
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
				try {
					InputStream is = new DefaultHttpClient().execute(new HttpGet(source))
							.getEntity().getContent();
					Bitmap bm = BitmapFactory.decodeStream(is);
					d = new BitmapDrawable(bm);
					float so = ((float)(bm.getHeight())/bm.getWidth());
					float h =  (screenW-40)*so;
					d.setBounds(0, 0,(int)screenW-40 ,(int)h);
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
