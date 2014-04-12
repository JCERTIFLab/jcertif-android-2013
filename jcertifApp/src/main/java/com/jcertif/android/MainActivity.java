package com.jcertif.android;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jcertif.android.broadcast.UpdaterBroadcast;
import com.jcertif.android.dao.CategorieProvider;
import com.jcertif.android.dao.UserProvider;
import com.jcertif.android.fragments.AboutFragmentActivity;
import com.jcertif.android.fragments.InitialisationFragment;
import com.jcertif.android.fragments.InitialisationFragment.RefentielDataLodedListener;
import com.jcertif.android.fragments.LoginFragment;
import com.jcertif.android.fragments.MapEventFragment;
import com.jcertif.android.fragments.ProfileFragment;
import com.jcertif.android.fragments.SessionParentFragment;
import com.jcertif.android.fragments.SpeakerParentFragment;
import com.jcertif.android.model.Participant;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;

import uk.co.senab.actionbarpulltorefresh.extras.actionbarsherlock.PullToRefreshAttacher;



/**
 * Partially based on the ASOP source/Drawer Layout
 * 
 * @author Patrick Bashizi (bashizip@gmail.com)
 * @author Komi Serge Innocent <komi.innocent@gmail.com>
 */
public class MainActivity extends SherlockFragmentActivity implements LoginFragment.OnSignedInListener,
		RefentielDataLodedListener, PullToRefreshAttacher.OnRefreshListener {

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private String[] mMenuTitles;
	private UserProvider userProvider;
	public static Participant user;

	private PullToRefreshAttacher mPullToRefreshAttacher;

	// preference twitter pour le stockage des cles du user
	private static SharedPreferences mSharedPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		// getWindow().requestFeature((int)
		// com.actionbarsherlock.view.Window.FEATURE_ACTION_BAR_OVERLAY);
		setContentView(R.layout.activity_main);

		// enable ActionBar app icon to behave as action to toggle nav drawer
		getSupportActionBar().setDisplayUseLogoEnabled(true);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

        mPullToRefreshAttacher = PullToRefreshAttacher.get(this);

		mTitle = mDrawerTitle = getTitle();
		mMenuTitles = getResources().getStringArray(R.array.menu_array);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		// set a custom shadow that overlays the main content when the drawer
		// opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		// set up the drawer's list view with items and click listener
		mDrawerList.setAdapter(new DrawerListAdapter(getLoggedInUser()));
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.ic_navigation_drawer, /*
										 * nav drawer image to replace 'Up'
										 * caret
										 */
		R.string.drawer_open, /* "open drawer" description for accessibility */
		R.string.drawer_close /* "close drawer" description for accessibility */
		) {
			public void onDrawerClosed(View view) {
				getSupportActionBar().setTitle(mTitle);
				supportInvalidateOptionsMenu(); // creates call to
												// onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				getSupportActionBar().setTitle(mDrawerTitle);

			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		mSharedPreferences = getApplicationContext().getSharedPreferences(Config.PREFERENCE_JCERTIF_TWITTER, 0);

		if (savedInstanceState == null) {
			init();
		}
		if (firstLaunch()) {
			selectItem(8);
		}

		// setupUpdateService();

	}

	public PullToRefreshAttacher getmPullToRefreshAttacher() {
		return mPullToRefreshAttacher;
	}

	@Override
	public void onBackPressed() {

		if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
			mDrawerLayout.closeDrawer(Gravity.LEFT);
		} else {
			super.onBackPressed();
		}

	}

	void init() {
		selectItem(0);
		mDrawerLayout.setVisibility(View.VISIBLE);
		mDrawerLayout.openDrawer(Gravity.LEFT);
	}

	private boolean firstLaunch() {
		return new CategorieProvider(this).getLabels().length == 0;
	}

	private Participant getLoggedInUser() {
		if (user == null) {
			userProvider = new UserProvider(this);
			List<Participant> list = userProvider.getAll(Participant.class);
			if (list != null && !list.isEmpty()) {
				user = list.get(0);
			}
			userProvider.close();
		}
		return user;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
				mDrawerLayout.closeDrawer(mDrawerList);
			} else {
				mDrawerLayout.openDrawer(mDrawerList);
			}

		}
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// getSupportMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/* The click listner for ListView in the navigation drawer */
	private class DrawerItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			view.setSelected(true);
			selectItem(position);
		}
	}

	private void selectItem(int position) {
		// update the main content by replacing fragments
		Fragment fragment = null;

		switch (position) {
		case 0:
			if (getLoggedInUser() == null) {
				fragment = new LoginFragment();
				// Apres authentification via le navigateur par defaut, deuxieme
				// phase de
				// la connexion vers twitter
				Bundle bundle = new Bundle();
				if (!mSharedPreferences.getBoolean(Config.PREF_KEY_TWITTER_LOGIN, false)) {
					Uri uri = getIntent().getData();
					if (uri != null && uri.toString().startsWith(Config.TWITTER_CALLBACK_URL)) {
						// oAuth verifier
						final String verifier = uri.getQueryParameter(Config.URL_TWITTER_OAUTH_VERIFIER);
						bundle.putString("params", verifier);
					}
				}

				fragment.setArguments(bundle);
			} else {
				fragment = new ProfileFragment();
			}
			break;
		case 1:
			fragment = new SessionParentFragment();
			break;
		case 2:
			fragment = new SpeakerParentFragment();
			break;
		case 3:
			fragment = new MapEventFragment();
			break;

		case 4:
			// fragment = new AboutFragment();
			startActivity(new Intent(MainActivity.this, AboutFragmentActivity.class));
			break;
		case 8:
			fragment = new InitialisationFragment();
			((InitialisationFragment) fragment).setListener(this);
			// mDrawerLayout.setVisibility(View.INVISIBLE);
			break;
		default:
			break;
		}

		FragmentManager fragmentManager = getSupportFragmentManager();
		if (fragment == null) {
			return;
		}

		fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

		mDrawerList.setItemChecked(position, true);
		mDrawerLayout.closeDrawer(mDrawerList);

	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getSupportActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

    class DrawerListAdapter extends BaseAdapter {

        private static final int TYPE_PROFILE = 0;
        private static final int TYPE_ITEM = 1;
        private LayoutInflater mInflater;

        private Participant user;

        public int getCount() {
            return mMenuTitles.length + 1;
        }

        public DrawerListAdapter(Participant user) {
            mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.user = user;
        }

        @Override
        public Object getItem(int position) {
            if (position == 0) {
                return 0;
            } else
                return mMenuTitles[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            int type = getItemViewType(position);
            if (convertView == null) {
                holder = new ViewHolder();
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            switch (type) {
                case TYPE_ITEM:
                    convertView = convertView == null?mInflater.inflate(R.layout.drawer_list_item, null):convertView;
                    holder.textView = (TextView) convertView.findViewById(R.id.tv_drawer_item);

                    holder.imgView = (ImageView) convertView.findViewById(R.id.img_drawer);
                    holder=setTypeItemHolderValues(holder,position);

                    break;
                case TYPE_PROFILE:
                    convertView = convertView == null?mInflater.inflate(R.layout.item_profile, null):convertView;
                    holder.textView = (TextView) convertView.findViewById(R.id.tv_username);
                    holder.imgView = (ImageView) convertView.findViewById(R.id.img_profile);
                    holder=setTypeProfilHolderValues(holder,user);
                    break;
            }
            convertView.setTag(holder);

            return convertView;
        }

        @Override
        public int getItemViewType(int position) {
            return (position == 0) ? TYPE_PROFILE : TYPE_ITEM;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        public class ViewHolder {
            public TextView textView;
            public ImageView imgView;
        }

        /**
         *
         * @param holder
         * @param itemPosition
         * @return
         */
        private ViewHolder setTypeItemHolderValues(ViewHolder holder,int itemPosition){
            switch (itemPosition) {
                case 1:
                    holder.imgView.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_list));
                    break;
                case 2:
                    holder.imgView.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_user));
                    break;

                case 3:
                    holder.imgView.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_map));
                    break;

                case 4:
                    holder.imgView.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_help));
                    break;
                default:
                    break;
            }
            holder.textView.setText(mMenuTitles[itemPosition - 1]);
            return holder;
        }

        /**
         *
         * @param holder
         * @param user
         * @return
         */
        private ViewHolder setTypeProfilHolderValues(ViewHolder holder,Participant user){

            if (user == null) {
                holder.textView.setText("Please Login");
            } else {
                holder.textView.setText(user.getFirstname() + " " + user.getLastname());
                Picasso.with(MainActivity.this).load(user.getPhoto()).placeholder(R.drawable.ic_action_profile)
                        .into(holder.imgView);
            }

            return holder;
        }

    }


    @Override
	public void onSignedIn(Participant user, ProgressDialog dlg) {

		mDrawerList.setAdapter(new DrawerListAdapter(user));
		setLogedInUser(user);
		selectItem(0);
		mDrawerLayout.openDrawer(Gravity.LEFT);

		dlg.dismiss();
	}

	private void setLogedInUser(Participant user) {
		MainActivity.user = user;
		userProvider = new UserProvider(this);
		userProvider.store(user);
		userProvider.close();
	}

	@Override
	public void OnRefDataLoaded() {
		init();
	}

	void setupUpdateService() {
		Calendar calendar = Calendar.getInstance();
		// 9 AM
		calendar.set(Calendar.HOUR_OF_DAY, 16);
		calendar.set(Calendar.MINUTE, 42);
		calendar.set(Calendar.SECOND, 0);
		Intent intent = new Intent(this, UpdaterBroadcast.class);

		PendingIntent pi = PendingIntent.getBroadcast(this, 0, intent, 0);

		AlarmManager am = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
		am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pi);
	}

	@Override
	public void onRefreshStarted(View view) {

	}
}
