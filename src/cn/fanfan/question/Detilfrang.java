package cn.fanfan.question;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.fanfan.main.R;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.Toast;

public class Detilfrang extends Fragment {
	private static Uri photoUri ;
	private File photo;
	private String[] items = new String[] { "选择图片", "拍照" };
	private float dp;
	private GridView gridview;
	private GridAdapter adapter;
	public List<Bitmap> bmp = new ArrayList<Bitmap>();
	public List<String> drr = new ArrayList<String>();
    private EditText editText;
	private HorizontalScrollView selectimg_horizontalScrollView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	setHasOptionsMenu(true);
    	super.onCreate(savedInstanceState);
    }
	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		        View rootView ;
				dp = getActivity().getResources().getDimension(R.dimen.dp);
				rootView = inflater.inflate(R.layout.quesdetil, container, false);
				editText = (EditText)rootView.findViewById(R.id.quesdet);
				selectimg_horizontalScrollView = (HorizontalScrollView)rootView.findViewById(R.id.selectimg_horizontalScrollView);
				gridview = (GridView) rootView.findViewById(R.id.noScrollgridview);
				gridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
				gridviewInit();
	            return rootView;
	}
	public EditText getTextString() {
		return editText;
		
	}
	

	public void gridviewInit() {
		adapter = new GridAdapter(getActivity());
		adapter.setSelectedPosition(0);
		int size = 0;
		if (bmp.size() < 6) {
			size = bmp.size() + 1;
		} else {
			size = bmp.size();
		}
		LayoutParams params = gridview.getLayoutParams();
		final int width = size * (int) (dp * 9.4f);
		params.width = width;
		gridview.setLayoutParams(params);
		gridview.setColumnWidth((int) (dp * 9.4f));
		gridview.setStretchMode(GridView.NO_STRETCH);
		gridview.setNumColumns(size);
		gridview.setAdapter(adapter);
		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				/*((InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE))
				.hideSoftInputFromWindow(getActivity()
						.getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);*/
		if (arg2 == bmp.size()) {
			String sdcardState = Environment.getExternalStorageState();
			if (Environment.MEDIA_MOUNTED.equals(sdcardState)) {
				showDialog();
			} else {
				Toast.makeText(getActivity(), "sdcard不存在",
						Toast.LENGTH_SHORT).show();
			}
		} else {
		}
			}
		});

		selectimg_horizontalScrollView.getViewTreeObserver()
				.addOnPreDrawListener(
						new OnPreDrawListener() {
							public boolean onPreDraw() {
								selectimg_horizontalScrollView.scrollTo(width,
										0);
								selectimg_horizontalScrollView
										.getViewTreeObserver()
										.removeOnPreDrawListener(this);
								return false;
							}
						});
	}
	private void showDialog() {

		new AlertDialog.Builder(getActivity())
				.setTitle("上传图片")
				.setItems(items, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							Intent openAlbumIntent = new Intent(Intent.ACTION_PICK,
									android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
							         getActivity(). startActivityForResult(openAlbumIntent, 0);
							break;
						case 1:
	                        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                        	String pString = Environment.getExternalStorageDirectory()+"/fanfantmp";
                        	 final File photodir = new File(pString);
                        	 if (!photodir.exists()) {  
                  
										photodir.mkdirs();
										                        		 
                             }  
                        	
									// TODO Auto-generated method stub
									photo = new File(photodir,getPhotoFileName());
		                        	 photoUri = Uri.fromFile(photo);
		                        	intent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);
		                        	getActivity().startActivityForResult(intent, 1);
						
							 System.out.println(photoUri.getPath());
							break;
						}
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).show();

	}
	
	 @SuppressLint("SimpleDateFormat") 
	 private String getPhotoFileName() {  
	        Date date = new Date(System.currentTimeMillis());  
	        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");  
	        return dateFormat.format(date) + ".jpg";  
	    }  
		public Uri getUri() {
			return photoUri;
			
		}  
   public void showpic(String picpath){
			Bitmap bitmap = Bimp.getLoacalBitmap(picpath);
            bitmap = Bimp.createFramedPhoto(200, 200, bitmap,
					(int) (dp * 1.6f));
			drr.add(picpath);
			bmp.add(bitmap);
			gridviewInit();
		}
	public class GridAdapter extends BaseAdapter {
		private LayoutInflater listContainer;
		private int selectedPosition = -1;
		private boolean shape;

		public boolean isShape() {
			return shape;
		}

		public void setShape(boolean shape) {
			this.shape = shape;
		}

		public class ViewHolder {
			public ImageView image;
		}

		public GridAdapter(Context context) {
			listContainer = LayoutInflater.from(context);
		}

		public int getCount() {
			if (bmp.size() < 6) {
				return bmp.size() + 1;
			} else {
				return bmp.size();
			}
		}

		public Object getItem(int arg0) {

			return null;
		}

		public long getItemId(int arg0) {

			return 0;
		}

		public void setSelectedPosition(int position) {
			selectedPosition = position;
		}

		public int getSelectedPosition() {
			return selectedPosition;
		}

		/**

		 */
		public View getView(int position, View convertView, ViewGroup parent) {
			final int sign = position;
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();

				convertView = listContainer.inflate(
						R.layout.item_published_grida, null);

				holder.image = (ImageView) convertView
						.findViewById(R.id.item_grida_image);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (position == bmp.size()) {
				holder.image.setImageBitmap(BitmapFactory.decodeResource(
						getResources(), R.drawable.icon_addpic_unfocused));
				if (position == 6) {
					holder.image.setVisibility(View.GONE);
				}
			} else {
				holder.image.setImageBitmap(bmp.get(position));
				holder.image.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						bmp.get(sign).recycle();
						bmp.remove(sign);
						drr.remove(sign);
						gridviewInit();
					}
				});
			}

			return convertView;
		}
	}
}
