package cn.fanfan.detilques;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.fanfan.main.R;
import cn.fanfan.topic.TopicDetail;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class TopicAbout extends Activity {
	
	private ListView topiclist;
	private TopicAboutList aboutList;
	private JSONArray array;
	private List<String> ids;
   
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.questopic);
		topiclist  = (ListView)findViewById(R.id.topiclist);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.show();
		ids = new ArrayList<String>();
		Intent intent = getIntent();
		String info = intent.getStringExtra("topic");
		try {
			 array = new JSONArray(info);
			 for (int i = 0; i < array.length(); i++) {
				JSONObject jsonObject = array.getJSONObject(i);
				String id = jsonObject.getString("topic_id");
				ids.add(id);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		aboutList = new TopicAboutList(TopicAbout.this, array);
		topiclist.setAdapter(aboutList);
		topiclist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("topic_id", ids.get(arg2));
				intent.setClass(TopicAbout.this, TopicDetail.class);
				startActivity(intent);
			}
		});
	}
	 public boolean onOptionsItemSelected(MenuItem item) {
			// TODO Auto-generated method stub
			switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				break;

			default:
				break;
			}
			return super.onOptionsItemSelected(item);
		}
}
