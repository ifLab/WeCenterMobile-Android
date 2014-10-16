package cn.fanfan.topic;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cn.fanfan.common.AsyncImageGet;
import cn.fanfan.common.Config;
import cn.fanfan.common.FanfanSharedPreferences;
import cn.fanfan.common.GlobalVariables;
import cn.fanfan.main.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class TopicDtailFirstFragment extends Fragment {
	private String uid;
	private ImageView imageView;
	private TextView topic_about_topic_tag;
	private TextView topic_about_topic_detail;
	private TextView topic_about_followers;
	private Button topic_about_follow;
	private String topic_id;
	private int has_focus;
	private int isFocus;
	private ProgressBar pb_change_follow;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.topic_about, container, false);
		Bundle bundle = getArguments();
		topic_id = bundle.getString("topic_id");
		isFocus = bundle.getInt("isFocus");
		init(view);
		getTopicDetailFirst(topic_id);
		return view;
	}

	private void init(View view) {
		// TODO Auto-generated method stub
		FanfanSharedPreferences sharedPreferences = new FanfanSharedPreferences(
				getActivity());
		uid = sharedPreferences.getUid("1");
		imageView = (ImageView) view.findViewById(R.id.topic_about_avatar1);
		topic_about_topic_tag = (TextView) view
				.findViewById(R.id.topic_about_topic_tag);
		topic_about_topic_detail = (TextView) view
				.findViewById(R.id.topic_about_topic_detail);
		topic_about_followers = (TextView) view
				.findViewById(R.id.topic_about_followers);
		topic_about_follow = (Button) view
				.findViewById(R.id.topic_about_follow);
		pb_change_follow = (ProgressBar) view
				.findViewById(R.id.pb_change_follow);
		if (isFocus == GlobalVariables.HOT_TOPIC) {
			topic_about_follow.setVisibility(View.GONE);
		}
		topic_about_follow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pb_change_follow.setVisibility(View.VISIBLE);
				follow("cancle");
			}
		});

	}

	public void follow(String type) {
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("uid", uid);
		params.put("topic_id", topic_id);
		if (has_focus == 1) {
			params.put("type", "cancel");
		}
		String url = Config.getValue("TopicDtailFirst");
		client.post(url, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(new String(arg2));
					int errno = jsonObject.getInt("errno");
					String err = jsonObject.getString("err");
					if (errno == 1) {
						String rsm = jsonObject.getString("rsm");
						if (has_focus == 1) {
							has_focus = 0;
						} else {
							has_focus = 1;
						}
						setTopicFollow();
					} else {
						Toast.makeText(getActivity(), err + "请重试！",
								Toast.LENGTH_SHORT).show();
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

	public void getTopicDetailFirst(String topic_id) {
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("uid", uid);
		params.put("topic_id", topic_id);
		client.get(Config.getValue("Topic"), params,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						// TODO Auto-generated method stub
						try {
							JSONObject jsonObject = new JSONObject(new String(
									arg2));
							int errno = jsonObject.getInt("errno");
							String err = jsonObject.getString("err");
							if (errno == 1) {
								String rsm = jsonObject.getString("rsm");
								JSONObject jsonObject2 = new JSONObject(rsm);
								String topic_title = jsonObject2
										.getString("topic_title");
								String topic_description = jsonObject2
										.getString("topic_description");
								String topic_pic = jsonObject2
										.getString("topic_pic");
								String focus_count = jsonObject2
										.getString("focus_count");
								has_focus = jsonObject2.getInt("has_focus");
								topic_about_topic_tag.setText(topic_title);
								topic_about_topic_detail
										.setText(topic_description + " ");
								topic_about_followers.setText(focus_count
										+ "人关注");
								if (!topic_pic.equals("")) {
									(new AsyncImageGet(Config
											.getValue("imageBaseUrl")
											+ topic_pic, imageView)).execute();
								}
								setTopicFollow();
							} else {
								Toast.makeText(getActivity(), err,
										Toast.LENGTH_SHORT).show();
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

	private void setTopicFollow() {
		pb_change_follow.setVisibility(View.GONE);
		if (has_focus == 1) {
			topic_about_follow
					.setBackgroundResource(R.drawable.btn_silver_normal);
			topic_about_follow.setText("取消关注");
			topic_about_follow.setTextColor(getActivity().getResources()
					.getColor(R.color.text_color_gray));
		} else {
			topic_about_follow
					.setBackgroundResource(R.drawable.btn_green_normal);
			topic_about_follow.setText("关注");
			topic_about_follow.setTextColor(getActivity().getResources()
					.getColor(R.color.text_color_white));
		}
	}
}
