package cn.fanfan.widget;

import cn.fanfan.main.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

public class LoadMoreList extends ListView implements OnScrollListener {
	private View footer; // 底部加载更多视图
	private int mTotalItemCount;// 总数量；
	private int mLastVisibleItem;// 最后一个可见的item；
	private boolean mIsLoading;// 正在加载；
	private OnLoadMoreListener mLoadMoreListener;// 加载更多回调接口

	public LoadMoreList(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public LoadMoreList(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public LoadMoreList(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		this.mLastVisibleItem = firstVisibleItem + visibleItemCount;
		this.mTotalItemCount = totalItemCount;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		if (mTotalItemCount == mLastVisibleItem
				&& scrollState == SCROLL_STATE_IDLE) {
			if (!mIsLoading) {
				mIsLoading = true;
				footer.findViewById(R.id.ll_loadlist_footer).setVisibility(
						View.VISIBLE);
				// 加载更多
				mLoadMoreListener.onLoad();
			}
		}
	}

	/**
	 * 添加底部加载提示布局到listview
	 * 
	 * @param context
	 */
	private void initView(Context context) {
		LayoutInflater inflater = LayoutInflater.from(context);
		footer = inflater.inflate(R.layout.widget_loadlist_footer, null);
		footer.findViewById(R.id.ll_loadlist_footer).setVisibility(View.GONE);
		this.addFooterView(footer);
		this.setOnScrollListener(this);
	}

	/**
	 * 加载完毕
	 */
	public void loadComplete() {
		mIsLoading = false;
		footer.findViewById(R.id.ll_loadlist_footer).setVisibility(View.GONE);
	}

	// 设置监听
	public void setOnLoadMoreListener(OnLoadMoreListener mLoadMoreListener) {
		this.mLoadMoreListener = mLoadMoreListener;
	}

	// 加载更多数据的回调接口
	public interface OnLoadMoreListener {
		public void onLoad();
	}

}
