package cn.fanfan.common;

import java.util.ArrayList;

import cn.fanfan.main.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class MyGridAdapter extends BaseAdapter {
    
	private ArrayList<String> urls;
	private LayoutInflater mLayoutInflater;
	
	public MyGridAdapter(ArrayList<String> urls, Context context) {
		// TODO Auto-generated constructor stub
		this.urls = urls;
		mLayoutInflater = LayoutInflater.from(context);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return urls == null ? 0 : urls.size();
	}

	@Override
	public String getItem(int arg0) {
		// TODO Auto-generated method stub
		return urls.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final MyGridViewHolder viewHolder;
		String url = urls.get(position);
		if (convertView == null) {
			viewHolder = new MyGridViewHolder();
			convertView = mLayoutInflater.inflate(R.layout.gridview_item,
					parent, false);
			viewHolder.imageView = (ImageView) convertView
					.findViewById(R.id.album_image);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (MyGridViewHolder) convertView.getTag();
		}
		
		viewHolder.imageView.setTag(url);
       
		return convertView;
	}
	private class MyGridViewHolder {
		ImageView imageView;
	}
}
