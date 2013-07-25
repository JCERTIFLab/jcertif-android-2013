package com.jcertif.android.fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.google.gson.Gson;
import com.jcertif.android.R;
import com.jcertif.android.model.Session;
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
		View rootView = inflater.inflate(R.layout.fragment_session_parent,
				container, false);
		getActivity().setTitle(R.string.session);
		getSherlockActivity().getSupportActionBar().setNavigationMode(
				com.actionbarsherlock.app.ActionBar.NAVIGATION_MODE_STANDARD);

		speakerListFragment = new SessionListFragment();
		FragmentTransaction ft = getChildFragmentManager().beginTransaction();

		ft.add(R.id.list_container, speakerListFragment);
		if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE) {
			speakerDetailFragment = new SpeakerDetailFragment();
			ft.add(R.id.detail_container, speakerDetailFragment);
		}
		ft.commit();
		return rootView;
	}

	private void updateList(String cat) {
		if (speakerListFragment != null) {
			((SessionListFragment) speakerListFragment).updateList(cat);
		}
	}

	@Override
	public void onSpeakerUpdated(Speaker session) {
		// TODO Auto-generated method stub
		if (speakerDetailFragment != null) {
			((SpeakerDetailFragment) speakerDetailFragment)
					.updateSpeakerData(session);
		} else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) < Configuration.SCREENLAYOUT_SIZE_LARGE) {
			getSherlockActivity()
					.getSupportActionBar()
					.setNavigationMode(
							com.actionbarsherlock.app.ActionBar.NAVIGATION_MODE_STANDARD);
			FragmentTransaction ft = getChildFragmentManager()
					.beginTransaction();

			ft.setCustomAnimations(android.R.anim.slide_in_left,
					android.R.anim.slide_out_right,
					android.R.anim.slide_in_left,
					android.R.anim.slide_out_right);

			/*
			 * ft.setCustomAnimations( R.animator.card_flip_right_in,
			 * R.animator.card_flip_right_out, R.animator.card_flip_left_in,
			 * R.animator.card_flip_left_out);
			 */

			speakerDetailFragment = new SpeakerDetailFragment();
			Bundle arg = new Bundle();
			arg.putString("session", new Gson().toJson(session));
			speakerDetailFragment.setArguments(arg);
			ft.replace(R.id.list_container, speakerDetailFragment)
					.addToBackStack(null);
			ft.commit();
		}
	}

}
