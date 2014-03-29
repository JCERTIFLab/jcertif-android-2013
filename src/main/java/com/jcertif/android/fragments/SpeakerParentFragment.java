package com.jcertif.android.fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.jcertif.android.R;
import com.jcertif.android.model.Speaker;

/**
 * 
 * @author Patrick Bashizi
 * 
 */
public class SpeakerParentFragment extends SherlockFragment implements
		SpeakeListFragment.OnSpeakerUpdatedListener {

	Fragment speakerDetailFragment;
	Fragment speakerListFragment;

	public SpeakerParentFragment() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		setRetainInstance(true);
		
		View rootView = inflater.inflate(R.layout.fragment_speaker_parent,
				container, false);
		getActivity().setTitle(R.string.spakers);
		getSherlockActivity().getSupportActionBar().setNavigationMode(
				com.actionbarsherlock.app.ActionBar.NAVIGATION_MODE_STANDARD);
		if (savedInstanceState == null) {
			speakerListFragment = new SpeakeListFragment();
			FragmentTransaction ft = getChildFragmentManager()
					.beginTransaction();

			ft.add(R.id.speaker_list_container, speakerListFragment);
			if (onTablet()) {
				speakerDetailFragment = new SpeakerDetailFragment();
				ft.add(R.id.speaker_detail_container, speakerDetailFragment);
			}

			ft.commit();
		}
		return rootView;
	}

	private boolean onTablet() {
		return ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE);
	}

	@Override
	public void onSpeakerUpdated(Speaker session) {
		if (speakerDetailFragment != null) {
			((SpeakerDetailFragment) speakerDetailFragment)
					.updateSpeakerData(session);
		} else if (!onTablet()) {

		}
	}

}
