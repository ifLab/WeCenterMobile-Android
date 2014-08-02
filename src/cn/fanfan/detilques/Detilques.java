package cn.fanfan.detilques;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import cn.fanfan.common.GetUserNamImage;
import cn.fanfan.common.TextShow;
import cn.fanfan.main.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class Detilques extends Activity {
	private ListView comlist;
	private List<AnswerItem> comlists;
	private ComAdapter adapter;
	private TextView addanswer, questiontitle, questiondetil, focus,
			answercount;
	private AsyncHttpClient client;
	private TextShow textShow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detilques);
		client = new AsyncHttpClient();
		comlist = (ListView) findViewById(R.id.comlist);
		comlists = new ArrayList<AnswerItem>();
		comlist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(Detilques.this, Answer.class);
				startActivity(intent);
			}
		});
		addanswer = (TextView) findViewById(R.id.addanswer);
		questiontitle = (TextView) findViewById(R.id.question_contents);
		questiondetil = (TextView) findViewById(R.id.question_detail);
		focus = (TextView) findViewById(R.id.focus_count);
		answercount = (TextView) findViewById(R.id.answer_count);
		addanswer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(Detilques.this, WriteAnswer.class);
				startActivity(intent);
			}
		});
		GetQuestion("2");
	}

	private void GetQuestion(String question_id) {
		String url = "http://w.hihwei.com/?/api/question/question/?id="
				+ question_id;
		client.get(url, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				String information = new String(arg2);
				try {
					JSONObject jsonObject = new JSONObject(information);
					JSONObject rsm = jsonObject.getJSONObject("rsm");
					JSONObject question_info = rsm
							.getJSONObject("question_info");
					String answer_count = rsm.getString("answer_count");
					JSONObject answers = rsm.getJSONObject("answers");
					answercount.setText(answer_count);
					JSONArray question_topics = rsm
							.getJSONArray("question_topics");
					String question_id = question_info.getString("question_id");
					String question_content = question_info
							.getString("question_content");
					String question_detail = question_info
							.getString("question_detail");
					String focus_count = question_info.getString("focus_count");
					questiontitle.setText(question_content);
					DisplayMetrics dm = new DisplayMetrics();
					getWindowManager().getDefaultDisplay().getMetrics(dm);
					float screenW = dm.widthPixels;
					textShow = new TextShow(question_detail, questiondetil,
							screenW);
					textShow.execute();
					focus.setText(focus_count);
					for (int i = 0; i < Integer.valueOf(answer_count); i++) {
						JSONObject answer = answers.getJSONObject(String
								.valueOf(i + 1));
						AnswerItem answerItem = new AnswerItem();
						answerItem.setAnswer_id(answer.getString("answer_id"));
						answerItem.setAnswer_content(answer
								.getString("answer_content"));
						answerItem.setAgree_count(answer
								.getString("agree_count"));
						answerItem.setUid(answer.getString("uid"));
						comlists.add(answerItem);
					}
					adapter = new ComAdapter(comlists, Detilques.this);
					comlist.setAdapter(adapter);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});
	}

	class ComAdapter extends BaseAdapter {
		private List<AnswerItem> comitems;
		private Context context;

		public ComAdapter(List<AnswerItem> comitems, Context context) {
			// TODO Auto-generated constructor stub
			this.comitems = comitems;
			this.context = context;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return comitems.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return comitems.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			ViewHodler hodler;
			if (arg1 == null) {
				hodler = new ViewHodler();
				arg1 = LayoutInflater.from(context).inflate(R.layout.comitem,
						null);
				hodler.name = (TextView) arg1.findViewById(R.id.name);
				hodler.agree = (TextView) arg1.findViewById(R.id.agree);
				hodler.content = (TextView) arg1.findViewById(R.id.content);
				hodler.imageView = (ImageView) arg1.findViewById(R.id.userpic);
				arg1.setTag(hodler);

			} else {
				hodler = (ViewHodler) arg1.getTag();
			}
			ImageGetter getter = new ImageGetter() {

				@Override
				public Drawable getDrawable(String arg0) {
					// TODO Auto-generated method stub
					Drawable drawable = new Drawable() {

						@Override
						public void setColorFilter(ColorFilter arg0) {
							// TODO Auto-generated method stub

						}

						@Override
						public void setAlpha(int arg0) {
							// TODO Auto-generated method stub

						}

						@Override
						public int getOpacity() {
							// TODO Auto-generated method stub
							return 0;
						}

						@Override
						public void draw(Canvas arg0) {
							// TODO Auto-generated method stub

						}
					};
					drawable.setBounds(0, 0, 0, 0);
					return drawable;
				}
			};
			hodler.content.setText(Html.fromHtml(comitems.get(arg0)
					.getAnswer_content(), getter, null));
			hodler.agree.setText(comitems.get(arg0).getAgree_count());
			GetUserNamImage getUserNamImage = new GetUserNamImage(context);
			getUserNamImage.getuserinfo(comitems.get(arg0).getUid(),
					hodler.name, hodler.imageView);
			return arg1;
		}

		class ViewHodler {
			private TextView agree, name, content;
			private ImageView imageView;
		}
	}

}
