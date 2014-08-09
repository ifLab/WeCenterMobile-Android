package cn.fanfan.userinfo;

import java.util.ArrayList;

import cn.fanfan.main.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ArticleAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<ArticleModel> datas;

	public ArticleAdapter() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ArticleAdapter(Context context, ArrayList<ArticleModel> datas) {
		super();
		this.context = context;
		this.datas = datas;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return datas.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return datas.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		if (arg1 == null) {
			viewHolder = new ViewHolder();
			arg1 = LayoutInflater.from(context).inflate(
					R.layout.list_item_article, null);
			viewHolder.textView = (TextView)arg1.findViewById(R.id.article_title);
			viewHolder.messagTextView = (TextView)arg1.findViewById(R.id.article_message);
			arg1.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) arg1.getTag();
		}
		viewHolder.textView.setText(datas.get(arg0).getTitle());
		viewHolder.messagTextView.setText(datas.get(arg0).getMessage());
		return arg1;
	}

	class ViewHolder {
		TextView textView;
		TextView messagTextView;
	}
}
