package cn.fanfan.common;

import android.app.ProgressDialog;
import android.content.Context;

public class MyProgressDialog extends ProgressDialog {

	public MyProgressDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public MyProgressDialog(Context context, String title, String message,
			boolean touchCancle) {
		super(context);
		// TODO Auto-generated constructor stub
		setTitle(title);
		setMessage(message);
		setCanceledOnTouchOutside(touchCancle);
	}

	public void hideAndCancle() {
		hide();
		cancel();
	}
}
