package cn.fanfan.common;


import java.util.ArrayList;
import uk.co.senab.photoview.PhotoView;
import cn.fanfan.main.R;
import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

public class ShowPic extends Activity {
	private static final String ISLOCKED_ARG = "isLocked";
	
	private ViewPager mViewPager;
	private ArrayList<String> urlspans;
	private int tag;
	
    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);
        mViewPager = (HackyViewPager) findViewById(R.id.view_pager);
		setContentView(mViewPager);
		Intent intent = getIntent();
		urlspans = intent.getStringArrayListExtra("images");
		mViewPager.setAdapter(new SamplePagerAdapter(urlspans));
		
		if (savedInstanceState != null) {
			boolean isLocked = savedInstanceState.getBoolean(ISLOCKED_ARG, false);
			((HackyViewPager) mViewPager).setLocked(isLocked);
		}
		tag = intent.getIntExtra("tag", 0);
		mViewPager.setCurrentItem(tag);
		
	}
    static class SamplePagerAdapter extends PagerAdapter {
    	
    	private ArrayList<String> urlspans;

		public SamplePagerAdapter(ArrayList<String> urlspans) {
			// TODO Auto-generated constructor stub
			this.urlspans =urlspans; 
		}
		@Override
		public int getCount() {
			return urlspans.size();
		}
        @Override
        public int getItemPosition(Object object) {
        	// TODO Auto-generated method stub
        	return POSITION_NONE;
        }
		@Override
		public View instantiateItem(ViewGroup container, int position) {
			PhotoView photoView = new PhotoView(container.getContext());
			photoView.setImageBitmap(BitmapFactory.decodeFile(urlspans.get(position)));
			container.addView(photoView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

			return photoView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

	}

}
