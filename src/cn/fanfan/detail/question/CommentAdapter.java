package cn.fanfan.detail.question;

import java.util.ArrayList;
import java.util.List;
import cn.fanfan.common.GetUserNamImage;
import cn.fanfan.common.ShowPic;
import cn.fanfan.common.GetUserNamImage.onLoaderListener;
import cn.fanfan.common.MyGridAdapter;
import cn.fanfan.main.R;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class CommentAdapter extends BaseAdapter {
	private List<AnswerItem> comitems;
	private Context context;

	public CommentAdapter(List<AnswerItem> comitems, Context context) {
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
	public View getView(final int pos, View arg1,  ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHodler hodler;
		if (arg1 == null) {
			hodler = new ViewHodler();
			arg1 = LayoutInflater.from(context).inflate(R.layout.comment_item,
					null);
			hodler.name = (TextView) arg1.findViewById(R.id.name);
			hodler.agree = (TextView) arg1.findViewById(R.id.agree);
			hodler.content = (TextView) arg1.findViewById(R.id.content);
			hodler.imageView = (ImageView) arg1.findViewById(R.id.userpic);
			hodler.gridView = (GridView) arg1.findViewById(R.id.gridView);
			arg1.setTag(hodler);

		} else {
			hodler = (ViewHodler) arg1.getTag();
		}
		hodler.gridView.setTag(comitems.get(pos).getAnswer_id()+"gridview");
		hodler.name.setTag(comitems.get(pos).getUid()+"name");
		hodler.imageView.setTag(comitems.get(pos).getUid()+"userimage");
		hodler.content.setText(Html.fromHtml(comitems.get(pos).getAnswer_content()));
		hodler.agree.setText(comitems.get(pos).getAgree_count());
		
		arg1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("answerid", comitems.get(pos).getAnswer_id());
				intent.setClass(context, AnswerActivity.class);
				context.startActivity(intent);
			}
		});
		GetUserNamImage getUserNamImage = new GetUserNamImage(context);
		getUserNamImage.getuserinfo(comitems.get(pos).getUid(),hodler.name,hodler.imageView,new onLoaderListener() {
	
			@Override
			public void onPicLoader(Bitmap bitmap, ImageView userimage) {
				// TODO Auto-generated method stub
				if (userimage.getTag()!=null && userimage.getTag().equals(comitems.get(pos).getUid()+"userimage")) {
					if (bitmap != null) {
						userimage.setImageBitmap(bitmap);
					} else {
						userimage.setImageDrawable(context.getResources()
								.getDrawable(R.drawable.ic_avatar_default));;
					}
					
				}
			}

			@Override
			public void onNameLoader(String name, TextView username) {
				// TODO Auto-generated method stub
				if (username.getTag() != null && username.getTag().equals(comitems.get(pos).getUid()+"name")) {
					username.setText(name);
				}
			}
			
		});
	    final ArrayList<String> urls = comitems.get(pos).getUrls();
	   
            if (urls != null && urls.size()>0 ) {
			hodler.gridView.setVisibility(View.VISIBLE);
			hodler.gridView.setAdapter(new MyGridAdapter(urls, context));
			hodler.gridView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					Intent intent = new Intent();
					intent.putStringArrayListExtra("images", urls);
		        	intent.putExtra("tag",arg2);
		        	intent.setClass(context, ShowPic.class);
		        	context.startActivity(intent);
				}
			});
		} else {
			hodler.gridView.setVisibility(View.GONE);
		}
		return arg1;
	}

	class ViewHodler {
		private TextView agree, name, content;
		private ImageView imageView;
		private GridView gridView;
	}
}
