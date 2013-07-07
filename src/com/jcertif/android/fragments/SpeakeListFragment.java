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
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jcertif.android.JcertifApplication;
import com.jcertif.android.MainActivity;
import com.jcertif.android.R;
import com.jcertif.android.adapters.SessionAdapter;
import com.jcertif.android.adapters.SpeakerAdapter;
import com.jcertif.android.adapters.SpeedScrollListener;
import com.jcertif.android.dao.SessionProvider;
import com.jcertif.android.dao.SpeakerProvider;
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
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_speaker, container,
				false);
		String speaker = getResources().getStringArray(R.array.menu_array)[1];
		mLvSpeakers = (ListView) rootView.findViewById(R.id.lv_speaker);
		getActivity().setTitle(speaker);
		return rootView;
	}

	public SpeakerProvider getProvider() {
		if (mProvider == null)
			mProvider = new SpeakerProvider(this.getSherlockActivity());
		return mProvider;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		mSpeakers = loadSpeakersFromCache();
		setSessions();
	}

	

	private void setSessions() {
		MainActivity activity = (MainActivity) getActivity();

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

			mListener = new SpeedScrollListener();
			mLvSpeakers.setOnScrollListener(mListener);
			mAdapter = new SpeakerAdapter(this.getActivity(), mListener,
					mSpeakers);
			mLvSpeakers.setAdapter(mAdapter);

		}
	}

	@Override
	public void onRESTResult(int code, String result) {
		if (code == 200 && result != null) {
			mSpeakers = parseSessionJson(result);
			Log.d(TAG, result);
			setSessions();
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
				for (Speaker sp : result)
					mProvider.store(sp);
			}
		}).start();
	}
	private List<Speaker> loadSpeakersFromCache() {	
		List<Speaker> list =getProvider().getAll(Speaker.class);		
		return list;
	}


	@Override
	public void onPause() {
		super.onDestroy();
		if(mProvider!=null){
		mProvider.close();
	    mProvider=null;
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();	
	}
	private List<Speaker> parseSessionJson(String result) {
		Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy hh:mm")
				.create();
		Speaker[] speakers = gson.fromJson(result, Speaker[].class);

		return Arrays.asList(speakers);

	}
}
