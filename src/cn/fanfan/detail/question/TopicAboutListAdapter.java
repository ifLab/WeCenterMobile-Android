package cn.fanfan.detail.question;

import org.json.JSONArray;
import org.json.JSONException;

import cn.fanfan.main.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TopicAboutListAdapter extends BaseAdapter{
	
	private Context context;
	private JSONArray array;
	
	public TopicAboutListAdapter(Context context,JSONArray array) {
		// TODO Auto-generated constructor stub
		this.array = array;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return array.length();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}
	public class ViewHolder {
		public TextView tag;
	}
	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (arg1 == null) {
			holder = new ViewHolder();
			arg1 = LayoutInflater.from(context).inflate(R.layout.question_topic_list, null);
			holder.tag = (TextView)arg1.findViewById(R.id.topictag);
			arg1.setTag(holder);
		} else {
           holder = (ViewHolder) arg1.getTag();
		}
		try {
			holder.tag.setText(array.getJSONObject(arg0).getString("topic_title"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return arg1;
	}

	
}
