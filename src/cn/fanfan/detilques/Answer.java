package cn.fanfan.detilques;


import cn.fanfan.main.R;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Answer extends Activity implements OnClickListener{
	private Dialog aDialog;
	private TextView zanorno;
	private Button addcom;
	private ImageView agree,disagree;
	private static int tag = 0;
   @Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	setContentView(R.layout.answer);
	zanorno = (TextView) findViewById(R.id.zanorno);
	addcom = (Button)findViewById(R.id.addcom);
	addcom.setOnClickListener(this);
	zanorno.setOnClickListener(this);
  }

@Override
public void onClick(View v) {
	// TODO Auto-generated method stub
	Drawable nav_up;
	System.out.println(tag);
	switch (v.getId()) {
	case R.id.zanorno:
		showDialog();
		break;
	case R.id.agree:
		switch (tag) {
		case 0:
			 nav_up=getResources().getDrawable(R.drawable.ic_vote_checked);
			 nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
			 zanorno.setCompoundDrawables(nav_up, null,null , null);
			 tag = 1;
			 aDialog.dismiss();
			break;
		case 1:
			System.out.println("a");
			nav_up=getResources().getDrawable(R.drawable.ic_vote_normal);
			nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
			zanorno.setCompoundDrawables(nav_up, null,null , null);
			tag = 0;
			System.out.println(tag+"    qwe");
			aDialog.dismiss();
			break;
		case 2:
			nav_up=getResources().getDrawable(R.drawable.ic_vote_checked);
			 nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
			 zanorno.setCompoundDrawables(nav_up, null,null , null);
			 tag = 1;
			 aDialog.dismiss();
			 break;
		default:
			break;
		}
		break;
    case R.id.disagree:
    	switch (tag) {
		case 0:
			 nav_up=getResources().getDrawable(R.drawable.ic_vote_down_checked);
	  		 nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
	  		 zanorno.setCompoundDrawables(nav_up, null,null , null);
	  		 tag = 2;
	  		 aDialog.dismiss();
			break;
		case 1:
			nav_up=getResources().getDrawable(R.drawable.ic_vote_down_checked);
	  		nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
	  		zanorno.setCompoundDrawables(nav_up, null,null , null);
	  		tag = 2;
	  		aDialog.dismiss();
	  		break;
		case 2:
			nav_up=getResources().getDrawable(R.drawable.ic_vote_normal);
	  		nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
	  		zanorno.setCompoundDrawables(nav_up, null,null , null);
	  		tag =0;
	  		aDialog.dismiss();
	  		break;
		default:
			break;
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
private void showDialog() {
		aDialog = new Dialog(this);
		LayoutInflater inflater = LayoutInflater.from(this);
		View view = inflater.inflate(R.layout.agranddis, null);
		agree = (ImageView)view.findViewById(R.id.agree);
		disagree = (ImageView)view .findViewById(R.id.disagree);
		aDialog.setTitle("Ñ¡Ôñ");
		aDialog.setCanceledOnTouchOutside(true);
		aDialog.setContentView(view);
		agree.setOnClickListener(this);
		disagree.setOnClickListener(this);
		aDialog.show();
}

}
