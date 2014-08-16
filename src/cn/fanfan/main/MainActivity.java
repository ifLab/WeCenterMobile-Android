package cn.fanfan.main;

import com.loopj.android.http.PersistentCookieStore;

import cn.fanfan.common.FanfanSharedPreferences;
import cn.fanfan.common.GlobalVariables;
import cn.fanfan.common.ImageFileUtils;
import cn.fanfan.draft.Draft;
import cn.fanfan.found.FoundFrg;
import cn.fanfan.question.Question;
import cn.fanfan.topic.Fragment_topic;
import cn.fanfan.topic.imageload.FileUtils;
import android.app.Activity;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks {

	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;
	private String[] draweritems;
	private int touchTimes = 0;
	private FanfanSharedPreferences sharedPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		sharedPreferences = new FanfanSharedPreferences(MainActivity.this);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
		if (!sharedPreferences.getLogInStatus(false)) {
			draweritems = this.getResources().getStringArray(
					R.array.nologindrawerliststring);
		} else {
			draweritems = this.getResources().getStringArray(
					R.array.drawerliststring);
			// Login();
		}
		mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getSupportFragmentManager();
		if (position == 0) {
			if (!GlobalVariables.IsLogin) {
				Fragment fragment = new Fragment_topic();
				Bundle bundle = new Bundle();
				bundle.putInt("isFocus", GlobalVariables.HOT_TOPIC);
				bundle.putInt("position", position + 1);
				fragment.setArguments(bundle);
				fragmentManager.beginTransaction()
						.replace(R.id.container, fragment).commit();
			} else {
			/*	sharedPreferences = new FanfanSharedPreferences(
						MainActivity.this);
				//Fragment fragment = new HomePageFragment();
				Bundle bundle = new Bundle();
				bundle.putString("uid", sharedPreferences.getUid(""));
				bundle.putInt("position", position + 1);
				//fragment.setArguments(bundle);
				fragmentManager.beginTransaction()
						.replace(R.id.container, fragment).commit();*/
			}
		} else if (position == 1) {
			fragmentManager.beginTransaction()
					.replace(R.id.container, (new FoundFrg())).commit();
			mTitle = draweritems[position];
		} else if (position == 2) {
			Fragment fragment = new Fragment_topic();
			Bundle bundle = new Bundle();
			bundle.putInt("isFocus", GlobalVariables.FOCUS_TOPIC);
			bundle.putInt("position", position + 1);
			fragment.setArguments(bundle);
			fragmentManager.beginTransaction()
					.replace(R.id.container, fragment).commit();
			mTitle = draweritems[position];
		} else if (position == 4) {
			fragmentManager.beginTransaction()
					.replace(R.id.container, (new Draft())).commit();
			mTitle = draweritems[position];
		} else if (position == 3) {
			Intent intent = new Intent(MainActivity.this, Question.class);
			startActivity(intent);
		} else {
			fragmentManager
					.beginTransaction()
					.replace(R.id.container,
							PlaceholderFragment.newInstance(position + 1))
					.commit();
		}
	}

	public void onSectionAttached(int number) {
		mTitle = draweritems[number - 1];
	}

	public void restoreActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			if (sharedPreferences.getLogInStatus(false)) {
				getMenuInflater().inflate(R.menu.main, menu);
			}
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.logout) {
			sharedPreferences.clear();
			PersistentCookieStore cookieStore = new PersistentCookieStore(
					MainActivity.this);
			cookieStore.clear();
			Intent intent = new Intent(this, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			TextView textView = (TextView) rootView
					.findViewById(R.id.section_label);
			textView.setText(Integer.toString(getArguments().getInt(
					ARG_SECTION_NUMBER)));
			return rootView;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((MainActivity) activity).onSectionAttached(getArguments().getInt(
					ARG_SECTION_NUMBER));
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (touchTimes == 0) {
				Toast.makeText(MainActivity.this, "再按一次退出", Toast.LENGTH_SHORT)
						.show();
				touchTimes++;
				return false;
			}
			FileUtils fileUtils = new FileUtils(MainActivity.this);
			ImageFileUtils imageFileUtils = new ImageFileUtils(MainActivity.this);
			fileUtils.deleteFile();
			imageFileUtils.deleteFile();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		if (GlobalVariables.ISFANFANLOGIN == true) {
			GlobalVariables.ISFANFANLOGIN = false;
			finish();
		} else {
		}
		super.onStop();
	}
}
