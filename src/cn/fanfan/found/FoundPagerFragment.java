package cn.fanfan.found;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cn.fanfan.asking.FileUtils;
import cn.fanfan.common.Config;
import cn.fanfan.detail.essay.EssayDetailActivity;
import cn.fanfan.detail.question.QuestionDetailActivity;
import cn.fanfan.main.R;
import cn.fanfan.topic.imageload.ImageDownLoader;
import cn.fanfan.topic.imageload.ImageDownLoader.onImageLoaderListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class FoundPagerFragment extends Fragment {
	private int totalItem;
	private boolean isFirstEnter;
	private int mFirstVisibleItem;
	private int mVisibleItemCount;
    private ListView listView;
    private List<FoundItem> newlist;
	private FoundAdapter adapter;
	private int currentPage = 1;
	private ImageDownLoader imageDownLoader;
	private int total_row;
	private LinearLayout footerLinearLayout;
	private TextView footText,tvHomePageLoading;
	private Bundle bundle;
	private String type;
	private String commend;
	private static String TAG = "FoundPage";
	private String pagrtag = "";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.found_list, container, false);
		listView = (ListView)rootView.findViewById(R.id.fodlist);
		tvHomePageLoading = (TextView) rootView
				.findViewById(R.id.tvHomePageLoading);
		
		newlist = new ArrayList<FoundItem>();
		imageDownLoader = new ImageDownLoader(getActivity());
		footerLinearLayout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.next_page_footer,null);
		footText = (TextView)footerLinearLayout.findViewById(R.id.footer_text);	
		bundle = getArguments();
		type = bundle.getString("type");
		commend = bundle.getString("commend");
		isFirstEnter = true;
		listView.addFooterView(footerLinearLayout, "", false);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent();
				// TODO Auto-generated method stub
				if (newlist.get(arg2).getType().equals("question")) {
					intent.putExtra("questionid", newlist.get(arg2).getQuestion_id());
					intent.setClass(getActivity(), QuestionDetailActivity.class);
					
				} else {
					intent.putExtra("eid", newlist.get(arg2).getQuestion_id());
					intent.setClass(getActivity(), EssayDetailActivity.class);
				}
				
				startActivity(intent);
			}
		});
		listView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE&&(mFirstVisibleItem + mVisibleItemCount == totalItem)) {
					if (total_row == 10) {
					     Log.v(TAG, "ss");
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
			}
			final ArrayList<String> thumbs = newlist.get(i).getThumbs();
			GridView gridView = (GridView)  listView.findViewWithTag(newlist.get(i).getQuestion_id()+"gridview");
            if (thumbs != null && thumbs.size() >0 ) {
				for (int j = 0; j < thumbs.size(); j++) {
					String thumb = thumbs.get(j);
					final ImageView imageView = (ImageView)gridView.findViewWithTag(thumb);
					imageDownLoader.getBitmap(thumb, new onImageLoaderListener() {
						
						@Override
						public void onImageLoader(Bitmap bitmap, String url) {
							// TODO Auto-generated method stub
							imageView.setImageBitmap(bitmap);
						}
					});
				}
			} 
		}
	}
	private void getInformation(String page){
		if (!pagrtag.equals(page)) {
			pagrtag = page;
			RequestParams params = new RequestParams();
			//String url = Config.getValue("FoundList");
			//TODO 
			String url = "http://w.hihwei.com/?/api/explore_v2/";
			AsyncHttpClient client = new AsyncHttpClient();
			Log.v(FoundPagerFragment.TAG, page+type);
			params.put("page", page);
			params.put("sort_type", type);
			params.put("is_recommend", commend);
			client.get(url, params,new AsyncHttpResponseHandler(){
				@Override
				public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
					// TODO Auto-generated method stub
					String string = new String(arg2);
					try {
						JSONObject all = new JSONObject(string);
						JSONObject rsm = all.getJSONObject("rsm");
			            total_row = rsm.getInt("total_rows");
			            if (total_row < 10) {
							listView.removeFooterView(footerLinearLayout);
						}
			            JSONArray rows = rsm.getJSONArray("rows");
	                        for (int i = 0; i < rows.length(); i++) {
	                        	ArrayList<String> urls = new ArrayList<String>();
	                        	ArrayList<String> thumbs = new ArrayList<String>(); 
	                        	ArrayList<String> nums = new ArrayList<String>();
	                        	FoundItem founditem = null;
	                        	JSONObject jsonObject = rows.getJSONObject(i);
								int inttga = 0;
								founditem = new FoundItem();
								String post_type = jsonObject.getString("post_type");
								founditem.setType(post_type);
								if (post_type.equals("question")) {
									founditem.setQuestion_id(jsonObject.getString("question_id"));
									founditem.setQuestion(jsonObject.getString("question_content"));
									String detail = jsonObject.getString("question_detail");
									Pattern pattern = Pattern.compile("\\[attach(.*?)attach\\]");
									Matcher matcher = pattern.matcher(detail);
									while (matcher.find()) {
										Pattern p = Pattern.compile("[0-9]{1,}");
                                        Matcher m = p.matcher(matcher.group());
                                        while (m.find()) {
											nums.add(m.group());
										}
									 }
									if (nums.size() > 0) {
										JSONObject attachs = jsonObject.getJSONObject("attachs");
										for (int j = 0; j < nums.size(); j++) {
											JSONObject attach = attachs.getJSONObject(nums.get(j));
											urls.add(attach.getString("attachment"));
											thumbs.add(attach.getString("thumb"));
										}
									}
									founditem.setUrls(urls);
									founditem.setThumbs(thumbs);
									int answer_count = jsonObject.getInt("answer_count");
									JSONObject object = jsonObject.getJSONObject("user_info");
									founditem.setAvatar_file(object.getString("avatar_file"));
									founditem.setUid(object.getString("uid"));
									JSONObject answer = jsonObject.getJSONObject("answer");                            
		                            
		                            if (answer_count != 0) {
		                            	JSONObject user_info = answer.getJSONObject("user_info");
									     founditem.setName(user_info.getString("user_name"));
									     inttga = 1;
									} else {
										founditem.setName(object.getString("user_name"));
										 inttga = 0;
									}
		                            founditem.setInttag(inttga);
		                            
								} else {
		                            founditem.setQuestion_id(jsonObject.getString("id"));
		                            founditem.setQuestion(jsonObject.getString("title"));                           
		                            JSONObject object = jsonObject.getJSONObject("user_info");
									founditem.setAvatar_file(object.getString("avatar_file"));
									founditem.setUid(object.getString("uid"));
									founditem.setName(object.getString("user_name"));
									inttga = 2;
									founditem.setInttag(inttga);
								}
								newlist.add(founditem);
							}
				
					 } catch (Exception e) {
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
					tvHomePageLoading.setVisibility(View.INVISIBLE);
				}

				@Override
				public void onFailure(int arg0, Header[] arg1, byte[] arg2,
						Throwable arg3) {
					// TODO Auto-generated method stub
					
				}
			});
		}
		
	} 
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		FileUtils.deleteDir(FileUtils.SDPATH2);
	}
	public void cancleTask(){
		imageDownLoader.cacelTask();
	}

}
