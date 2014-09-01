package cn.fanfan.homepage;

import java.util.List;

import com.loopj.android.image.SmartImageView;

import cn.fanfan.detailessay.DetailEssayActivity;
import cn.fanfan.detailquestion.AnswerActivity;
import cn.fanfan.detailquestion.DetailQuestionActivity;
import cn.fanfan.main.R;
import cn.fanfan.userinfo.UserInfoActivity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HomePageAdapter extends ArrayAdapter<HomePageItemModel> {
	private int resourceId;
	private Context context;

	public HomePageAdapter(Context context, int itemViewResourceId,
			List<HomePageItemModel> objects) {
		super(context, itemViewResourceId, objects);
		this.context = context;
		resourceId = itemViewResourceId;

	}

	public View getView(int position, View convertView, ViewGroup parent) {
		final HomePageItemModel itemModel = getItem(position);
		// 优化ListView性能
		View view;
		ViewHolder viewHolder;
		if (convertView == null) {
			view = LayoutInflater.from(getContext()).inflate(resourceId, null);
			viewHolder = new ViewHolder();
			viewHolder.llItemLayout = (LinearLayout) view
					.findViewById(R.id.llItemLayout);
			viewHolder.complexLayout = (LinearLayout) view
					.findViewById(R.id.llHomeListItemContent);
			if (itemModel.getLayoutType() == HomePageItemModel.LAYOUT_TYPE_SIMPLE) {
				viewHolder.complexLayout.setVisibility(View.GONE);
			} else {
				viewHolder.complexLayout.setVisibility(View.VISIBLE);
			}
			viewHolder.avatar = (FrameLayout) view.findViewById(R.id.flAvatar);
			viewHolder.avatarImage = (SmartImageView) view
					.findViewById(R.id.ivHomeListItemAvatar);
			viewHolder.userName = (TextView) view
					.findViewById(R.id.tvHomeListItemName);
			viewHolder.action = (TextView) view
					.findViewById(R.id.tvHomeListIteAction);
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
			if (itemModel.getLayoutType() == HomePageItemModel.LAYOUT_TYPE_SIMPLE) {
				viewHolder.complexLayout.setVisibility(View.GONE);
			} else {
				viewHolder.complexLayout.setVisibility(View.VISIBLE);
			}
		}
		// 将数据设置到相应的View上
		viewHolder.avatarImage.setImageUrl(itemModel.getAvatarUrl());
		viewHolder.agreeCount.setText(itemModel.getAgreeCount() + " ");
		viewHolder.action.setText(itemModel.getAction());
		viewHolder.userName.setText(itemModel.getUserName());
		viewHolder.itemTitle.setText(itemModel.getItemTitle());
		String replacAnswer = itemModel.getBestAnswer().replaceAll(
				"<img [^>]*>", "图片");
		viewHolder.bestAnswer.setText(replacAnswer);
		// 设置各个对象的监听事件
		viewHolder.llItemLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// TODO Auto-generated method stub
				if (itemModel.getAction().equals("发布该文章")
						|| itemModel.getAction().equals("赞同该文章")) {
					Intent mIntent = new Intent(context, DetailEssayActivity.class);
					mIntent.putExtra("eid",
							Integer.toString(itemModel.getItemTitleUid()));
					context.startActivity(mIntent);
				} else {
					Intent mIntent = new Intent(context, DetailQuestionActivity.class);
					mIntent.putExtra("questionid",
							Integer.toString(itemModel.getItemTitleUid()));
					context.startActivity(mIntent);
				}
			}
		});
		viewHolder.avatarImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				UserInfoActivity.actionStar(context,
						Integer.toString(itemModel.getUserUid()));
			}
		});
		viewHolder.userName.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				UserInfoActivity.actionStar(context,
						Integer.toString(itemModel.getUserUid()));
			}
		});
		viewHolder.itemTitle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (itemModel.getAction().equals("发布该文章")
						|| itemModel.getAction().equals("赞同该文章")) {
					Intent mIntent = new Intent(context, DetailEssayActivity.class);
					mIntent.putExtra("eid",
							Integer.toString(itemModel.getItemTitleUid()));
					context.startActivity(mIntent);
				} else {
					Intent mIntent = new Intent(context, DetailQuestionActivity.class);
					mIntent.putExtra("questionid",
							Integer.toString(itemModel.getItemTitleUid()));
					context.startActivity(mIntent);
				}
			}
		});
		viewHolder.bestAnswer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent mIntent = new Intent(context, AnswerActivity.class);
				mIntent.putExtra("answerid",
						Integer.toString(itemModel.getBestAnswerUid()));
				context.startActivity(mIntent);
			}
		});
		return view;
	}

	class ViewHolder {
		LinearLayout complexLayout, llItemLayout;
		FrameLayout avatar;
		SmartImageView avatarImage;
		TextView userName;
		TextView action;
		TextView itemTitle;
		TextView bestAnswer;
		TextView agreeCount;
	}
}
