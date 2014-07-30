package cn.fanfan.draft;

import cn.fanfan.main.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class Draft extends Fragment {
	private ListView listView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.draft, null);
		listView = (ListView) view.findViewById(R.id.draft_lisview);
		DraftAdapter adapter = new DraftAdapter(getActivity());
		listView.setAdapter(adapter);
		return view;
	}
}
