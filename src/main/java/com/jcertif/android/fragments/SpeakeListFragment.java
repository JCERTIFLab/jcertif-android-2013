/*
 * Copyright 2013 JCertifLab.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jcertif.android.fragments;

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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jcertif.android.JcertifApplication;
import com.jcertif.android.MainActivity;
import com.jcertif.android.R;
import com.jcertif.android.adapters.SpeakerAdapter;
import com.jcertif.android.adapters.SpeedScrollListener;
import com.jcertif.android.dao.SpeakerProvider;
import com.jcertif.android.model.Speaker;
import com.jcertif.android.service.RESTService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import uk.co.senab.actionbarpulltorefresh.extras.actionbarsherlock.PullToRefreshAttacher;

public class SpeakeListFragment extends RESTResponderFragment implements PullToRefreshAttacher.OnRefreshListener {

	public static final String SPEAKER_LIST_URI = JcertifApplication.BASE_URL
			+ "/speaker/list";

	private static String TAG = SessionListFragment.class.getName();

	private List<Speaker> mSpeakers;
	private ListView mLvSpeakers;
	private SpeakerAdapter mAdapter;
	private SpeakerProvider mProvider;
	private SpeedScrollListener mListener;
    private PullToRefreshAttacher mPullToRefreshAttacher;
	

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

		
		
		 /**
         * Here we create a PullToRefreshAttacher manually without an Options instance.
         * PullToRefreshAttacher will manually create one using default values.
         */
      
        // Set the Refreshable View to be the ListView and the refresh listener to be this.
		
	   mPullToRefreshAttacher=((MainActivity)getSherlockActivity()).getmPullToRefreshAttacher();				
       mPullToRefreshAttacher.addRefreshableView(mLvSpeakers, this);
		
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
		//mLvSpeakers.setAdapter(null);
		
		mListener = new SpeedScrollListener();
		mLvSpeakers.setOnScrollListener(mListener);
		mAdapter = new SpeakerAdapter(this.getActivity(), mListener, mSpeakers);
	
		mLvSpeakers.setAdapter(mAdapter);
		setLoading(false);
		if(refreshing){
			refreshing=false;
			mPullToRefreshAttacher.setRefreshComplete();
		}
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

	@Override
	public void onRefreshStarted(View view) {
	
		mProvider.deleteAll(Speaker.class);
	//	mLvSpeakers.setAdapter(null);
		mSpeakers = new ArrayList<Speaker>();
		setSpeakers();
		
	   refreshing=true;
		
	}
}
