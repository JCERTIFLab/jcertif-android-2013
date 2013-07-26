package com.jcertif.android.fragments;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.google.gson.Gson;
import com.jcertif.android.R;
import com.jcertif.android.compound.SlidingUpPanelLayout;
import com.jcertif.android.compound.SpeakerBadge;
import com.jcertif.android.compound.SlidingUpPanelLayout.PanelSlideListener;
import com.jcertif.android.dao.SpeakerProvider;
import com.jcertif.android.model.Session;
import com.jcertif.android.model.Speaker;

/**
 * 
 * @author Patrick Bashizi
 * 
 */
public class SessionParentFragment extends SherlockFragment implements
		SessionListFragment.OnSessionUpdatedListener {

	Fragment sessionDetailFragment;
	Fragment sessionListFragment;

	String[] actions = new String[] { "All", "Android", "HTML5", "Java",
			"Entreprise", "Web Design", "Community" };

	private Session session;
	TextView tv_title, tv_desc, tv_date_room, tv_sep_desc, tv_sep_speaker;
	private List<Speaker> speakers = new ArrayList<Speaker>();
	private LinearLayout lyt_detail, 
	lyt_draggable;

	SlidingUpPanelLayout slidingLayout;
	ScrollView scroll;

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

		tv_title = (TextView) rootView
				.findViewById(R.id.tv_title_session_detail);
		tv_desc = (TextView) rootView.findViewById(R.id.tv_description);
		tv_sep_desc = (TextView) rootView.findViewById(R.id.tv_separator_desc);
		tv_date_room = (TextView) rootView.findViewById(R.id.tv_date_room);
		tv_sep_speaker = (TextView) rootView
				.findViewById(R.id.tv_separator_speaker);
		lyt_detail = (LinearLayout) rootView
				.findViewById(R.id.lyt_badges);
		
		 scroll = (ScrollView) rootView.findViewById(R.id.sv);
	
		
		lyt_draggable = (LinearLayout) rootView
				.findViewById(R.id.lyt_draggabledraggable);

		sessionListFragment = new SessionListFragment();
		FragmentTransaction ft = getChildFragmentManager().beginTransaction();
		ft.add(R.id.list_container, sessionListFragment);

		if (onTablet()) {
			sessionDetailFragment = new SessionDetailFragment();
			ft.add(R.id.detail_container, sessionDetailFragment);
		}

		ft.commit();

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

		slidingLayout = (SlidingUpPanelLayout) rootView
				.findViewById(R.id.sliding_layout);
		if (!onTablet()) {
			slidingLayout.setShadowDrawable(getResources().getDrawable(
					R.drawable.above_shadow));

			slidingLayout.setDragView(lyt_draggable);
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
					getSherlockActivity()
							.getSupportActionBar()
							.setNavigationMode(
									com.actionbarsherlock.app.ActionBar.NAVIGATION_MODE_LIST);

				}

				@Override
				public void onPanelCollapsed(View panel) {
					getSherlockActivity()
							.getSupportActionBar()
							.setNavigationMode(
									com.actionbarsherlock.app.ActionBar.NAVIGATION_MODE_LIST);
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
		tv_title.setText(session.getTitle());
		tv_desc.setText(session.getDescription());
		tv_date_room.setText(getString(R.string.from_) + " "
				+ session.getStart() + getString(R.string._to_)
				+ session.getEnd() + " in room " + session.getSalle());
		tv_sep_speaker.setText(formatSeparator(getResources().getString(
				R.string.spakers).toUpperCase()));
		tv_sep_desc.setText(formatSeparator(getResources().getString(
				R.string.desc)));

		if (!onTablet()) {
			slidingLayout.showPane();
			slidingLayout.setPanelHeight(100);
			slidingLayout.expandPane();
			slidingLayout.setDragView(lyt_draggable);
		}
		new SpeakerLoaderTask().execute();
	}

	void loadSpeakers() {
		// load speakers
		SpeakerProvider seProvider = new SpeakerProvider(this.getActivity());
		String[] speakerEmails = session.getSpeakers();
		for (int i = 0; i < speakerEmails.length; i++) {
			Speaker speaker = seProvider.getByEmail(speakerEmails[i]);
			speakers.add(speaker);
		}
		
	}

	class SpeakerLoaderTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			loadSpeakers();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (!speakers.isEmpty())
				for (Speaker sp : speakers) {
					lyt_detail.removeAllViews();
									lyt_detail.addView(new SpeakerBadge(
							SessionParentFragment.this.getActivity(), sp));
				}
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

	}

	SpannableString formatSeparator(String sep) {
		SpannableString content = new SpannableString(sep);
		content.setSpan(new UnderlineSpan(), 0, sep.length(), 0);
		return content;
	}

	@Override
	public void onSessionUpdated(Session session) {
		this.session = session;
	//	this.session.setDescription(session.getDescription()+getFakeText());
		if (sessionDetailFragment != null) {
			((SessionDetailFragment) sessionDetailFragment)
					.updateSessionData(session);
		} else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) < Configuration.SCREENLAYOUT_SIZE_LARGE) {
			getSherlockActivity()
					.getSupportActionBar()
					.setNavigationMode(
							com.actionbarsherlock.app.ActionBar.NAVIGATION_MODE_STANDARD);

			updateSessionData(session);
		}
	}
	


}
