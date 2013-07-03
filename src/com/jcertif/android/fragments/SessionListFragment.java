package com.jcertif.android.fragments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jcertif.android.JcertifApplication;
import com.jcertif.android.MainActivity;
import com.jcertif.android.R;
import com.jcertif.android.adapters.SessionAdapter;
import com.jcertif.android.dao.SessionProvider;
import com.jcertif.android.model.Session;
import com.jcertif.android.service.RESTService;

/**
 * 
 * @author
 * 
 */
public class SessionListFragment extends RESTResponderFragment {

	private static final String SESSIONS_LIST_URI = JcertifApplication.BASE_URL
			+ "/session/list";

	private static String TAG = SessionListFragment.class.getName();

	private List<Session> mSessions;
	private ListView mLvSessions;
	private SessionAdapter mAdapter;
	private SessionProvider mProvider;
	
	public SessionListFragment() {
		// Empty constructor required for fragment subclasses
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setRetainInstance(true);
		View rootView = inflater.inflate(R.layout.fragment_session, container,
				false);
		mLvSessions = (ListView) rootView.findViewById(R.id.lv_session);
		String session = getResources().getStringArray(R.array.menu_array)[0];
		getActivity().setTitle(session);

		mSessions = new ArrayList<Session>();

		return rootView;
	}
	
	public  SessionProvider getProvider() {
		if (mProvider == null)
			mProvider = new SessionProvider(this.getSherlockActivity());
		return mProvider;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// This gets called each time our Activity has finished creating itself.
		// First check the local cahe, if it's ampty data will be fetched from
		// web
		
		mSessions = loadSessionsFromCache();
		setSessions();
	}

	/**
	 * We cache our stored session here so that we can return right away on
	 * multiple calls to setSession() during the Activity lifecycle events (such
	 * as when the user rotates their device).
	 */
	private void setSessions() {
		MainActivity activity = (MainActivity) getActivity();

		//

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
			intent.putExtra(RESTService.EXTRA_RESULT_RECEIVER,
					getResultReceiver());

			// Here we send our Intent to our RESTService.
			activity.startService(intent);
		} else if (activity != null) {
			// Here we check to see if our activity is null or not.
			// We only want to update our views if our activity exists.
			// Load our list adapter with our session.

			mAdapter = new SessionAdapter(this.getActivity(), mSessions);
			mLvSessions.setAdapter(mAdapter);
			// mAdapter.notifyDataSetChanged();

		}
	}

	@Override
	public void onRESTResult(int code, String result) {
		// Here is where we handle our REST response. 
		// Check to see if we got an HTTP 200 code and have some data.
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
		List<Session> list =getProvider().getAll(Session.class);		
		return list;
	}


	@Override
	public void onPause() {
		super.onDestroy();
		mProvider.close();
	    mProvider=null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();	
	}
}
