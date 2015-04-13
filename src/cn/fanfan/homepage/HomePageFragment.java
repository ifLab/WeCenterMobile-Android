package cn.fanfan.homepage;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import cn.fanfan.common.Config;
import cn.fanfan.common.NetworkState;
import cn.fanfan.main.MainActivity;
import cn.fanfan.main.R;
import cn.fanfan.widget.LoadMoreList;
import cn.fanfan.widget.LoadMoreList.OnLoadMoreListener;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class HomePageFragment extends Fragment implements
		SwipeRefreshLayout.OnRefreshListener, OnLoadMoreListener {
	public static final String TAG = "HomePageFragment";
	// 页数，getData的可选择参数，用于指定获取第几页的数据。默认0，从第0页开始。
	private int mPage = 0;
	// 条数，getData的可选参数，用于指定一次获取数据的条数。默认20，一次获取20条数据
	@SuppressWarnings("unused")
	private int mItem = 20;
	// 当前Fragment所依托的Activity
	private MainActivity mActivity;
	// 判断网络状态
	private NetworkState mNetState;

	private AsyncHttpClient mHttpClient;
	// 数据集合
	private List<HomePageItemModel> itemDataList = new ArrayList<HomePageItemModel>();

	private HomePageAdapter mAdapter;

	private LoadMoreList mListView;

	private SwipeRefreshLayout mSwipeLayout;

	private Bundle bundle;
	private int totalRow;
	// JSON解析的数据
	private int actionCode;

	private int userUid = 1;
	private String userName = "Null";
	private String avatarUrl = "Null";

	private String itemTitle = "Null";
	private int itemTitleUid = 1;

	private String bestAnswer = "Null";
	private int bestAnswerUid = 1;
	private int agreeCount;

	private String action = "没有动态";
	private int layoutType;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View fragmentView = inflater.inflate(R.layout.fragment_homepage,
				container, false);
		mActivity = (MainActivity) getActivity();
		mNetState = new NetworkState();
		mListView = (LoadMoreList) fragmentView
				.findViewById(R.id.lvHomeListView);
		mAdapter = new HomePageAdapter(mActivity, R.layout.list_item_homepage,
				itemDataList);
		mListView.setAdapter(mAdapter);
		mListView.setOnLoadMoreListener(this);
		mSwipeLayout = (SwipeRefreshLayout) fragmentView
				.findViewById(R.id.swipe_container);
		mSwipeLayout.setOnRefreshListener(this);
		// 设置刷新颜色样式
		mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		mSwipeLayout.setRefreshing(true);
		getData(mPage);
		return fragmentView;
	}

	private void getData(int page) {
		if (mNetState == null) {
			mNetState = new NetworkState();
		}
		if (mActivity == null) {
			mActivity = (MainActivity) getActivity();
		}
		if (mHttpClient == null) {
			mHttpClient = new AsyncHttpClient();
		}
		if (mNetState.isNetworkConnected(mActivity)) {
			// 配置Http请求参数
			RequestParams params = new RequestParams();
			params.put("page", page);
			String url = Config.getValue("HomePageUrl");
			PersistentCookieStore mCookieStore = new PersistentCookieStore(
					mActivity);
			mHttpClient.setCookieStore(mCookieStore);
			// 进行Http Get请求数据
			mHttpClient.get(url, params, new AsyncHttpResponseHandler() {
				@Override
				public void onFailure(int arg0, Header[] arg1, byte[] arg2,
						Throwable arg3) {
					// TODO Auto-generated method stub
					// 请求失败后提示用户
					Toast.makeText((MainActivity) getActivity(), "无法获取，请重试！",
							Toast.LENGTH_LONG).show();
				}

				@Override
				public void onSuccess(int arg0, Header[] arg1,
						byte[] responseContent) {
					// TODO Auto-generated method stub
					parseData(responseContent);
					mSwipeLayout.setRefreshing(false);
				}
			});
		} else {
			Toast.makeText((MainActivity) getActivity(), "未连接网络！",
					Toast.LENGTH_LONG).show();
			mSwipeLayout.setRefreshing(false);
		}
	}

	private void parseData(byte[] responseContent) {
		// TODO Auto-generated method stub
		// 请求成功后解析数据
		layoutType = HomePageItemModel.LAYOUT_TYPE_SIMPLE;
		String data = new String(responseContent);
		try {
			// 进行JSON解析数据
			JSONObject all = new JSONObject(data);
			JSONObject rsm = all.getJSONObject("rsm");
			totalRow = (rsm.getInt("total_rows"));
			// 如果请求成功totalRow为0时说明无更多数据了
			if (totalRow == 0) {
				// 已经加载全部的数据
				if (mPage == 0) {
					Toast.makeText(mActivity, "没有东东哦，快去关注别人吧！",
							Toast.LENGTH_LONG).show();
					MainActivity.mNavigationDrawerFragment.selectItem(1);
				} else {
					mPage = mPage - 1;
					Toast.makeText(mActivity, "没有更多数据！", Toast.LENGTH_LONG)
							.show();
					mListView.loadComplete();
				}
			}
			JSONArray rows = rsm.getJSONArray("rows");
			for (int i = 0; i < rows.length(); i++) {
				JSONObject rowsObject = rows.getJSONObject(i);
				// actionCode不同则JSON中数组的对象不同分情况解析
				actionCode = rowsObject.getInt("associate_action");
				// 获取userInfo对象
				JSONObject userInfoObject = rowsObject
						.getJSONObject("user_info");
				userUid = userInfoObject.getInt("uid");
				userName = userInfoObject.getString("user_name");

				if (!TextUtils.isEmpty(userInfoObject.getString("avatar_file"))) {
					avatarUrl = Config.getValue("AvatarPrefixUrl")
							+ userInfoObject.getString("avatar_file");
				} else {
					avatarUrl = "";
				}

				// 根据actionCode不同，不同分情况解析剩下的JSON数据
				switch (actionCode) {
				case 101:
					JSONObject questionInfoObject101 = rowsObject
							.getJSONObject("question_info");
					itemTitle = questionInfoObject101.getString(
							"question_content").trim();
					itemTitleUid = questionInfoObject101.getInt("question_id");
					action = "发布该问题";
					layoutType = HomePageItemModel.LAYOUT_TYPE_SIMPLE;
					break;
				case 105:
					JSONObject questionInfoObject105 = rowsObject
							.getJSONObject("question_info");
					itemTitle = questionInfoObject105.getString(
							"question_content").trim();
					itemTitleUid = questionInfoObject105.getInt("question_id");
					action = "关注该问题";
					layoutType = HomePageItemModel.LAYOUT_TYPE_SIMPLE;
					break;
				case 501:
					JSONObject articleInfoObject501 = rowsObject
							.getJSONObject("article_info");
					itemTitleUid = articleInfoObject501.getInt("id");
					itemTitle = articleInfoObject501.getString("title").trim();
					action = "发布该文章";
					layoutType = HomePageItemModel.LAYOUT_TYPE_SIMPLE;
					break;
				case 502:
					JSONObject articleInfoObject502 = rowsObject
							.getJSONObject("article_info");
					itemTitleUid = articleInfoObject502.getInt("id");
					itemTitle = articleInfoObject502.getString("title").trim();
					action = "赞同该文章";
					layoutType = HomePageItemModel.LAYOUT_TYPE_SIMPLE;
					break;
				case 201:
					JSONObject answerInfoObject201 = rowsObject
							.getJSONObject("answer_info");
					bestAnswerUid = answerInfoObject201.getInt("answer_id");
					bestAnswer = answerInfoObject201
							.getString("answer_content").trim();
					agreeCount = answerInfoObject201.getInt("agree_count");
					JSONObject questionInfoObject201 = rowsObject
							.getJSONObject("question_info");
					itemTitle = questionInfoObject201.getString(
							"question_content").trim();
					itemTitleUid = questionInfoObject201.getInt("question_id");
					action = "回答该问题";
					layoutType = HomePageItemModel.LAYOUT_TYPE_COMPLEX;
					break;
				case 204:
					JSONObject answerInfoObject204 = rowsObject
							.getJSONObject("answer_info");
					bestAnswerUid = answerInfoObject204.getInt("answer_id");
					bestAnswer = answerInfoObject204
							.getString("answer_content").trim();
					agreeCount = answerInfoObject204.getInt("agree_count");
					JSONObject questionInfoObject204 = rowsObject
							.getJSONObject("question_info");
					itemTitle = questionInfoObject204.getString(
							"question_content").trim();
					itemTitleUid = questionInfoObject204.getInt("question_id");
					action = "赞同该回答";
					layoutType = HomePageItemModel.LAYOUT_TYPE_COMPLEX;
					break;
				default:
					break;
				}
				// 加载到ListItemModel
				HomePageItemModel item = new HomePageItemModel(layoutType,
						avatarUrl, userName, userUid, action, itemTitle,
						itemTitleUid, bestAnswer, bestAnswerUid, agreeCount);
				itemDataList.add(item);
				mAdapter.notifyDataSetChanged();
				mSwipeLayout.setRefreshing(false);
				mListView.loadComplete();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			mSwipeLayout.setRefreshing(true);
			Log.i(TAG, "Json解析异常");
		}

	}

	// 实现SwipeRefreshLayout接口，完成刷新操作
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		if (!mListView.isLoading()) {
			itemDataList.clear();
			mPage = 0;
			getData(mPage);
		}
	}

	@Override
	public void onLoad() {
		// TODO Auto-generated method stub
		if (!mSwipeLayout.isRefreshing()) {
			mPage++;
			getData(mPage);
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (bundle != null) {
			((MainActivity) activity).onSectionAttached(getArguments().getInt(
					"position"));
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mHttpClient.cancelAllRequests(true);
	}
	
}
