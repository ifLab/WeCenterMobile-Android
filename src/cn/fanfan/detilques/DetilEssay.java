package cn.fanfan.detilques;

import cn.fanfan.main.R;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class DetilEssay extends Activity implements OnClickListener{
	TextView agree;
	TextView disagree;
	Button addcom;
	 static int tagagree = 0,tagdisagree = 0;
	// private ComListView listView;
  @Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	setContentView(R.layout.detilessay);
	 agree = (TextView)findViewById(R.id.agree);
	 disagree = (TextView)findViewById(R.id.disagree);
	 addcom = (Button)findViewById(R.id.addcom);
	 agree.setOnClickListener(this);
	 disagree.setOnClickListener(this);
	 addcom.setOnClickListener(this);
  }

@Override
public void onClick(View arg0) {
	// TODO Auto-generated method stub
	 Drawable nav_up;
	 Drawable nav_down;
	switch (arg0.getId()) {
	  
	case R.id.agree:
		
		if (tagagree==0 && tagdisagree==0) {
		     nav_up=getResources().getDrawable(R.drawable.ic_vote_checked);
		     nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
		     agree.setCompoundDrawables(nav_up, null,null , null);
			 tagagree = 1;
		} else if(tagagree==0 && tagdisagree==1){
			 nav_up=getResources().getDrawable(R.drawable.ic_vote_checked);
		     nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
		     agree.setCompoundDrawables(nav_up, null,null , null);
			 tagagree = 1;
			 nav_down=getResources().getDrawable(R.drawable.ic_vote_down_normal);
			 nav_down.setBounds(0, 0, nav_down.getMinimumWidth(), nav_down.getMinimumHeight());
		     disagree.setCompoundDrawables(nav_down, null,null , null);
			 tagdisagree = 0;
		}else{
			nav_up=getResources().getDrawable(R.drawable.ic_vote_normal);
			nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
		    agree.setCompoundDrawables(nav_up, null,null , null);
			tagagree = 0;
		}
		
		break;
    case R.id.disagree:
    	if (tagdisagree==0 && tagagree ==0) {
		     nav_down=getResources().getDrawable(R.drawable.ic_vote_down_checked);
		     nav_down.setBounds(0, 0, nav_down.getMinimumWidth(), nav_down.getMinimumHeight());
			 disagree.setCompoundDrawables(nav_down, null,null , null);
			 tagdisagree = 1;
		} else if(tagdisagree==0 && tagagree ==1){
			 nav_down=getResources().getDrawable(R.drawable.ic_vote_down_checked);
		     nav_down.setBounds(0, 0, nav_down.getMinimumWidth(), nav_down.getMinimumHeight());
			 disagree.setCompoundDrawables(nav_down, null,null , null);
			 tagdisagree = 1;
			 nav_up=getResources().getDrawable(R.drawable.ic_vote_normal);
			 nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
		     agree.setCompoundDrawables(nav_up, null,null , null);
			 tagagree = 0;
		}else{
			 nav_down=getResources().getDrawable(R.drawable.ic_vote_down_normal);
		     nav_down.setBounds(0, 0, nav_down.getMinimumWidth(), nav_down.getMinimumHeight());
			 disagree.setCompoundDrawables(nav_down, null,null , null);
			 tagdisagree = 0;
		}
		
    	break;
    case R.id.addcom:
    	Intent intent = new Intent();
    	intent.setClass(this, ComList.class);
    	startActivity(intent);
	default:
		break;
	}
}
 
}
