package cn.fanfan.main;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DrawerAdapter extends BaseAdapter {

	private Context context;
	private String[] draweritems;
	private ArrayList<Drawable> drawables;

	public DrawerAdapter(Context context, String[] draweritems,
			ArrayList<Drawable> drawables) {
		super();
		this.context = context;
		this.draweritems = draweritems;
		this.drawables = drawables;
	}

	public void setDrawable(ArrayList<Drawable> drawables) {
		this.drawables = drawables;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return draweritems.length;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return draweritems[arg0];
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.drawer_item, null);
		TextView textView = (TextView) view.findViewById(R.id.draweritem);
		ImageView imageView = (ImageView) view.findViewById(R.id.icon);
		imageView.setImageDrawable(drawables.get(arg0));
		textView.setText(draweritems[arg0]);
		return view;
	}

}
