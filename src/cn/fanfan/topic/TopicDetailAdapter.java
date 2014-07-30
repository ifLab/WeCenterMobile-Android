package cn.fanfan.topic;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TopicDetailAdapter extends FragmentPagerAdapter {

	
	private final String[] titles = { "话题资料", "精华" };
	private String topic_id;
	
	public TopicDetailAdapter(FragmentManager fm,String topic_id) {
		super(fm);
		this.topic_id = topic_id;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		if (arg0 == 0) {
			TopicDtailFirst topicDtailFirst = new TopicDtailFirst();
			Bundle bundle = new Bundle();
			bundle.putString("topic_id", topic_id);
			topicDtailFirst.setArguments(bundle);
			return topicDtailFirst;
		}else {
			TopicDetailSecond topicDetailSecond = new TopicDetailSecond();
			return topicDetailSecond;
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return titles.length;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return titles[position];
	}
}
