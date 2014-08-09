package cn.fanfan.topic;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

import cn.fanfan.common.Config;
import cn.fanfan.detilques.Answer;
import cn.fanfan.main.R;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class TopicDetailSecond extends Fragment {

	private String topic_id;
	private ArrayList<BestAnswerModel> datas;
	private ListView listView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.foundlist,
				container, false);
		listView = (ListView)view.findViewById(R.id.fodlist);
		listView.setDividerHeight(10);
		datas = new ArrayList<BestAnswerModel>();
		Bundle bundle = getArguments();
		topic_id = bundle.getString("topic_id");
		getData();
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),Answer.class);
				intent.putExtra("answerid", String.valueOf(datas.get(position).getAnswer_id()));
				startActivity(intent);
			}
			
		});
		return view;
	}
	public void getData() {
		String url = Config.getValue("topic_best_answer");
		RequestParams params = new RequestParams();
		params.put("id", topic_id);
		AsyncHttpClient client = new AsyncHttpClient();
		PersistentCookieStore cookieStore = new PersistentCookieStore(getActivity());
		client.setCookieStore(cookieStore);
		client.get(url, params,new JsonHttpResponseHandler(){

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, responseString, throwable);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, headers, response);
				try {
					String rsm = response.getString("rsm");
					JSONObject jsonObject = new JSONObject(rsm);
					int total_rows = jsonObject.getInt("total_rows");
					if (total_rows == 0) {
						//Toast.makeText(getActivity(), "没有最佳回答", Toast.LENGTH_SHORT).show();
					}else {
						String rows = jsonObject.getString("rows");
						JSONArray jsonArray = new JSONArray(rows);
						for (int i = 0; i < jsonArray.length(); i++) {
							BestAnswerModel bestAnswerModel = new BestAnswerModel();
							JSONObject jsonObject2 = jsonArray.getJSONObject(i);
							String question_info = jsonObject2.getString("question_info");
							JSONObject question_infoJsonObject = new JSONObject(question_info);
							int question_id = question_infoJsonObject.getInt("question_id");
							bestAnswerModel.setQuestion_id(question_id);
							String question_content = question_infoJsonObject.getString("question_content");
							bestAnswerModel.setQuestion_content(question_content);
							String answer_info = jsonObject2.getString("answer_info");
							JSONObject answer_infoJsonObject = new JSONObject(answer_info);
							int answer_id = answer_infoJsonObject.getInt("answer_id");
							bestAnswerModel.setAnswer_id(answer_id);
							String answer_content = answer_infoJsonObject.getString("answer_content");
							bestAnswerModel.setAnswer_content(answer_content);
							int agree_count = answer_infoJsonObject.getInt("agree_count");
							bestAnswerModel.setAgree_count(agree_count);
							int uid = answer_infoJsonObject.getInt("uid");
							bestAnswerModel.setUid(uid);
							String avatar_file = answer_infoJsonObject.getString("avatar_file");
							bestAnswerModel.setAvatar_file(avatar_file);
							datas.add(bestAnswerModel);
						}
						TopicDetailSecondAdapter adapter = new TopicDetailSecondAdapter(getActivity(), datas);
						listView.setAdapter(adapter);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		});
	}
}
