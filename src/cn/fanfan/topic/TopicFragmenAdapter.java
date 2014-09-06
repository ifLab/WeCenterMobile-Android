package cn.fanfan.topic;

import java.util.List;

import cn.fanfan.common.Config;
import cn.fanfan.main.R;
import cn.fanfan.topic.imageload.ImageDownLoader;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TopicFragmenAdapter extends BaseAdapter {

	private Context context;
	private List<TopicModel> topicModels;
	private ImageDownLoader mImageDownLoader;

	
	public TopicFragmenAdapter(Context context,List<TopicModel> topicModels,ImageDownLoader mImageDownLoader) {
		super();
		this.context = context;
		this.topicModels = topicModels;
		this.mImageDownLoader = mImageDownLoader;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return topicModels.size();
	}

	public void add(List<TopicModel> topic){
		for (TopicModel topicModel : topic) {
			topicModels.add(topicModel);
		}
	}
	
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return topicModels.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHodler viewHodler;
		final String mImageUrl = Config.getValue("imageBaseUrl")+topicModels.get(position).getImageUrl();
		if (convertView == null) {
			viewHodler = new ViewHodler();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.list_item_topic, null);
			viewHodler.topicTitle = (TextView) convertView
					.findViewById(R.id.topic_tag);
			viewHodler.topicSummary = (TextView) convertView
					.findViewById(R.id.topic1);
			viewHodler.imageView = (ImageView) convertView.findViewById(R.id.avatar);
			convertView.setTag(viewHodler);
		} else {
			viewHodler = (ViewHodler) convertView.getTag();
		}
		viewHodler.imageView.setTag(mImageUrl);
		//viewHodler.imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		viewHodler.topicTitle.setText(topicModels.get(position).getTopicTitle());
		viewHodler.topicSummary.setText(topicModels.get(position).getTopicSummary());
		Bitmap bitmap = mImageDownLoader.getBitmapFromMemCache(mImageUrl
				.replaceAll("[^\\w]", ""));
		if (bitmap != null) {
			viewHodler.imageView.setImageBitmap(bitmap);
		} else {
			viewHodler.imageView.setImageDrawable(context.getResources()
					.getDrawable(R.drawable.ic_topic_default));
		}
		return convertView;
	}

	class ViewHodler {
		private TextView topicTitle;
		private TextView topicSummary;
		private ImageView imageView;
	}
}
