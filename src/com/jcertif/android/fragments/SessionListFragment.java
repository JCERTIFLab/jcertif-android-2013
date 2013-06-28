package com.jcertif.android.fragments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
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
import com.jcertif.adroid.service.RESTService;
import com.jcertif.android.JcertifApplication;
import com.jcertif.android.MainActivity;
import com.jcertif.android.R;
import com.jcertif.android.adapters.SessionAdapter;
import com.jcertif.android.model.Session;

/**
 * 
 * @author
 * 
 */
public class SessionListFragment  extends RESTResponderFragment {

	private static final String SESSIONS_LIST_URI = 
			JcertifApplication.BASE_URL+ "/session/list";

	private static String TAG = SessionListFragment.class.getName();

	private List<Session> mSessions;
	private ListView mLvSessions;
	private SessionAdapter mAdapter;;

	public SessionListFragment() {
		// Empty constructor required for fragment subclasses
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setRetainInstance(true);
		View rootView = inflater.inflate(R.layout.fragment_session, container,
				false);
        mLvSessions=(ListView)rootView.findViewById(R.id.lv_session);
		String session = getResources().getStringArray(R.array.menu_array)[0];
		getActivity().setTitle(session);
		
		mSessions= new ArrayList<Session>();
		mAdapter= new SessionAdapter(this.getActivity(), mSessions);
		mLvSessions.setAdapter(mAdapter);			
		
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// This gets called each time our Activity has finished creating itself.
		setSessions();
	}

	/**
	 * We cache our stored session here so that we can return right away on
	 * multiple calls to setSession() during the Activity lifecycle events (such
	 * as when the user rotates their device).
	 */
	private void setSessions() {
		MainActivity activity = (MainActivity) getActivity();

		mSessions = loadSessionsFromCache();
		
		if (mSessions.isEmpty()&& activity != null) {
			
			// This is where we make our REST call to the service. We also pass
			// in our ResultReceiver
			// defined in the RESTResponderFragment super class.

			// We will explicitly call our Service since we probably want to
			// keep it as a private
			// component in our app. You could do this with Intent actions as
			// well, but you have
			// to make sure you define your intent filters correctly in your
			// manifest.
			Intent intent = new Intent(activity, RESTService.class);
			intent.setData(Uri.parse(SESSIONS_LIST_URI));

			// Here we are going to place our REST call parameters. Note that
			// we could have just used Uri.Builder and appendQueryParameter()

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
		    
		     mAdapter.notifyDataSetChanged();
			
		}
	}

	private List<Session> loadSessionsFromCache() {
	
		//call DAO here
		
		return new ArrayList<Session>();
	}

	@Override
	public void onRESTResult(int code, String result) {
		// Here is where we handle our REST response. This is similar to the
		// LoaderCallbacks<D>.onLoadFinished() call from the previous tutorial.

		// Check to see if we got an HTTP 200 code and have some data.
		if (code == 200 && result != null) {

			// For really complicated JSON decoding use Gson
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
		  Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy hh:mm").create();       
	        Session[] sessions=  gson.fromJson(result, Session[].class);
	       	        
		return Arrays.asList(sessions);
	}

	protected void saveToCache(List<Session> sessions) {	
		// must be done async		
	}


	protected ArrayList<Session> readFromCache() {
		// TODO Auto-generated method stub
		return null;
	}



}
