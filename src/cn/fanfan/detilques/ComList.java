package cn.fanfan.detilques;


import cn.fanfan.main.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Intents.Insert;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;

@SuppressWarnings("unused")
public class ComList extends Activity implements OnItemClickListener{
	private Dialog aDialog;
	private ListView comlist;
      @Override
    protected void onCreate(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.comlist);
    	
    	comlist = (ListView) findViewById(R.id.comlist);
    	comlist.setAdapter(new ComAdapter(this));
    	comlist.setOnItemClickListener(this);
    }

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		showDialog();
	}
     private class ComAdapter  extends BaseAdapter{
    	 private Context context;
        public ComAdapter(Context context) {
			// TODO Auto-generated constructor stub
        	this.context = context;
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 10;
		} 

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			ViewHodler hodler;
			 if(arg1 == null){
				    hodler = new ViewHodler();			
					arg1 = LayoutInflater.from(context).inflate(R.layout.comdetil, null);
					//hodler.name = (TextView)arg1.findViewById(R.id.newname);
					arg1.setTag(hodler);
				
			} else {
				hodler = (ViewHodler)arg1.getTag();
			}
			//hodler.name.setText(newitems.get(arg0));
			return arg1;
		}
		class ViewHodler{
			private TextView tag,name,com;
			private ImageView imageView;
		}   
     }
 	private void showDialog() {
 		aDialog = new Dialog(this);
 		LayoutInflater inflater = LayoutInflater.from(this);
 		View view = inflater.inflate(R.layout.dialog, null);
 		Button zan = (Button) view.findViewById(R.id.zan);
 		Button back = (Button) view.findViewById(R.id.backanswer);
 		Button cancel = (Button) view.findViewById(R.id.cancel);
 		aDialog.setTitle("Ñ¡Ôñ");
 		aDialog.setCanceledOnTouchOutside(true);
 		aDialog.setContentView(view);
 		zan.setOnClickListener(new Click());
 		back.setOnClickListener(new Click());
 		cancel.setOnClickListener(new Click());
 		aDialog.show();
	}
    class Click implements android.view.View.OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			switch (arg0.getId()) {
			case R.id.zan:
				aDialog.hide();
				break;
            case R.id.backanswer:
            	aDialog.hide();
            	break;
            case R.id.cancel:
            	aDialog.hide();
            	break;
			default:
				break;
			}
		}
    	
    }
}
