package cn.fanfan.common;



import java.util.ArrayList;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher.OnPhotoTapListener;
import cn.fanfan.main.R;
import cn.fanfan.topic.imageload.FileUtils;
import cn.fanfan.topic.imageload.ImageDownLoader;
import cn.fanfan.topic.imageload.ImageDownLoader.onImageLoaderListener;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
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
	private FileUtils fileUtils;
	private Bitmap bm;
	private ImageDownLoader downLoader;
	
    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);
        mViewPager = (HackyViewPager) findViewById(R.id.view_pager);
		setContentView(mViewPager);
		Intent intent = getIntent();
		downLoader = new ImageDownLoader(this);
		urlspans = intent.getStringArrayListExtra("images");
		mViewPager.setAdapter(new SamplePagerAdapter(urlspans));
		fileUtils = new FileUtils(this);
		if (savedInstanceState != null) {
			boolean isLocked = savedInstanceState.getBoolean(ISLOCKED_ARG, false);
			((HackyViewPager) mViewPager).setLocked(isLocked);
		}
		tag = intent.getIntExtra("tag", 0);
		mViewPager.setCurrentItem(tag);
		
	}
     class SamplePagerAdapter extends PagerAdapter {
    	
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
		public View instantiateItem(final ViewGroup container, int position) {
			final PhotoView photoView = new PhotoView(container.getContext());
			photoView.setOnPhotoTapListener(new OnPhotoTapListener() {
				
				@Override
				public void onPhotoTap(View arg0, float arg1, float arg2) {
					// TODO Auto-generated method stub
					finish();
				}
			});
			String url = urlspans.get(position).replaceAll("[^\\w]", "");
			String imageurl = Environment
					.getExternalStorageDirectory()
					+ "/fanfantopic/" + url;
			try {
				if (!fileUtils.isFileExists(url)
						|| fileUtils.getFileSize(url) == 0) {

					downLoader.getBitmap(urlspans.get(position), new onImageLoaderListener() {
						
						@Override
						public void onImageLoader(Bitmap bitmap, String url) {
							// TODO Auto-generated method stub
							photoView.setImageBitmap(bitmap);
						}
					});
				} else {
				
					bm = BitmapFactory.decodeFile(imageurl);
					photoView.setImageBitmap(bm);
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
			
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
