package cn.fanfan.topic;

import cn.fanfan.main.R;
import android.app.Activity;
import android.os.Bundle;

public class BestAnswersActivity extends Activity{
	
	private int questionID;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.best_answer);
		init();
	}
	private void init() {
		// TODO Auto-generated method stub
		Bundle bundle = getIntent().getBundleExtra("question");
		questionID = bundle.getInt("question_id");
		String question_content = bundle.getString("question_content");
		String avatar_file = bundle.getString("avatar_file");
		int agree_count = bundle.getInt("agree_count");
		String answer_content = bundle.getString("answer_content");
		//Toast.makeText(BestAnswers.this, questionID, Toast.LENGTH_SHORT).show();
	}
}
