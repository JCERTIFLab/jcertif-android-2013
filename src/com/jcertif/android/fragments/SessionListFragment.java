package com.jcertif.android.fragments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshAttacher;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jcertif.android.JcertifApplication;
import com.jcertif.android.MainActivity;
import com.jcertif.android.R;
import com.jcertif.android.adapters.SessionAdapter;
import com.jcertif.android.adapters.SpeedScrollListener;
import com.jcertif.android.dao.SessionProvider;
import com.jcertif.android.dao.SpeakerProvider;
import com.jcertif.android.model.Session;
import com.jcertif.android.model.Speaker;
import com.jcertif.android.service.RESTService;

/**
 * 
 * @author Patrick Bashizi
 * 
 */
public class SessionListFragment extends RESTResponderFragment implements PullToRefreshAttacher.OnRefreshListener{

	public static final String SESSIONS_LIST_URI = JcertifApplication.BASE_URL
			+ "/session/list";
	public static final String CATEGORY_LIST_URI = JcertifApplication.BASE_URL
			+ "/ref/category/list";

	private static String TAG = SessionListFragment.class.getName();

	private List<Session> mSessions = new ArrayList<Session>();;
	private ListView mLvSessions;
	private SessionAdapter mAdapter;
	private SessionProvider mProvider;
	private SpeedScrollListener mListener;
	private ActionMode mActionMode;
	private Session mSelectedSession;

	public SessionListFragment() {
		// Empty constructor required for fragment subclasses
	}

	public interface OnSessionUpdatedListener {
		void onSessionUpdated(Session session);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// setRetainInstance(true);
		View rootView = inflater.inflate(R.layout.fragment_session, container,
				false);
		mLvSessions = (ListView) rootView.findViewById(R.id.lv_session);
		String session = getResources().getStringArray(R.array.menu_array)[0];
		setHasOptionsMenu(true);
		getActivity().setTitle(session);

		mLvSessions = (ListView) rootView.findViewById(R.id.lv_session);
		
		mPullToRefreshAttacher=((MainActivity)getSherlockActivity()).getmPullToRefreshAttacher();		
	    mPullToRefreshAttacher.addRefreshableView(mLvSessions, this);
	       
		mLvSessions.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos,
					long position) {
				mAdapter.setSelectedIndex(pos);
				mSelectedSession = ((Session) parent
						.getItemAtPosition((int) position));
				updateSession(mSelectedSession);
			}

		});

		mLvSessions
				.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> arg0,
							View arg1, int pos, long arg3) {
						if (mActionMode != null) {
							return false;
						}

						mActionMode = getSherlockActivity().startActionMode(
								mActionModeCallback);
						mSelectedSession = ((Session) arg0
								.getItemAtPosition((int) pos));
						mAdapter.setSelectedIndex(pos);
						return true;
					}

				});
		return rootView;
	}

	private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {

			MenuInflater inflater = mode.getMenuInflater();
			inflater.inflate(R.menu.context_menu_session, menu);
			return true;
		}

		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			return false;
		}

		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

			switch (item.getItemId()) {
			case R.id.menu_share:
				shareSessionItem();
				mode.finish(); // Action picked, so close the CAB
				break;
			case R.id.menu_add_to_schedule:
				addSessionItemToSchedule();
				mode.finish(); // Action picked, so close the CAB
				break;
			default:
				return false;
			}
			return true;
		}

		public void onDestroyActionMode(ActionMode mode) {
			mActionMode = null;
		}
	};

	private void addSessionItemToSchedule() {
	
		if (android.os.Build.VERSION.SDK_INT >= 14){
		Intent intent = new Intent(Intent.ACTION_INSERT);
		intent.setType("vnd.android.cursor.item/event");
		intent.putExtra(Events.TITLE, mSelectedSession.getTitle());
		intent.putExtra(Events.EVENT_LOCATION,"Room"+ mSelectedSession.getSalle());
		intent.putExtra(Events.DESCRIPTION, mSelectedSession.getDescription());

		
		Date evStartDate= mSelectedSession.getStart();
		Date evEndDate= mSelectedSession.getStart();
	
		// Setting dates
		GregorianCalendar startcalDate = new GregorianCalendar();
		startcalDate.setTime(evStartDate);
		
		// Setting dates
		GregorianCalendar endCalDate = new GregorianCalendar();
	    endCalDate.setTime(evEndDate);
		
		intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,startcalDate.getTimeInMillis());
		intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,endCalDate.getTimeInMillis());
		// Make it a full day event
		intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);
		// Make it a recurring Event
	//	intent.putExtra(Events.RRULE, "WKST=SU");
		// Making it private and shown as busy
		intent.putExtra(Events.ACCESS_LEVEL, Events.ACCESS_PRIVATE);
		intent.putExtra(Events.AVAILABILITY, Events.AVAILABILITY_BUSY); 
		//intent.putExtra(Events.DISPLAY_COLOR, Events.EVENT_COLOR); 
		startActivity(intent);
		}else{
			Toast.makeText(this.getSherlockActivity(), 
					"Not supported for your device :(", Toast.LENGTH_SHORT).show();
		} 
	}

	private void shareSessionItem() {

		Speaker sp = new SpeakerProvider(this.getSherlockActivity())
				.getByEmail(mSelectedSession.getSpeakers()[0]);
		Intent intent = new Intent(android.content.Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

		intent.putExtra(Intent.EXTRA_SUBJECT, "Share Session");
		intent.putExtra(
				Intent.EXTRA_TEXT,
				"Checking out this  #Jcertif2013 session : "
						+ mSelectedSession.getTitle() + " by "
						+ sp.getFirstname() + " " + sp.getLastname());

		startActivity(intent);
	}

	protected void updateSession(Session s) {
		if(onTablet()){
			((OnSessionUpdatedListener) getParentFragment()).onSessionUpdated(s);
		}else{
			Intent intent = new Intent(this.getActivity().getApplicationContext(), 
					SessionDetailFragmentActivity.class);
		
		    String sessionJson=	new Gson().toJson(s);
			intent.putExtra("session",sessionJson);
			
			startActivity(intent);
			getSherlockActivity().overridePendingTransition ( 0 , R.anim.slide_up_left);
		}
	}
	
	
	
		
	public SessionProvider getProvider() {
		if (mProvider == null)
			mProvider = new SessionProvider(this.getSherlockActivity());
		return mProvider;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// This gets called each time our Activity has finished creating itself.
		// First check the local cache, if it's empty data will be fetched from
		// web
		mSessions = loadSessionsFromCache();
		setSessions();

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
	
	}

	/**
	 * We cache our stored session here so that we can return right away on
	 * multiple calls to setSession() during the Activity lifecycle events (such
	 * as when the user rotates their device).
	 */
	private void setSessions() {
		MainActivity activity = (MainActivity) getActivity();
		setLoading(true);
		if (mSessions.isEmpty() && activity != null) {

			// This is where we make our REST call to the service. We also pass
			// in our ResultReceiver
			// defined in the RESTResponderFragment super class.

			// We will explicitly call our Service since we probably want to
			// keep it as a private component in our app.
			Intent intent = new Intent(activity, RESTService.class);
			intent.setData(Uri.parse(SESSIONS_LIST_URI));

			// Here we are going to place our REST call parameters.
			Bundle params = new Bundle();
			params.putString(RESTService.KEY_JSON_PLAYLOAD, null);

			intent.putExtra(RESTService.EXTRA_PARAMS, params);
			intent.putExtra(RESTService.EXTRA_RESULT_RECEIVER,getResultReceiver());

			// Here we send our Intent to our RESTService.
			activity.startService(intent);
		} else if (activity != null) {
			// Here we check to see if our activity is null or not.
			// We only want to update our views if our activity exists.
			// Load our list adapter with our session.

			updateList();
			setLoading(false);
		}
	}

	void updateList() {

		mListener = new SpeedScrollListener();
		mLvSessions.setOnScrollListener(mListener);
		mAdapter = new SessionAdapter(this.getActivity(), mListener, mSessions);
		mLvSessions.setAdapter(mAdapter);
		if(refreshing){
			refreshing=false;
			mPullToRefreshAttacher.setRefreshComplete();
		}
	}

	private boolean onTablet() {
		return ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE);
	}

	public void updateList(String cat) {
		if (cat.equals("All") || cat.equals("Tous")) {
			mSessions = loadSessionsFromCache();
		} else {
			mSessions = getProvider().getSessionsByCategory(cat);
		}
		updateList();

	}

	@Override
	public void onRESTResult(int code, Bundle resultData) {
		// Here is where we handle our REST response.
		// Check to see if we got an HTTP 200 code and have some data.
		String result = null;
		if (resultData != null) {
			result = resultData.getString(RESTService.REST_RESULT);
		} else {
			return;
		}
		if (code == 200 && result != null) {

			mSessions = parseSessionJson(result);
			Log.d(TAG, result);
			setSessions();
			saveToCache(mSessions);

		} else {
			Activity activity = getActivity();
			if (activity != null) {
				Toast.makeText(
						activity,
						"Failed to load Session data. Check your internet settings.",
						Toast.LENGTH_SHORT).show();

			}
		}
		setLoading(false);
	}

	private List<Session> parseSessionJson(String result) {
		Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy hh:mm")
				.create();
		Session[] sessions = gson.fromJson(result, Session[].class);

		return Arrays.asList(sessions);
	}

	protected void saveToCache(final List<Session> sessions) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				for (Session session : sessions)
					mProvider.store(session);
			}
		}).start();
	}

	private List<Session> loadSessionsFromCache() {
		List<Session> list = getProvider().getAll(Session.class);
		return list;
	}

	@Override
	public void onPause() {
		super.onDestroy();

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onRefreshStarted(View view) {
		
		mProvider.deleteAll(Session.class);
		//mLvSessions.setAdapter(null);
		mSessions = loadSessionsFromCache();
		setSessions();
		
	   refreshing=true;
		
		
	}
}
