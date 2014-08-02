package cn.fanfan.main;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cn.fanfan.common.AsyncImageGet;
import cn.fanfan.common.Config;
import cn.fanfan.common.FanfanSharedPreferences;
import cn.fanfan.common.GlobalVariables;
import cn.fanfan.userinfo.UserInfoActivity;
import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

//pref_user_learned_drawer
/**
 * Fragment used for managing interactions for and presentation of a navigation
 * drawer. See the <a href=
 * "https://developer.android.com/design/patterns/navigation-drawer.html#Interaction"
 * > design guidelines</a> for a complete explanation of the behaviors
 * implemented here.
 */
public class NavigationDrawerFragment extends Fragment {

	/**
	 * Remember the position of the selected item.
	 */
	private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

	/**
	 * Per the design guidelines, you should show the drawer on launch until the
	 * user manually expands it. This shared preference tracks this.
	 */
	private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

	/**
	 * A pointer to the current callbacks instance (the Activity).
	 */
	private NavigationDrawerCallbacks mCallbacks;

	/**
	 * Helper component that ties the action bar to the navigation drawer.
	 */
	private ActionBarDrawerToggle mDrawerToggle;

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerListView;
	private View mFragmentContainerView;

	private int mCurrentSelectedPosition = 0;
	private boolean mFromSavedInstanceState;
	private boolean mUserLearnedDrawer;
	private LinearLayout login_header;
	private RelativeLayout user_header;
	
	private String uid;
	
	private int prePosition = 0;
	private int currentPosition = 0;
	private String userNameString;

	private ImageView login_icon;
	public NavigationDrawerFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//System.out.println(GlobalVariables.uSER_IMAGE_URL);
		if (GlobalVariables.uSER_IMAGE_URL == null) {
			Login();
		}
		// Read in the flag indicating whether or not the user has demonstrated
		// awareness of the
		// drawer. See PREF_USER_LEARNED_DRAWER for details.
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

		if (savedInstanceState != null) {
			mCurrentSelectedPosition = savedInstanceState
					.getInt(STATE_SELECTED_POSITION);
			mFromSavedInstanceState = true;
		}

		// Select either the default item (0) or the last selected item.
		selectItem(mCurrentSelectedPosition);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// Indicate that this fragment would like to influence the set of
		// actions in the action bar.
		setHasOptionsMenu(true);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (GlobalVariables.uSER_IMAGE_URL != null) {
			(new AsyncImageGet(Config.getValue("userImageBaseUrl")+GlobalVariables.uSER_IMAGE_URL, login_icon)).execute();
		}
	}
	private void Login(){
		FanfanSharedPreferences sharedPreferences = new FanfanSharedPreferences(getActivity());
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams requestParams = new RequestParams();
		requestParams.put("user_name", sharedPreferences.getUserName(""));
		requestParams.put("password", sharedPreferences.getPasswd(""));
		String url = Config.getValue("LoginUrl");
		client.post(url,requestParams,new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				try {
					JSONObject jsonObject = new JSONObject(new String(arg2));
					int errno = jsonObject.getInt("errno");
					if (errno == -1) {
						String err = jsonObject.getString("err");
						if (err.equals("请输入正确的账号或密码")) {
					
							Toast.makeText(getActivity(), "您的账号或密码已经修改，请重新登录", Toast.LENGTH_SHORT).show();
						}
					}else {
						String rsm = jsonObject.getString("rsm");
						JSONObject jsonObject2 = new JSONObject(rsm);
						String avatar_file = jsonObject2.getString("avatar_file");
						GlobalVariables.uSER_IMAGE_URL = avatar_file;
						(new AsyncImageGet(Config.getValue("userImageBaseUrl")+GlobalVariables.uSER_IMAGE_URL, login_icon)).execute();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					
					e.printStackTrace();
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		FanfanSharedPreferences sharedPreferences = new FanfanSharedPreferences(getActivity());
		uid = sharedPreferences.getUid("1");
		userNameString = sharedPreferences.getUserName("");
		LinearLayout linearLayout = (LinearLayout) inflater.inflate(
				R.layout.fragment_navigation_drawer, null);
		login_header = (LinearLayout) linearLayout
				.findViewById(R.id.drawer_header_log_in);
		user_header = (RelativeLayout) linearLayout
				.findViewById(R.id.drawer_header_user);
		mDrawerListView = (ListView) linearLayout.findViewById(R.id.draw_list);
		login_icon = (ImageView)user_header.findViewById(R.id.login_icon);
		TextView userName = (TextView)user_header.findViewById(R.id.name);
		userName.setText(userNameString);
		user_header.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),UserInfoActivity.class);
				intent.putExtra("uid", uid);
				GlobalVariables.ISFANFANLOGIN = true;
				startActivity(intent);
			}
		});
		login_header.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),cn.fanfan.welcome.Login.class);
				startActivity(intent);
			}
		});
		mDrawerListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						prePosition = currentPosition;
						currentPosition = position;
						selectItem(position);
					}
				});
		Resources resources = getActivity().getResources();
		if (!sharedPreferences.getLogInStatus(false)) {
			String[] strings = resources
					.getStringArray(R.array.nologindrawerliststring);
			ArrayList<Drawable> drawables = new ArrayList<Drawable>();
			drawables.add(resources
					.getDrawable(R.drawable.ic_drawer_follow_normal));
			DrawerAdapter adapter = new DrawerAdapter(getActivity(), strings,
					drawables);
			user_header.setVisibility(View.GONE);
			login_header.setVisibility(View.VISIBLE);
			mDrawerListView.setAdapter(adapter);
		} else {
			String[] strings = resources
					.getStringArray(R.array.drawerliststring);
			ArrayList<Drawable> drawables = new ArrayList<Drawable>();
			drawables.add(resources
					.getDrawable(R.drawable.ic_drawer_home_normal));
			drawables.add(resources
					.getDrawable(R.drawable.ic_drawer_explore_normal));
			drawables.add(resources
					.getDrawable(R.drawable.ic_drawer_follow_normal));
			drawables.add(resources.getDrawable(R.drawable.ic_collected));
			drawables.add(resources
					.getDrawable(R.drawable.ic_drawer_draft_normal));
			drawables.add(resources
					.getDrawable(R.drawable.ic_drawer_question_normal));
			DrawerAdapter adapter = new DrawerAdapter(getActivity(), strings,
					drawables);
			user_header.setVisibility(View.VISIBLE);
			login_header.setVisibility(View.GONE);
			mDrawerListView.setAdapter(adapter);
		}
		/*
		 * mDrawerListView.setAdapter(new ArrayAdapter<String>(
		 * getActionBar().getThemedContext(),
		 * android.R.layout.simple_expandable_list_item_1, android.R.id.text1,
		 * new String[]{ getString(R.string.title_section1),
		 * getString(R.string.title_section2),
		 * getString(R.string.title_section3), }));
		 */
		mDrawerListView.setItemChecked(mCurrentSelectedPosition, true);
		return linearLayout;
	}

	public boolean isDrawerOpen() {
		return mDrawerLayout != null
				&& mDrawerLayout.isDrawerOpen(mFragmentContainerView);
	}

	/**
	 * Users of this fragment must call this method to set up the navigation
	 * drawer interactions.
	 * 
	 * @param fragmentId
	 *            The android:id of this fragment in its activity's layout.
	 * @param drawerLayout
	 *            The DrawerLayout containing this fragment's UI.
	 */
	public void setUp(int fragmentId, DrawerLayout drawerLayout) {
		mFragmentContainerView = getActivity().findViewById(fragmentId);
		mDrawerLayout = drawerLayout;

		// set a custom shadow that overlays the main content when the drawer
		// opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		// set up the drawer's list view with items and click listener

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the navigation drawer and the action bar app icon.
		mDrawerToggle = new ActionBarDrawerToggle(getActivity(), /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
		R.string.navigation_drawer_open, /*
										 * "open drawer" description for
										 * accessibility
										 */
		R.string.navigation_drawer_close /*
										 * "close drawer" description for
										 * accessibility
										 */
		) {
			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
				RelativeLayout relativeLayout = (RelativeLayout) mDrawerListView
						.getChildAt(mCurrentSelectedPosition);
				int colorId = getActivity().getResources().getColor(
						R.color.drawer_item_checked);
				// relativeLayout.setBackgroundColor(colorId);
				if (!isAdded()) {
					return;
				}
				getActivity().invalidateOptionsMenu(); // calls
														// onPrepareOptionsMenu()
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				RelativeLayout relativeLayout = (RelativeLayout) mDrawerListView
						.getChildAt(mCurrentSelectedPosition);
				int colorId = getActivity().getResources().getColor(
						R.color.drawer_item_checked);
				// relativeLayout.setBackgroundColor(colorId);
				if (!isAdded()) {
					return;
				}
				if (!mUserLearnedDrawer) {
					// The user manually opened the drawer; store this flag to
					// prevent auto-showing
					// the navigation drawer automatically in the future.
					mUserLearnedDrawer = true;
					SharedPreferences sp = PreferenceManager
							.getDefaultSharedPreferences(getActivity());
					sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true)
							.apply();
				}
				getActivity().invalidateOptionsMenu(); // calls
														// onPrepareOptionsMenu()
			}
		};

		// If the user hasn't 'learned' about the drawer, open it to introduce
		// them to the drawer,
		// per the navigation drawer design guidelines.
		if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
			mDrawerLayout.openDrawer(mFragmentContainerView);
		}

		// Defer code dependent on restoration of previous instance state.
		mDrawerLayout.post(new Runnable() {
			@Override
			public void run() {
				mDrawerToggle.syncState();
			}
		});
		mDrawerLayout.setDrawerListener(mDrawerToggle);
	}

	private void selectItem(int position) {
		mCurrentSelectedPosition = position;
		if (mDrawerListView != null) {
			if (position == 5) {
				mDrawerListView.setItemChecked(position, false);
				mDrawerListView.setItemChecked(prePosition, true);
			}else {
				mDrawerListView.setItemChecked(position, true);
			}
		}
		if (mDrawerLayout != null) {
			mDrawerLayout.closeDrawer(mFragmentContainerView);
		}
		if (mCallbacks != null) {
			mCallbacks.onNavigationDrawerItemSelected(position);
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mCallbacks = (NavigationDrawerCallbacks) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(
					"Activity must implement NavigationDrawerCallbacks.");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mCallbacks = null;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Forward the new configuration the drawer toggle component.
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// If the drawer is open, show the global app actions in the action bar.
		// See also
		// showGlobalContextActionBar, which controls the top-left area of the
		// action bar.
		if (mDrawerLayout != null && isDrawerOpen()) {
			inflater.inflate(R.menu.global, menu);
			showGlobalContextActionBar();
		}
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		/*if (item.getItemId() == R.id.action_example) {
			Toast.makeText(getActivity(), "Example action.", Toast.LENGTH_SHORT)
					.show();
			return true;
		}
*/
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Per the navigation drawer design guidelines, updates the action bar to
	 * show the global app 'context', rather than just what's in the current
	 * screen.
	 */
	private void showGlobalContextActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setTitle(R.string.app_name);
	}

	private ActionBar getActionBar() {
		return getActivity().getActionBar();
	}

	/**
	 * Callbacks interface that all activities using this fragment must
	 * implement.
	 */
	public static interface NavigationDrawerCallbacks {
		/**
		 * Called when an item in the navigation drawer is selected.
		 */
		void onNavigationDrawerItemSelected(int position);
	}
	
}
