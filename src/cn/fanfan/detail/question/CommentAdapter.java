package cn.fanfan.detail.question;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.fanfan.common.GetUserNamImage;
import cn.fanfan.common.GetUserNamImage.onLoaderListener;
import cn.fanfan.main.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
	public View getView(final int arg0, View arg1, ViewGroup arg2) {
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
			arg1.setTag(hodler);

		} else {
			hodler = (ViewHodler) arg1.getTag();
		}
		hodler.name.setTag(comitems.get(arg0).getUid()+"name");
		hodler.imageView.setTag(comitems.get(arg0).getUid()+"userimage");
		Pattern p=Pattern.compile("(<img(.*?)>)");		
	    Matcher m=p.matcher(comitems.get(arg0)
					.getAnswer_content());
		String content = m.replaceAll("ͼƬ");
		hodler.content.setText(Html.fromHtml(content));
		hodler.agree.setText(comitems.get(arg0).getAgree_count());
		GetUserNamImage getUserNamImage = new GetUserNamImage(context);
		getUserNamImage.getuserinfo(comitems.get(arg0).getUid(),hodler.name,hodler.imageView,new onLoaderListener() {
	
			@Override
			public void onPicLoader(Bitmap bitmap, ImageView userimage) {
				// TODO Auto-generated method stub
				if (userimage.getTag()!=null && userimage.getTag().equals(comitems.get(arg0).getUid()+"userimage")) {
					if (bitmap != null) {
						userimage.setImageBitmap(bitmap);
					} else {
						userimage.setImageDrawable(context.getResources()
								.getDrawable(R.drawable.logo));;
					}
					
				}
			}

			@Override
			public void onNameLoader(String name, TextView username) {
				// TODO Auto-generated method stub
				if (username.getTag() != null && username.getTag().equals(comitems.get(arg0).getUid()+"name")) {
					username.setText(name);
				}
			}
		});
		return arg1;
	}

	class ViewHodler {
		private TextView agree, name, content;
		private ImageView imageView;
	}
}
