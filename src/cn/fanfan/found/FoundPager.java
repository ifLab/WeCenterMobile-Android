package cn.fanfan.found;

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
import cn.fanfan.detilessay.DetilEssay;
import cn.fanfan.detilques.Detilques;
import cn.fanfan.main.R;
import cn.fanfan.topic.imageload.ImageDownLoader;
import cn.fanfan.topic.imageload.ImageDownLoader.onImageLoaderListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class FoundPager extends Fragment {
	private int totalItem;
	private boolean isFirstEnter;
	private int mFirstVisibleItem;
	private int mVisibleItemCount;
    private ListView listView;
    private List<Founditem> newlist;
	private FoundAdapter adapter;
	private int currentPage = 1;
	private ImageDownLoader imageDownLoader;
	private int total_row;
	private LinearLayout footerLinearLayout;
	private TextView footText;
	private Bundle bundle;
	private String type;
	private String commend;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.foundlist, container, false);
		listView = (ListView)rootView.findViewById(R.id.fodlist);
		listView.setDividerHeight(10);
		newlist = new ArrayList<Founditem>();
		imageDownLoader = new ImageDownLoader(getActivity());
		footerLinearLayout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.next_page_footer,null);
		footText = (TextView)footerLinearLayout.findViewById(R.id.footer_text);	
		bundle = getArguments();
		type = bundle.getString("type");
		commend = bundle.getString("commend");
		System.out.println(type+"````````````````"+commend);
		isFirstEnter = true;
		listView.addFooterView(footerLinearLayout, "", false);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("questionid", newlist.get(arg2).getQuestion_id());
				intent.setClass(getActivity(), Detilques.class);
				startActivity(intent);
			}
		});
		listView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE&&(mFirstVisibleItem + mVisibleItemCount == totalItem)) {
					if (total_row == 10) {
						getInformation(String.valueOf(currentPage));
					}else {
						//footText.setText("没有更多数据了");
						//footerLinearLayout.setVisibility(View.GONE);
						listView.removeFooterView(footerLinearLayout);
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
		getInformation("1");
		return rootView;
	}
	private void showImage(int firstVisibleItem, int visibleItemCount) {
		// 注：firstVisibleItem + visibleItemCount-1 = 20 1其中包括了footview，这儿一定要小心！
		for (int i = firstVisibleItem; i < firstVisibleItem + visibleItemCount
				- 1; i++) {
			String mImageUrl = newlist.get(i).getAvatar_file();
			//System.out.println(mImageUrl);
			if (!mImageUrl.equals("") ) {
				mImageUrl = Config.getValue("userImageBaseUrl")+mImageUrl;
				//System.err.println(mImageUrl);
				final ImageView mImageView = (ImageView) listView
						.findViewWithTag(mImageUrl);
				imageDownLoader.getBitmap(mImageUrl, new onImageLoaderListener() {

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
	private void getInformation(String page){
		RequestParams params = new RequestParams();
		String url = Config.getValue("FoundList");
		AsyncHttpClient client = new AsyncHttpClient();
		params.put("page", page);
		params.put("sort_type", type);
		params.put("is_recommend", commend);
		client.get(url, params,new AsyncHttpResponseHandler(){
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				String string = new String(arg2);
				JSONArray rows = null;
				try {
					JSONObject all = new JSONObject(string);
					JSONObject rsm = all.getJSONObject("rsm");
		            total_row = (rsm.getInt("total_rows"));
		            if (total_row < 10) {
						listView.removeFooterView(footerLinearLayout);
					}
					 rows = rsm.getJSONArray("rows");

				
		            for (int i = 0; i < rows.length(); i++) {
						JSONObject jsonObject = rows.getJSONObject(i);
						Founditem founditem = new Founditem();
						String post_type = jsonObject.getString("post_type");
						if (post_type.equals("question")) {
							founditem.setQuestion_id(jsonObject.getString("question_id"));
							founditem.setQuestion(jsonObject.getString("question_content"));
							int answer_count = jsonObject.getInt("answer_count");
							founditem.setAnswer_count(answer_count);
							JSONObject answer = jsonObject.getJSONObject("answer");                            
                            int inttga = 0;
                            if (answer_count != 0) {
                            	JSONObject user_info = answer.getJSONObject("user_info");
							     founditem.setFocus_count(answer.getInt("anonymous"));
							     founditem.setName(answer.getString("answer_content"));
							     founditem.setAvatar_file(user_info.getString("avatar_file"));
							     founditem.setUid(user_info.getString("uid"));
							     inttga = 1;
							} else {
								  founditem.setFocus_count(0);
								     founditem.setName("");
								     founditem.setAvatar_file("");
								     founditem.setUid("");
								 inttga = 0;
							}
                            founditem.setInttag(inttga);
                            newlist.add(founditem);
						} else {
                            
						}
		           
					} 			
				 } catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						
					}
						
				if (currentPage == 1) {
					adapter = new FoundAdapter(newlist, getActivity(),imageDownLoader);
					listView.setAdapter(adapter);
					currentPage++;
				} else {
					adapter.notifyDataSetChanged();
					currentPage ++;
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				// TODO Auto-generated method stub
				
			}
		});
	} 
	public void cancleTask(){
		imageDownLoader.cacelTask();
	}
}
