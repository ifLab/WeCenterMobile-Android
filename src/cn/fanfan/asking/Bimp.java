package cn.fanfan.asking;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.provider.ContactsContract.Contacts.Photo;

/**
 * 压缩图片
 * 
 * @author Administrator
 * 
 */
public class Bimp {
	public static int max = 0;
	public static boolean act_bool = true;
	public static List<Bitmap> bmp = new ArrayList<Bitmap>();

	// 图片sd地址 上传服务器时把图片调用下面方法压缩后 保存到临时文件夹 图片压缩后小�?100KB，失真度不明�?
	public static List<String> drr = new ArrayList<String>();

	// TelephonyManager tm = (TelephonyManager) this
	// .getSystemService(Context.TELEPHONY_SERVICE);

	public static String revitionImageSize(String path) throws IOException {
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(
				new File(path)));

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		// Bitmap btBitmap=BitmapFactory.decodeFile(path);
		// System.out.println("原尺寸高度："+btBitmap.getHeight());
		// System.out.println("原尺寸宽度："+btBitmap.getWidth());
		BitmapFactory.decodeStream(in, null, options);
		in.close();
		int i = 0;
		Bitmap bitmap = null;
		while (true) {
			if ((options.outWidth >> i <= 800)
					&& (options.outHeight >> i <= 800)) {
				in = new BufferedInputStream(
						new FileInputStream(new File(path)));
				options.inSampleSize = (int) Math.pow(2.0D, i);
				options.inJustDecodeBounds = false;
				bitmap = BitmapFactory.decodeStream(in, null, options);
				break;
			}
			i += 1;
		}
		// 当机型为三星时图片翻�?
//		bitmap = Photo.photoAdapter(path, bitmap);
//		System.out.println("-----压缩后尺寸高度：" + bitmap.getHeight());
//		System.out.println("-----压缩后尺寸宽度度�?" + bitmap.getWidth());
		
		return newpath(bitmap);
	}
    private static String newpath(Bitmap bitmap) {
    CompressFormat format= Bitmap.CompressFormat.JPEG;  
    String pString = Environment.getExternalStorageDirectory()+"/fanfantmp";
    OutputStream outputStream = null;
    int quality = 100;
   	 final File photodir = new File(pString);
   	 if (!photodir.exists()) {  
					photodir.mkdirs();					                        		 
        }
   	 String name = getPhotoFileName();
   	 File photo = new File(pString,name);
   	 try {
		outputStream = new FileOutputStream(photo);
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
   	 bitmap.compress(format, quality, outputStream);
		return pString+"/"+name;
		
	}
	 private static String getPhotoFileName() {  
	        Date date = new Date(System.currentTimeMillis());  
	        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMGTMP'_yyyyMMdd_HHmmss");  
	        return dateFormat.format(date) + ".jpg";  
	    }  
	public static Bitmap getLoacalBitmap(String url) {
		try {
			FileInputStream fis = new FileInputStream(url);
			return BitmapFactory.decodeStream(fis); // /把流转化为Bitmap图片

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 
	 * @param x
	 *            图像的宽�?
	 * @param y
	 *            图像的高�?
	 * @param image
	 *            源图�?
	 * @param outerRadiusRat
	 *            圆角的大�?
	 * @return 圆角图片
	 */
	public static Bitmap createFramedPhoto(int x, int y, Bitmap image, float outerRadiusRat) {
		// 根据源文件新建一个darwable对象
		Drawable imageDrawable = new BitmapDrawable(image);

		// 新建�?个新的输出图�?
		Bitmap output = Bitmap.createBitmap(x, y, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		// 新建�?个矩�?
		RectF outerRect = new RectF(0, 0, x, y);

		// 产生�?个红色的圆角矩形
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(Color.RED);
		canvas.drawRoundRect(outerRect, outerRadiusRat, outerRadiusRat, paint);

		// 将源图片绘制到这个圆角矩形上
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		imageDrawable.setBounds(0, 0, x, y);
		canvas.saveLayer(outerRect, paint, Canvas.ALL_SAVE_FLAG);
		imageDrawable.draw(canvas);
		canvas.restore();

		return output;
	}
}
