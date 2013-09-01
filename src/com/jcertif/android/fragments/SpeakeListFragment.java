package com.jcertif.android.fragments;

import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jcertif.android.JcertifApplication;
import com.jcertif.android.MainActivity;
import com.jcertif.android.R;
import com.jcertif.android.adapters.SpeakerAdapter;
import com.jcertif.android.adapters.SpeedScrollListener;
import com.jcertif.android.dao.SpeakerProvider;
import com.jcertif.android.fragments.SessionListFragment.OnSessionUpdatedListener;
import com.jcertif.android.model.Session;
import com.jcertif.android.model.Speaker;
import com.jcertif.android.service.RESTService;

public class SpeakeListFragment extends RESTResponderFragment {

	private static final String SPEAKER_LIST_URI = JcertifApplication.BASE_URL
			+ "/speaker/list";

	private static String TAG = SessionListFragment.class.getName();

	private List<Speaker> mSpeakers;
	private ListView mLvSpeakers;
	private SpeakerAdapter mAdapter;
	private SpeakerProvider mProvider;
	private SpeedScrollListener mListener;

	public SpeakeListFragment() {
		// Empty constructor required for fragment subclasses
		setRetainInstance(true);
	}

	public interface OnSpeakerUpdatedListener {
		void onSpeakerUpdated(Speaker s);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_list_speaker,
				container, false);
		String speaker = getResources().getStringArray(R.array.menu_array)[1];
		mLvSpeakers = (ListView) rootView.findViewById(R.id.lv_speaker);

		mLvSpeakers.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos,
					long position) {
				mAdapter.setSelectedIndex(pos);
				Speaker speaker = ((Speaker) parent
						.getItemAtPosition((int) position));
				selectSpeaker(speaker);
			}

		});
		getActivity().setTitle(speaker);
		return rootView;
	}

	private void selectSpeaker(Speaker speaker) {
		if(onTablet()){
		((OnSpeakerUpdatedListener) getParentFragment())
				.onSpeakerUpdated(speaker);
		}else{
			Intent intent = new Intent(this.getActivity().getApplicationContext(), 
					SpeakerDetailFragmentActivity.class);
		
		    String speakerJson=	new Gson().toJson(speaker);
			intent.putExtra("speaker",speakerJson);
			
			startActivity(intent);
			getSherlockActivity().overridePendingTransition ( 0 , R.anim.slide_up_left);
		}
	}

	private boolean onTablet() {
	return	((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE);
	}
	
	
	public SpeakerProvider getProvider() {
		if (mProvider == null)
			mProvider = new SpeakerProvider(this.getSherlockActivity());
		return mProvider;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		getSherlockActivity().getSupportActionBar().setNavigationMode(
				ActionBar.NAVIGATION_MODE_STANDARD);
		mSpeakers = loadSpeakersFromCache();
		setSpeakers();
	}
	
	

	private void setSpeakers() {
		MainActivity activity = (MainActivity) getActivity();
		setLoading(true);
		if (mSpeakers.isEmpty() && activity != null) {

			Intent intent = new Intent(activity, RESTService.class);
			intent.setData(Uri.parse(SPEAKER_LIST_URI));

			Bundle params = new Bundle();
			params.putString(RESTService.KEY_JSON_PLAYLOAD, null);

			intent.putExtra(RESTService.EXTRA_PARAMS, params);
			intent.putExtra(RESTService.EXTRA_RESULT_RECEIVER,
					getResultReceiver());

			activity.startService(intent);
		} else if (activity != null) {
			updateList();
		}
	}

	void updateList() {

		mListener = new SpeedScrollListener();
		mLvSpeakers.setOnScrollListener(mListener);
		mAdapter = new SpeakerAdapter(this.getActivity(), mListener, mSpeakers);
		mLvSpeakers.setAdapter(mAdapter);
		setLoading(false);
	}

	@Override
	public void onRESTResult(int code, Bundle resultData) {
		String result=null;
		if(resultData!=null){
			result=resultData.getString(RESTService.REST_RESULT);
		}else{
			Toast.makeText(this.getActivity(), "Failled to load data, check your connection", Toast.LENGTH_LONG).show();
			return;
		
		}
		if (code == 200 && result != null) {
			mSpeakers = parseSpeakerJson(result);
			Log.d(TAG, result);
			setSpeakers();
			saveToCache(mSpeakers);

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

	private void saveToCache(final List<Speaker> result) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				if (result != null)
					for (Speaker sp : result)
						mProvider.store(sp);
			}
		}).start();
	}

	private List<Speaker> loadSpeakersFromCache() {
		List<Speaker> list = getProvider().getAll(Speaker.class);
		return list;
	}

	@Override
	public void onPause() {
		super.onPause();

	}

	@Override
	public void onResume() {
		super.onResume();

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		/*
		 * if (mProvider != null) { mProvider.close(); mProvider = null; }
		 */
	}

	private List<Speaker> parseSpeakerJson(String result) {
		Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy hh:mm")
				.create();
		Speaker[] speakers = gson.fromJson(result, Speaker[].class);

		return Arrays.asList(speakers);

	}
}
