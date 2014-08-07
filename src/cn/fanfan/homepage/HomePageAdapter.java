package cn.fanfan.homepage;

import java.util.List;

import cn.fanfan.main.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HomePageAdapter extends ArrayAdapter<HomePageItemModel> {
	private int resourceId;

	public HomePageAdapter(Context context, int itemViewResourceId,
			List<HomePageItemModel> objects) {
		super(context, itemViewResourceId, objects);
		resourceId = itemViewResourceId;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		HomePageItemModel itemModel = getItem(position);
		// 优化ListView性能
		View view;
		ViewHolder viewHolder;
		if (convertView == null) {
			view = LayoutInflater.from(getContext()).inflate(resourceId, null);
			viewHolder = new ViewHolder();
			viewHolder.complexLayout = (LinearLayout) view
					.findViewById(R.id.llsHomeListItemContent);
			viewHolder.avatarImage = (ImageView) view
					.findViewById(R.id.ivHomeListItemAvatar);
			viewHolder.userName = (TextView) view
					.findViewById(R.id.tvHomeListItemName);
			viewHolder.mState = (TextView) view
					.findViewById(R.id.tvHomeListItemState);
			viewHolder.itemTitle = (TextView) view
					.findViewById(R.id.tvHomeListItemTitle);
			viewHolder.bestAnswer = (TextView) view
					.findViewById(R.id.tvHomeListItemBestAnswer);
			viewHolder.agreeCount = (TextView) view
					.findViewById(R.id.tvHomeListItemAgreeCount);
			view.setTag(viewHolder);
		} else {
			view = convertView;
			viewHolder = (ViewHolder) view.getTag();
		}
		viewHolder.avatarImage.setImageResource(itemModel.getAvatarImageId());
		viewHolder.userName.setText(itemModel.getUserName());
		viewHolder.mState.setText(itemModel.getmState());
		viewHolder.itemTitle.setText(itemModel.getItemTitle());
		viewHolder.agreeCount.setText(itemModel.getAgreeCount());
		viewHolder.bestAnswer.setText(itemModel.getBestAnswer());
		// 根据数据类型决定是否隐藏布局的下部分
		if (itemModel.getLayoutType() == HomePageItemModel.LAYOUT_TYPE_SIMPLE) {
			viewHolder.complexLayout.setVisibility(View.GONE);
		}
		return view;
	}

	class ViewHolder {
		LinearLayout complexLayout;
		ImageView avatarImage;
		TextView userName;
		TextView mState;
		TextView itemTitle;
		TextView bestAnswer;
		TextView agreeCount;
	}
}
