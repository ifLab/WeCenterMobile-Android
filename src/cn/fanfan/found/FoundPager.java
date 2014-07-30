package cn.fanfan.found;

import java.util.ArrayList;
import java.util.List;

import cn.fanfan.detilques.DetilEssay;
import cn.fanfan.detilques.Detilques;
import cn.fanfan.main.R;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class FoundPager extends Fragment {
    private ListView listView;
    private List<String> newlist;
	private NewAdapter adapter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.foundlist, container, false);
		listView = (ListView)rootView.findViewById(R.id.fodlist);
		newlist = new ArrayList<String>();
		newlist.add("qwe");
		newlist.add("asd");
		newlist.add("zxc");
		adapter = new NewAdapter(newlist, getActivity());
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(getActivity(), Detilques.class);
				startActivity(intent);
			}
		});
		return rootView;
	}
	   class NewAdapter extends BaseAdapter{
		   private List<String> newitems;
		   private Context context;
	     public NewAdapter(List<String> comitems,Context context) {
			// TODO Auto-generated constructor stub
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
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			ViewHodler hodler;
			 if(arg1 == null){
				    hodler = new ViewHodler();			
					arg1 = LayoutInflater.from(context).inflate(R.layout.foundques, null);
					hodler.name = (TextView)arg1.findViewById(R.id.newname);
					arg1.setTag(hodler);
				
			} else {
				hodler = (ViewHodler)arg1.getTag();
			}
			hodler.name.setText(newitems.get(arg0));
			return arg1;
		}
		class ViewHodler{
			private TextView tag,name,com;
			private ImageView imageView;
		}  
	   }
}
