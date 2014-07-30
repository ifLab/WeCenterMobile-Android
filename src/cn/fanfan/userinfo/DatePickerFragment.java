package cn.fanfan.userinfo;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

public class DatePickerFragment extends DialogFragment {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current date as the default date in the picker
		int year = 1990;
		int month = 5;
		int day = 15;
		// Create a new instance of DatePickerDialog and return it
		return new DatePickerDialog(getActivity(),
				(UserInfoEditActivity) getActivity(), year, month, day);
	}

}