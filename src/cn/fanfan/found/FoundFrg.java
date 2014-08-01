package cn.fanfan.found;

import cn.fanfan.main.R;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class FoundFrg extends Fragment {
	 private int offset = 0;// 动画图片偏移量
	 private int currIndex = 0;// 当前页卡编号
	 private int bmpW;// 动画图片宽度
	 private int itemcount = 4;
	 private ViewPager viewPager;
	 private ImageView cursor;// 动画图片
	 private TextView t1, t2, t3,t4;// 页卡头标
	 private FoundPageAdapter foundPageAdapter;
	 public FoundFrg() {
		// TODO Auto-generated constructor stub
	}
	 @Override
     public View onCreateView(LayoutInflater inflater, ViewGroup container,
             Bundle savedInstanceState) {
	
         View rootView = inflater.inflate(R.layout.foundfrg, container, false);
         cursor = (ImageView) rootView.findViewById(R.id.cursor);
         t1 = (TextView) rootView.findViewById(R.id.text1);
	     t2 = (TextView) rootView.findViewById(R.id.text2);
	     t3 = (TextView) rootView.findViewById(R.id.text3);
	     t4 = (TextView) rootView.findViewById(R.id.text4);
	     InitTextView();
	     InitImageView();
	     viewPager = (ViewPager) rootView.findViewById(R.id.foundpager);
	     InitViewPager();
         return rootView;
     }
	 private void InitTextView() {
	     t1.setOnClickListener(new MyOnClickListener(0));
	     t2.setOnClickListener(new MyOnClickListener(1));
	     t3.setOnClickListener(new MyOnClickListener(2));
	     t4.setOnClickListener(new MyOnClickListener(3));
	    }
	 private void InitViewPager() {

	        foundPageAdapter = new FoundPageAdapter(getActivity().getSupportFragmentManager());
	        viewPager.setAdapter(foundPageAdapter);
	        viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
	    }
	 private void InitImageView() {
			bmpW = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.a).getWidth();// 获取图片宽度
			DisplayMetrics dm = new DisplayMetrics();
			getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
			int screenW = dm.widthPixels;// 获取分辨率宽度
			offset = (screenW / itemcount - bmpW) / 2;// 计算偏移量
			Matrix matrix = new Matrix();
			matrix.postTranslate(offset, 0);
			cursor.setImageMatrix(matrix);// 设置动画初始位置
		}

	 public class MyOnClickListener implements View.OnClickListener {
	        private int index = 0;

	        public MyOnClickListener(int i) {
	            index = i;
	        }

	        @Override
	        public void onClick(View v) {
	            viewPager.setCurrentItem(index);
	        }
	    };
	    public class FoundPageAdapter extends FragmentStatePagerAdapter{
	
	        
	        public FoundPageAdapter(FragmentManager fm) {
				// TODO Auto-generated constructor stub
	        	super(fm);
			}
	     
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return 4;
			}
			@Override
			public Fragment getItem(int arg0) {
				// TODO Auto-generated method stub
				Fragment fragment = new FoundPager();
				Bundle args = new Bundle();
				String type,commend;
				switch (arg0) {
				case 0:
					type = "new";
					commend = "0";
					break;
				case 1:
					type = null;
					commend = "1";
					break;
				case 2:
					type = "hot";
					commend = "0";
					break;
				case 3:
					type = "unresponsive";
					commend = "0";
					break;
				default:
					return null;
				}
				args.putString("type", type);
				args.putString("commend", commend);
				fragment.setArguments(args);
				return fragment;
			}
		}
	    public class MyOnPageChangeListener implements OnPageChangeListener {

			int one = offset * 2 + bmpW;

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				Animation animation = null;
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, one * arg0, 0, 0);
				} else {
					animation = new TranslateAnimation(one * currIndex, one * arg0,
							0, 0);
				}
				currIndex = arg0;
				animation.setFillAfter(true);// True:图片停在动画结束位置
				animation.setDuration(300);
				cursor.startAnimation(animation);

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

		}
}
