package cn.fanfan.detail.essay;

import java.util.List;

import cn.fanfan.common.Config;
import cn.fanfan.detail.question.CommentModel;
import cn.fanfan.main.R;
import cn.fanfan.topic.imageload.ImageDownLoader;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class EssayCommentAdapter extends BaseAdapter {
	private Context context;
	private List<EssayCommentModel> comitems;
	private ImageDownLoader imageDownLoader;

	public EssayCommentAdapter(Context context, List<EssayCommentModel> comitems,
			ImageDownLoader imageDownLoader) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.comitems = comitems;
		this.imageDownLoader = imageDownLoader;
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
		final String mImageUrl = Config.getValue("userImageBaseUrl")
				+ comitems.get(arg0).getAvatarfile();
		if (arg1 == null) {
			hodler = new ViewHodler();

			arg1 = LayoutInflater.from(context)
					.inflate(R.layout.essay_comment, null);
			hodler.name = (TextView) arg1.findViewById(R.id.username);
			hodler.imageView = (ImageView) arg1.findViewById(R.id.userimg);
			hodler.backname = (TextView) arg1.findViewById(R.id.backname);
			hodler.com = (TextView) arg1.findViewById(R.id.comcontent);
			hodler.time = (TextView) arg1.findViewById(R.id.comtime);
			hodler.tag = (TextView) arg1.findViewById(R.id.tag);
			hodler.agree = (TextView)arg1.findViewById(R.id.agreecount);

			arg1.setTag(hodler);

		} else {
			hodler = (ViewHodler) arg1.getTag();
		}
		hodler.tag.setVisibility(View.INVISIBLE);
		hodler.backname.setVisibility(View.INVISIBLE);
		hodler.imageView.setTag(mImageUrl);
		hodler.name.setTag(comitems.get(arg0).getUid() + "name");
		hodler.name.setText(comitems.get(arg0).getUsername());
		hodler.com.setText(comitems.get(arg0).getComcontent());
		if (!comitems.get(arg0).getBackuid().equals("")
				&& !comitems.get(arg0).getBackname().equals("")) {
			hodler.backname.setText(comitems.get(arg0).getBackname());
			hodler.tag.setVisibility(View.VISIBLE);
			hodler.backname.setVisibility(View.VISIBLE);

		}
		hodler.agree.setText(comitems.get(arg0).getAgreecount());
		System.out.println(mImageUrl);
		Bitmap bitmap = imageDownLoader.getCacheBitmap(mImageUrl.replaceAll(
				"[^\\w]", ""));
		System.out.println(bitmap);
		if (bitmap != null) {

			hodler.imageView.setImageBitmap(bitmap);
		} else {
			hodler.imageView.setImageDrawable(context.getResources()
					.getDrawable(R.drawable.logo));
		}
		return arg1;
	}

	class ViewHodler {
		private TextView time, name, com, backname, tag,agree;
		private ImageView imageView;
	}
}
