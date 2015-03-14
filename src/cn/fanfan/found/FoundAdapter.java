package cn.fanfan.found;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import cn.fanfan.common.Config;
import cn.fanfan.common.MyGridAdapter;
import cn.fanfan.common.ShowPic;
import cn.fanfan.common.image.SmartImageView;
import cn.fanfan.detail.essay.EssayDetailActivity;
import cn.fanfan.detail.question.QuestionDetailActivity;
import cn.fanfan.main.R;
import cn.fanfan.topic.imageload.ImageDownLoader;
import cn.fanfan.userinfo.UserInfoShowActivity;

public class FoundAdapter extends BaseAdapter {
	private static final String TAG = "FoundAdapter";
	private List<FoundItem> newitems;
	private Context context;

	public FoundAdapter(List<FoundItem> comitems, Context context,
			ImageDownLoader imageDownLoader) {
		// TODO Auto-generated constructor stub

		super();
		this.newitems = comitems;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return newitems.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return newitems.get(arg0);
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
		final String mImageUrl = Config.getValue("userImageBaseUrl")
				+ newitems.get(position).getAvatar_file();
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
		hodler.gridView.setTag(newitems.get(position).getQuestion_id()
				+ "gridview");
		hodler.userimage.setTag(mImageUrl);
		contentView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				// TODO Auto-generated method stub
				if (newitems.get(position).getType().equals("question")) {
					intent.putExtra("questionid", newitems.get(position)
							.getQuestion_id());
					intent.setClass(context, QuestionDetailActivity.class);

				} else {
					intent.putExtra("eid", newitems.get(position)
							.getQuestion_id());
					intent.setClass(context, EssayDetailActivity.class);
				}

				context.startActivity(intent);
			}
		});
		hodler.name.setText(newitems.get(position).getName());
		hodler.question.setText(newitems.get(position).getQuestion());
		switch (newitems.get(position).getInttag()) {
		case 0:
			hodler.tag.setText("发起了问题");
			break;
		case 1:
			hodler.tag.setText("回复了问题");
			break;
		case 2:
			hodler.tag.setText("发表了文章");
			break;
		default:
			break;
		}
		if (!TextUtils.isEmpty(mImageUrl)) {
			hodler.userimage.setImageUrl(mImageUrl);
		} else {
			Log.d(TAG, "imageUrl is null");
		}
		hodler.userimage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(context, UserInfoShowActivity.class);
				intent.putExtra("uid", newitems.get(position).getUid());
				context.startActivity(intent);
			}
		});
		final ArrayList<String> urls = newitems.get(position).getUrls();
		if (urls != null && urls.size() > 0) {
			hodler.gridView.setVisibility(View.VISIBLE);
			hodler.gridView.setAdapter(new MyGridAdapter(newitems.get(position)
					.getThumbs(), context));
			hodler.gridView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					Intent intent = new Intent();
					intent.putStringArrayListExtra("images", urls);
					intent.putExtra("tag", arg2);
					intent.setClass(context, ShowPic.class);
					context.startActivity(intent);
				}
			});
		} else {
			hodler.gridView.setVisibility(View.GONE);
		}
		return contentView;
	}

	class ViewHodler {
		private TextView name, question, tag;
		private SmartImageView userimage;
		private GridView gridView;
	}
}
