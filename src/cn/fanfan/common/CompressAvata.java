package cn.fanfan.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;
import android.util.Log;

public class CompressAvata {
	String compressAvatarPath;

	public CompressAvata(String avatarPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(avatarPath, newOpts);
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		float hh = 400f;
		float ww = 240f;
		int be = 8;// be=1表示不缩放
		// // if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
		// // be = (int) (newOpts.outWidth / ww);
		// // } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
		// // be = (int) (newOpts.outHeight / hh);
		// // }
		// if (be <= 0)
		// be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		bitmap = BitmapFactory.decodeFile(avatarPath, newOpts);
		Bitmap Cbitmap = compressByCutQuality(bitmap);// 压缩好比例大小后再进行质量压缩
		String path = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED) ? Environment
				.getExternalStorageDirectory().getPath() + "/fanfan" : null
				+ "/fanfan";
		File foldFile = new File(path);
		if (!foldFile.exists()) {
			foldFile.mkdir();
		}
		
		compressAvatarPath = path + File.separator + "avatarImageMin.jpg";
		File file = new File(path + File.separator + "avatarImageMin.jpg");
		try {
			file.createNewFile();
			FileOutputStream outputStream = new FileOutputStream(file);
			bitmap.compress(CompressFormat.JPEG, 100, outputStream);
			outputStream.flush();
			outputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.i("拍照压缩", "失败");
		}

		// bitmap保存
	}

	public String getCompressAvatarPath() {
		return compressAvatarPath;
	}

	public Boolean isConformSize() {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(compressAvatarPath, newOpts);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		if (baos.toByteArray().length / 1024 > 512) {
			return true;
		} else {
			return false;
		}
	}

	private Bitmap compressByCutQuality(Bitmap image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 90, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 80;
		while (baos.toByteArray().length / 1024 > 512) { // 循环判断如果压缩后图片是否大于512kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;// 每次都减少10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		return bitmap;

	}
}
