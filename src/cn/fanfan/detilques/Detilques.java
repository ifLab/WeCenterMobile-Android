package cn.fanfan.detilques;

import java.util.ArrayList;
import java.util.List;

import cn.fanfan.main.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class Detilques extends Activity {
	private ListView comlist;
	private List<String> comlists;
	private ComAdapter adapter;
	private TextView addanswer;
 @Override
   protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	setContentView(R.layout.detilques);
	comlist = (ListView)findViewById(R.id.comlist);
	comlists = new ArrayList<String>();
	comlists.add("qwe");
	comlists.add("asd");
	comlists.add("zxc");
	adapter = new ComAdapter(comlists, this);
	comlist.setAdapter(adapter);
	comlist.setOnItemClickListener(new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.setClass(Detilques.this, Answer.class);
			startActivity(intent);
		}
	});
	addanswer = (TextView)findViewById(R.id.addanswer);
	addanswer.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.setClass(Detilques.this, WriteAnswer.class);
			startActivity(intent);
		}
	});
   }
   class ComAdapter extends BaseAdapter{
	   private List<String> comitems;
	   private Context context;
     public ComAdapter(List<String> comitems,Context context) {
		// TODO Auto-generated constructor stub
    	 this.comitems = comitems;
    	 this.context = context;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return comitems.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return comitems.get(arg0);
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
				arg1 = LayoutInflater.from(context).inflate(R.layout.comitem, null);
				hodler.name = (TextView)arg1.findViewById(R.id.name);
				arg1.setTag(hodler);
			
		} else {
			hodler = (ViewHodler)arg1.getTag();
		}
		hodler.name.setText(comitems.get(arg0));
		return arg1;
	}
	class ViewHodler{
		private TextView tag,name,com;
		private ImageView imageView;
	}  
   }

}
