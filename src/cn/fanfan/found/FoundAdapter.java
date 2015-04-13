package cn.fanfan.found;

import java.util.List;

import bean.Article;
import bean.FoundItem;
import bean.Question;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import cn.fanfan.common.Config;
import cn.fanfan.common.image.SmartImageView;
import cn.fanfan.detail.essay.EssayDetailActivity;
import cn.fanfan.detail.question.QuestionDetailActivity;
import cn.fanfan.main.R;
import cn.fanfan.userinfo.UserInfoShowActivity;

public class FoundAdapter extends BaseAdapter {
	private List<FoundItem> items;
	private Context context;

	public FoundAdapter(List<FoundItem> comitems, Context context) {
		// TODO Auto-generated constructor stub

		super();
		this.items = comitems;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return items.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return items.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(final int position, View contentView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHodler hodler;
		String avatarUrl;

		if (contentView == null) {
			hodler = new ViewHodler();
			contentView = LayoutInflater.from(context).inflate(
					R.layout.found_question, null);
			hodler.name = (TextView) contentView.findViewById(R.id.username);
			hodler.question = (TextView) contentView
					.findViewById(R.id.quescontent);
			hodler.userimage = (SmartImageView) contentView
					.findViewById(R.id.userimage);
			hodler.tag = (TextView) contentView.findViewById(R.id.tag);
			hodler.gridView = (GridView) contentView
					.findViewById(R.id.gridView);
			contentView.setTag(hodler);

		} else {
			hodler = (ViewHodler) contentView.getTag();
		}

		// 判断Object类型
		if (items.get(position) instanceof Article) {
			Article item = (Article) items.get(position);
			hodler.question.setText(item.getTitle());
		} else {
			Question item = (Question) items.get(position);
			hodler.question.setText(item.getQuestion_content());
		}

		hodler.name.setText(items.get(position).getUser_info().getUser_name());
		// 设置头像
		if (items.get(position).getUser_info().getAvatar_file().isEmpty()) {
			avatarUrl = "";
			Log.d("foundAvatarUrl:", avatarUrl);
		} else {
			avatarUrl = Config.getValue("userImageBaseUrl")
					+ items.get(position).getUser_info().getAvatar_file();
			hodler.userimage.setImageUrl(avatarUrl);
		}
		hodler.userimage.setTag(avatarUrl);

		contentView.setOnClickListener(new OnClickListener() {
			//
			@Override
			public void onClick(View view) {
				Intent intent = new Intent();
				if (items.get(position) instanceof Question) {
					Question item = (Question) items.get(position);
					intent.putExtra("questionid",
							String.valueOf(item.getQuestion_id()));
					intent.setClass(context, QuestionDetailActivity.class);
				} else if (items.get(position) instanceof Article) {
					Article item = (Article) items.get(position);
					intent.putExtra("eid", String.valueOf(item.getId()));
					intent.setClass(context, EssayDetailActivity.class);
				}
				context.startActivity(intent);
			}
		});
		hodler.userimage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(context, UserInfoShowActivity.class);
				intent.putExtra("uid", items.get(position).getUser_info()
						.getUid());
				context.startActivity(intent);
			}
		});
		// urls = items.get(position).getUrls();
		// thumbs = items.get(position).getThumbs();
		// if (urls != null && urls.size() > 0) {
		// hodler.gridView.setVisibility(View.VISIBLE);
		// hodler.gridView.setAdapter(new MyGridAdapter(items.get(position)
		// .getThumbs(), context));
		// //
		// if (thumbs != null && thumbs.size() > 0) {
		// for (int j = 0; j < thumbs.size(); j++) {
		// String thumb = thumbs.get(j);
		// final ImageView imageView = (ImageView) hodler.gridView
		// .findViewWithTag(thumb);
		// mLoadImage.getBitmap(thumb, new onImageLoaderListener() {
		//
		// @Override
		// public void onImageLoader(Bitmap bitmap, String url) {
		// // TODO Auto-generated method stub
		// imageView.setImageBitmap(bitmap);
		// }
		// });
		// }
		// }
		// hodler.gridView.setOnItemClickListener(new OnItemClickListener() {
		//
		// @Override
		// public void onItemClick(AdapterView<?> arg0, View arg1,
		// int arg2, long arg3) {
		// // TODO Auto-generated method stub
		// Intent intent = new Intent();
		// intent.putStringArrayListExtra("images", urls);
		// intent.putExtra("tag", arg2);
		// intent.setClass(context, ShowPic.class);
		// context.startActivity(intent);
		// }
		// });
		// } else {
		// hodler.gridView.setVisibility(View.GONE);
		// }
		return contentView;
	}

	class ViewHodler {
		private TextView name, question, tag;
		private SmartImageView userimage;
		private GridView gridView;
	}
}
