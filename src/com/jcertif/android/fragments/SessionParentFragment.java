package com.jcertif.android.fragments;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.jcertif.android.R;
import com.jcertif.android.compound.SlidingUpPanelLayout;
import com.jcertif.android.compound.SlidingUpPanelLayout.PanelSlideListener;
import com.jcertif.android.compound.SpeakerBadge;
import com.jcertif.android.dao.CategorieProvider;
import com.jcertif.android.dao.SessionProvider;
import com.jcertif.android.dao.SpeakerProvider;
import com.jcertif.android.model.Category;
import com.jcertif.android.model.Session;
import com.jcertif.android.model.Speaker;

/**
 * 
 * @author Patrick Bashizi
 * 
 */
public class SessionParentFragment extends SherlockFragment implements
		SessionListFragment.OnSessionUpdatedListener {

	SessionListFragment sessionListFragment;
	SessionDetailFragment sessionDetailFragment;
	String[] actions;

	private Session session;
	private List<Speaker> speakers = new ArrayList<Speaker>();
	private LinearLayout lyt_draggable_area;
	SlidingUpPanelLayout slidingLayout;

	public SessionParentFragment() {
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
				com.actionbarsherlock.app.ActionBar.NAVIGATION_MODE_LIST);

		sessionListFragment = new SessionListFragment();
		FragmentTransaction ft = getChildFragmentManager().beginTransaction();
		ft.add(R.id.list_container, sessionListFragment);

		sessionDetailFragment = new SessionDetailFragment();
		ft.add(R.id.detail_container, sessionDetailFragment);
		ft.commit();

		actions = new CategorieProvider(this.getSherlockActivity()).getLabels();

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				getSherlockActivity().getSupportActionBar().getThemedContext(),
				R.layout.sherlock_spinner_item, actions);
		/** Defining Navigation listener */
		ActionBar.OnNavigationListener navigationListener = new OnNavigationListener() {

			@Override
			public boolean onNavigationItemSelected(int itemPosition,
					long itemId) {
				updateList(actions[itemPosition]);
				return false;
			}
		};
		adapter.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);
		/**
		 * Setting dropdown items and item navigation listener for the actionbar
		 */
		getSherlockActivity().getSupportActionBar().setListNavigationCallbacks(
				adapter, navigationListener);

		if (!onTablet()) {

			slidingLayout = (SlidingUpPanelLayout) rootView
					.findViewById(R.id.sliding_layout);
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

	private void updateList(String cat) {
		if (sessionListFragment != null) {
			((SessionListFragment) sessionListFragment).updateList(cat);
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

	}

	public void updateSessionData(Session session) {

		if (!onTablet()) {

			// if(lyt_draggable_area!=null)slidingLayout.setDragView(lyt_draggable_area);
			slidingLayout.showPane();
			slidingLayout.setPanelHeight(100);
			slidingLayout.expandPane();

		}

	}

	@Override
	public void onSessionUpdated(Session session) {
		this.session = session;
		((SessionDetailFragment) sessionDetailFragment)
				.updateSessionData(session);
		updateSessionData(session);
	}

}
