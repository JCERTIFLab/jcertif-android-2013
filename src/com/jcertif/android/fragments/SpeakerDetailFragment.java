package com.jcertif.android.fragments;

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

import com.actionbarsherlock.app.ActionBar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jcertif.android.JcertifApplication;
import com.jcertif.android.MainActivity;
import com.jcertif.android.R;
import com.jcertif.android.adapters.SpeakerAdapter;
import com.jcertif.android.adapters.SpeedScrollListener;
import com.jcertif.android.dao.SpeakerProvider;
import com.jcertif.android.model.Session;
import com.jcertif.android.model.Speaker;
import com.jcertif.android.service.RESTService;


/**
 * *
 * @author Patrick Bashizi
 *
 */
public class SpeakerDetailFragment extends RESTResponderFragment {

	private static String TAG = SessionListFragment.class.getName();

	private List<Speaker> mSpeakers;
	private ListView mLvSpeakers;
	private SpeakerAdapter mAdapter;
	private SpeakerProvider mProvider;
	private SpeedScrollListener mListener;

	public SpeakerDetailFragment() {
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
		getSherlockActivity().getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		
	}



	public void updateSpeakerData(Speaker s) {
		
		
	}

	@Override
	public void onRESTResult(int code, Bundle resultData) {
		String result=	resultData.getString(RESTService.REST_RESULT);
		// TODO Auto-generated method stub
		
	}
}
