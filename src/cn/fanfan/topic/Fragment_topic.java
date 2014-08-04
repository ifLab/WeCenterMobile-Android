package cn.fanfan.topic;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cn.fanfan.common.Config;
import cn.fanfan.common.FanfanSharedPreferences;
import cn.fanfan.common.GlobalVariables;
import cn.fanfan.main.MainActivity;
import cn.fanfan.main.R;
import cn.fanfan.topic.imageload.ImageDownLoader;
import cn.fanfan.topic.imageload.ImageDownLoader.onImageLoaderListener;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class Fragment_topic extends Fragment {

	private List<TopicModel> topicModels;
	private ListView listView;
	private FragmenTopicAdapter adapter;
	private boolean isFirstEnter;
	private int mFirstVisibleItem;
	private int mVisibleItemCount;
	private int totalItem;
	private ImageDownLoader mImageDownLoader;
	private String uid;
	private int per_page = 10;
	private int currentPage = 1;
	private int allPages = 1;
	
	private LinearLayout footerLinearLayout;
	private TextView footText;
	
	private int isFocus;
	private Bundle bundle;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_topic, container, false);
		final FanfanSharedPreferences sharedPreferences = new FanfanSharedPreferences(getActivity());
		//uid = sharedPreferences.getUid("1");
		
		Intent intent = getActivity().getIntent();
		uid = intent.getStringExtra("uid");
		if (uid == null) {
			uid = sharedPreferences.getUid("10");
		}
		
		bundle= getArguments();
		if (bundle == null) {
			isFocus = GlobalVariables.FOCUS_TOPIC;
		}else {
			isFocus = bundle.getInt("isFocus", GlobalVariables.FOCUS_TOPIC);
		}
		
		topicModels = new ArrayList<TopicModel>();
		mImageDownLoader = new ImageDownLoader(getActivity());
		footerLinearLayout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.next_page_footer,null);
		footText = (TextView)footerLinearLayout.findViewById(R.id.footer_text);
		isFirstEnter = true;
		listView = (ListView) view
				.findViewById(R.id.fragment_topic_list);
		
		listView.addFooterView(footerLinearLayout);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (position<topicModels.size()) {
					Intent intent = new Intent(getActivity(), TopicDetail.class);
					intent.putExtra("topic_id", topicModels.get(position).getTopicId());
					intent.putExtra("isFocus", isFocus);
					//System.out.println(topicModels.get(position).getTopicId()+"!!");
					startActivity(intent);
				}
			}
		});
		listView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE&&(mFirstVisibleItem + mVisibleItemCount == totalItem)) {
					if (currentPage<=allPages) {
						getTopicModels(uid);
					}else {
						footText.setText("没有更多数据了");
						//footerLinearLayout.setVisibility(View.GONE);
						//listView.removeFooterView(footerLinearLayout);
					}
				}
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
		});
		getTopicModels(uid);
		return view;
	}

	private void showImage(int firstVisibleItem, int visibleItemCount) {
		// 注：firstVisibleItem + visibleItemCount-1 = 20 1其中包括了footview，这儿一定要小心！
		for (int i = firstVisibleItem; i < firstVisibleItem + visibleItemCount
				- 1; i++) {
			String mImageUrl = topicModels.get(i).getImageUrl();
			//System.out.println(mImageUrl);
			if (!mImageUrl.equals("") ) {
				mImageUrl = Config.getValue("imageBaseUrl")+mImageUrl;
				//System.err.println(mImageUrl);
				final ImageView mImageView = (ImageView) listView
						.findViewWithTag(mImageUrl);
				mImageDownLoader.getBitmap(mImageUrl, new onImageLoaderListener() {

					public void onImageLoader(Bitmap bitmap, String url) {
						//System.out.println(bitmap+")(");
						if (mImageView != null && bitmap != null) {
							mImageView.setImageBitmap(bitmap);
						}
					}
				});
			}else {
				continue;
			}
		}
	}
	
	public void cancleTask(){
		mImageDownLoader.cacelTask();
	}
	
	private void getTopicModels(String uid) {
		RequestParams params = new RequestParams();
		String url = "";
		if (isFocus == GlobalVariables.FOCUS_TOPIC) {
			params.put("uid", uid);
			url = Config.getValue("Focus_topic");
		}else {
			url = Config.getValue("Hot_topic");
		}
		params.put("page", currentPage);
		params.put("per_page", per_page);
		AsyncHttpClient client = new AsyncHttpClient();
		//PersistentCookieStore cookieStore = new PersistentCookieStore(getActivity());
		//client.setCookieStore(cookieStore);
		client.get(url, params, new AsyncHttpResponseHandler() {
			
			
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				//topicModels = new ArrayList<TopicModel>();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				try {
					//System.out.println(new String(arg2));
					JSONObject jsonObject = new JSONObject(new String(arg2));
					int errno = jsonObject.getInt("errno");
					if (errno == -1) {
						String err = jsonObject.getString("err");
						//System.out.println(err);
						Toast.makeText(getActivity(), err, Toast.LENGTH_SHORT).show();
					}else {
						String rsm = jsonObject.getString("rsm");
						JSONObject jsonObject2 = new JSONObject(rsm);
						String total_rows = jsonObject2.getString("total_rows");
						if (currentPage == 1) {
							int totalPages = Integer.parseInt(total_rows);
							int a = totalPages%per_page;
							if (a == 0) {
								allPages = totalPages/per_page;
							}else {
								allPages = totalPages/per_page+1;
								//System.out.println(allPages);
							}
						}
						String topics_list = jsonObject2.getString("rows");
						JSONArray jsonArray = new JSONArray(topics_list);
						for (int i = 0; i < jsonArray.length(); i++) {
							TopicModel topicModel = new TopicModel();
							JSONObject jsonObject3 = jsonArray.getJSONObject(i);
							String topic_id = jsonObject3.getString("topic_id");
							String topic_title = jsonObject3.getString("topic_title");
							String topic_description = jsonObject3.getString("topic_description");
							String topic_pic = jsonObject3.getString("topic_pic");
							topicModel.setTopicId(topic_id);
							topicModel.setTopicSummary(topic_description);
							topicModel.setTopicTitle(topic_title);
							topicModel.setImageUrl(topic_pic);
							topicModels.add(topicModel);
						}
						//System.out.println(topicModels);
						if (currentPage == 1) {
							adapter = new FragmenTopicAdapter(getActivity(),topicModels,mImageDownLoader);
							listView.setAdapter(adapter);
							if (topicModels.size()<per_page) {
								footText.setText("没有更多数据了！");
							}
							currentPage++;
						}else {
							//adapter.add(topicModels);
							//System.out.println("currentPage   "+currentPage+"-----------"+"allpages "+allPages);
							adapter.notifyDataSetChanged();
							currentPage ++;
						}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (bundle != null) {
			((MainActivity) activity).onSectionAttached(getArguments().getInt(
					"position"));
		}
	}
}