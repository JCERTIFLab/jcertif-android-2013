package com.jcertif.android.fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.actionbarsherlock.app.SherlockFragment;
import com.google.gson.Gson;
import com.jcertif.android.R;
import com.jcertif.android.compound.SlidingUpPanelLayout;
import com.jcertif.android.compound.SlidingUpPanelLayout.PanelSlideListener;
import com.jcertif.android.model.Speaker;

/**
 * 
 * @author Patrick Bashizi
 * 
 */
public class SpeakerParentFragment extends SherlockFragment implements
		SpeakeListFragment.OnSpeakerUpdatedListener {

	Fragment speakerListFragment;
	Fragment speakerDetailFragment;
	
	
	SlidingUpPanelLayout slidingLayout;
	private LinearLayout lyt_draggable_area;
	private Speaker speaker;

	public SpeakerParentFragment() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setRetainInstance(true);
		View rootView = inflater.inflate(R.layout.fragment_speaker_parent,
				container, false);
		getActivity().setTitle(R.string.session);
		getSherlockActivity().getSupportActionBar().setNavigationMode(
				com.actionbarsherlock.app.ActionBar.NAVIGATION_MODE_STANDARD);
		
	

		speakerListFragment = new SpeakeListFragment();
		speakerDetailFragment = new SpeakerDetailFragment();
		
		FragmentTransaction ft = getChildFragmentManager().beginTransaction();
		ft.add(R.id.speaker_list_container, speakerListFragment);
	    ft.add(R.id.speaker_detail_container, speakerDetailFragment);
		
		ft.commit();	
		
	
		
		if (!onTablet()) {

			slidingLayout = (SlidingUpPanelLayout) rootView
					.findViewById(R.id.speaker_sliding_layout);
			slidingLayout.setShadowDrawable(getResources().getDrawable(
					R.drawable.above_shadow));
			if (lyt_draggable_area != null)
			slidingLayout.setDragView(lyt_draggable_area);
			slidingLayout.setPanelHeight(0);
			slidingLayout.setPanelSlideListener(new PanelSlideListener() {

				@Override
				public void onPanelSlide(View panel, float slideOffset) {
					if (slideOffset < 0.2) {
						if (getSherlockActivity().getSupportActionBar()
								.isShowing()) {
							getSherlockActivity().getSupportActionBar().hide();
						}
					} else {
						if (!getSherlockActivity().getSupportActionBar()
								.isShowing()) {
							getSherlockActivity().getSupportActionBar().show();
						}
					}
				}

				@Override
				public void onPanelExpanded(View panel) {

				}

				@Override
				public void onPanelCollapsed(View panel) {

				}
			});
		}
		return rootView;
	}

	private boolean onTablet() {
		return ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE);
	}
	
	@Override
	public void onSpeakerUpdated(Speaker sp) {		
		this.speaker = sp;
		((SpeakerDetailFragment) speakerDetailFragment).updateSpeakerData(sp);
		updateSpeakerData(sp);
	}

	private void updateSpeakerData(Speaker sp) {
		if (!onTablet()) {
			slidingLayout.showPane();
			slidingLayout.setPanelHeight(100);
			slidingLayout.expandPane();

		}		
	}
}
