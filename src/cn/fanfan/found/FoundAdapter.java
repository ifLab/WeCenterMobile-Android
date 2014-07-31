package cn.fanfan.found;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.fanfan.common.Config;
import cn.fanfan.main.R;
import cn.fanfan.topic.imageload.ImageDownLoader;
import cn.fanfan.userinfo.UserInfoActivity;

public class FoundAdapter extends BaseAdapter{
		   private List<Founditem> newitems;
		   private Context context;
		   private ImageDownLoader imageDownLoader;
	     public FoundAdapter(List<Founditem> comitems,Context context,ImageDownLoader imageDownLoader) {
			// TODO Auto-generated constructor stub
	    	 super();
	    	 this.newitems = comitems;
	    	 this.context = context;
	    	 this.imageDownLoader = imageDownLoader;
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
		public View getView(final int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			ViewHodler hodler;
			final String mImageUrl = Config.getValue("userImageBaseUrl")+newitems.get(arg0).getAvatar_file();
			System.out.println(mImageUrl+"  aasa");
			 if(arg1 == null){
				    hodler = new ViewHodler();			
					arg1 = LayoutInflater.from(context).inflate(R.layout.foundques, null);
					hodler.name = (TextView)arg1.findViewById(R.id.name);
					hodler.question = (TextView)arg1.findViewById(R.id.question);
					hodler.focus_count = (TextView)arg1.findViewById(R.id.focus_count);
					hodler.awsner_count = (TextView)arg1.findViewById(R.id.awsner_count);
					hodler.view_count = (TextView)arg1.findViewById(R.id.view_count);
					hodler.userimage = (ImageView)arg1.findViewById(R.id.userimage);
					arg1.setTag(hodler);
				
			} else {
				hodler = (ViewHodler)arg1.getTag();
			}
			 hodler.userimage.setTag(mImageUrl);
			 hodler.name.setText(newitems.get(arg0).getName());
			 hodler.question.setText(newitems.get(arg0).getQuestion());
			 hodler.awsner_count.setText(String.valueOf(newitems.get(arg0).getAnswer_count()));
			 hodler.focus_count.setText(String.valueOf(newitems.get(arg0).getFocus_count()));
			 hodler.view_count.setText(String.valueOf(newitems.get(arg0).getView_count()));
			 Bitmap bitmap = imageDownLoader.getBitmapFromMemCache(mImageUrl.replaceAll("[^\\w]", ""));
				if (bitmap != null) {
					hodler.userimage.setImageBitmap(bitmap);
				} else {
					hodler.userimage.setImageDrawable(context.getResources()
							.getDrawable(R.drawable.logo));
				}
				hodler.userimage.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View view) {
						// TODO Auto-generated method stub
						Intent intent = new Intent();
						intent.setClass(context, UserInfoActivity.class);
						System.out.println(newitems.get(arg0).getQuestion_id()+" 123");
						intent.putExtra("uid", "13");
						context.startActivity(intent);
					}
				});
			return arg1;
		}
		class ViewHodler{
			private TextView name,question,focus_count,awsner_count,view_count;
			private ImageView userimage;
	}  
}

