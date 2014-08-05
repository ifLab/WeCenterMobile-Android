package cn.fanfan.detilques;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.fanfan.common.GetUserNamImage;
import cn.fanfan.common.GetUserNamImage.onLoaderListener;
import cn.fanfan.main.R;

public class ComListAdapter extends BaseAdapter {
	private Context context;
	private List<Comitem> comitems;

	public ComListAdapter(Context context, List<Comitem> comitems) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.comitems = comitems;
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

			arg1 = LayoutInflater.from(context)
					.inflate(R.layout.comdetil, null);
			hodler.name = (TextView) arg1.findViewById(R.id.username);
			hodler.imageView = (ImageView) arg1.findViewById(R.id.userimg);
			hodler.backname = (TextView) arg1.findViewById(R.id.backname);
			hodler.com = (TextView) arg1.findViewById(R.id.comcontent);
			hodler.time = (TextView) arg1.findViewById(R.id.time);
			hodler.tag = (TextView) arg1.findViewById(R.id.tag);

			arg1.setTag(hodler);

		} else {
			hodler = (ViewHodler) arg1.getTag();
		}
		hodler.tag.setVisibility(View.INVISIBLE);
		hodler.backname.setVisibility(View.INVISIBLE);
		hodler.imageView.setTag(comitems.get(arg0).getUid() + "image");
		hodler.name.setTag(comitems.get(arg0).getUid() + "name");
		hodler.name.setText(comitems.get(arg0).getUsername());
		hodler.com.setText(comitems.get(arg0).getComcontent());
		if (!comitems.get(arg0).getBackuid().equals("")
				&& !comitems.get(arg0).getBackname().equals("")) {
				hodler.backname.setText(comitems.get(arg0).getBackname());
				hodler.tag.setVisibility(View.VISIBLE);
				hodler.backname.setVisibility(View.VISIBLE);

		}
		GetUserNamImage getUserNamImage = new GetUserNamImage(context);
		getUserNamImage.getuserinfo(comitems.get(arg0).getUid(), hodler.name,
				hodler.imageView, new onLoaderListener() {

					@Override
					public void onPicLoader(Bitmap bitmap, ImageView userimage) {
						// TODO Auto-generated method stub
						if (userimage.getTag() != null
								&& userimage.getTag().equals(
										comitems.get(arg0).getUid() + "image")) {
							if (bitmap != null) {
								userimage.setImageBitmap(bitmap);
							} else {
								userimage.setImageDrawable(context
										.getResources().getDrawable(
												R.drawable.logo));
							}

						}
					}

					@Override
					public void onNameLoader(String name, TextView username) {
						// TODO Auto-generated method stub

					}
				});
		return arg1;
	}

	class ViewHodler {
		private TextView time, name, com, backname, tag;
		private ImageView imageView;
	}

}
