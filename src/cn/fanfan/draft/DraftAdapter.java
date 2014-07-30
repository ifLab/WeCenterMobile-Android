package cn.fanfan.draft;

import java.util.ArrayList;

import cn.fanfan.main.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DraftAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<DraftModel> draftModels;

	public DraftAdapter(Context context) {
		super();
		this.context = context;
		//this.draftModels = draftModels;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 10;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHodler viewHodler;
		if (convertView == null) {
			viewHodler = new ViewHodler();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.listitem_draft, null);
			viewHodler.draftUsr = (TextView) convertView
					.findViewById(R.id.draft_title);
			viewHodler.yourDraft = (TextView) convertView
					.findViewById(R.id.your_draft);
			convertView.setTag(viewHodler);
		} else {
			viewHodler = (ViewHodler) convertView.getTag();
		}
		return convertView;
	}

	class ViewHodler {
		private TextView draftUsr;
		private TextView yourDraft;
	}
}
