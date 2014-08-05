package cn.fanfan.main;

import java.io.IOException;

import cn.fanfan.common.AsyncImageGet;
import cn.fanfan.common.Config;
import cn.fanfan.common.ImageFileUtils;
import cn.fanfan.common.ImageGet;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class Test extends Activity {
	ImageView imageView;
	private Button button;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.best_answer);
		//button = (Button)findViewById(R.id.login);
			// TODO: handle exception
		//imageView = (ImageView)findViewById(R.id.imageView1);
		//(new AsyncImageGet("http://img0.bdstatic.com/img/image/shouye/zqbbsjbzt.jpg", imageView)).execute();
		//Bitmap bitmap = imageGetetBitmap("/storage/sdcard0/test/logo.png");
		//(new As()).execute();
		/*button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent openAlbumIntent = new Intent(
						Intent.ACTION_GET_CONTENT);
				openAlbumIntent.setType("image/*");
				startActivityForResult(openAlbumIntent, 0);

			}
		});*/
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Uri uri = data.getData();
		String[] pojo = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, pojo, null, null, null);
		String picPath = null;
		if (cursor != null) {
			int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
			cursor.moveToFirst();
			picPath = cursor.getString(columnIndex);
			System.out.println(picPath+"!!");
			cursor.close();
		}
		System.out.println(picPath+"");
		System.out.println(uri+"");

	}
	
	
	class As extends AsyncTask<String, String, Bitmap>{

		@Override
		protected Bitmap doInBackground(String... params) {
			// TODO Auto-generated method stub
			//ImageGet imageGet = new ImageGet();
			//Bitmap bitmap = imageGet.loadPictureFromUrl("http://img0.bdstatic.com/img/image/shouye/zqbbsjbzt.jpg");
			return null;
		}
		
		@Override
		protected void onPostExecute(Bitmap result) {
			// TODO Auto-generated method stub
			ImageFileUtils fileUtils = new ImageFileUtils(Test.this);
			result = fileUtils.getBitmap("http://img0.bdstatic.com/img/image/shouye/zqbbsjbzt.jpg");
			System.out.println(result == null);
			imageView.setImageBitmap(result);
			try {
				fileUtils.saveBitmap("http://img0.bdstatic.com/img/image/shouye/zqbbsjbzt.jpg", result);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			fileUtils.deleteFile("http://img0.bdstatic.com/img/image/shouye/zqbbsjbzt.jpg");
			super.onPostExecute(result);
		}
	}
}
