package cn.fanfan.topic;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.fanfan.common.AsyncImageGet;
import cn.fanfan.common.Config;
import cn.fanfan.main.R;
import cn.fanfan.userinfo.UserInfoActivity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TopicDetailSecondAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<BestAnswerModel> datas;

	public TopicDetailSecondAdapter() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TopicDetailSecondAdapter(Context context,
			ArrayList<BestAnswerModel> datas) {
		super();
		System.out.println(datas);
		System.out.println(datas.size());
		this.context = context;
		this.datas = datas;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return datas.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.listitem_topic_essence,null);
			viewHolder = new ViewHolder();
			viewHolder.essence_title = (TextView)convertView.findViewById(R.id.essence_title);
			viewHolder.essence_image = (ImageView)convertView.findViewById(R.id.essence_image);
			viewHolder.essencejianlve = (TextView)convertView.findViewById(R.id.essencejianlve);
			viewHolder.praise = (TextView)convertView.findViewById(R.id.praise);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.essence_title.setText(datas.get(position).getQuestion_content());
		String answer = datas.get(position).getAnswer_content();
		Pattern p = Pattern.compile("<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>");
		Matcher m = p.matcher(answer);
		while(m.find()){
			answer = m.replaceAll("");
		}
		p = Pattern.compile("<br>");
		m = p.matcher(answer);
		while(m.find()){
			answer = m.replaceAll("\n");
		}
		viewHolder.essencejianlve.setText(answer);
		(new AsyncImageGet(Config.getValue("AvatarPrefixUrl")+datas.get(position).getAvatar_file(), viewHolder.essence_image)).execute();
		//System.out.println(Config.getValue("AvatarPrefixUrl")+datas.get(position).getAvatar_file());
		viewHolder.praise.setText(datas.get(position).getAgree_count()+"");
		viewHolder.essence_image.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				UserInfoActivity.actionStar(context, String.valueOf(datas.get(position).getUid()));
			}
		});
		return convertView;
	}
	
	class ViewHolder{
		TextView essence_title;
		ImageView essence_image;
		TextView essencejianlve;
		TextView praise;
	}
}
