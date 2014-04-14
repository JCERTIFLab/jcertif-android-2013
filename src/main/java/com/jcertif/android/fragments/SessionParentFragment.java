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

import java.util.ArrayList;
import java.util.List;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.actionbarsherlock.app.SherlockFragment;
import com.google.gson.Gson;
import com.jcertif.android.R;
import com.jcertif.android.dao.CategorieProvider;
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

	public SessionParentFragment() {
		super();
	//	setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		//setRetainInstance(true);
		View rootView = inflater.inflate(R.layout.fragment_session_parent,
				container, false);
		getActivity().setTitle(R.string.session);
		
		getSherlockActivity().getSupportActionBar().setNavigationMode(
				com.actionbarsherlock.app.ActionBar.NAVIGATION_MODE_LIST);
		sessionListFragment = new SessionListFragment();
		FragmentTransaction ft = getChildFragmentManager().beginTransaction();
		ft.add(R.id.session_list_container, sessionListFragment);
		if (onTablet()) {
			sessionDetailFragment = new SessionDetailFragment();
			ft.add(R.id.session_detail_container, sessionDetailFragment);
		}
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
		if (sessionDetailFragment != null) {
			((SessionDetailFragment) sessionDetailFragment)
					.updateSessionData(session);
		} else if (!onTablet()) {
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

			sessionDetailFragment = new SessionDetailFragment();
			Bundle arg = new Bundle();
			arg.putString("speaker", new Gson().toJson(session));
			sessionDetailFragment.setArguments(arg);
			ft.replace(R.id.session_list_container, sessionDetailFragment);
			ft.addToBackStack(null);
			ft.commit();
		}
	}

	@Override
	public void onSessionUpdated(Session session) {
		if (sessionDetailFragment != null) {
			((SessionDetailFragment) sessionDetailFragment)
					.updateSessionData(session);
		} else if (!onTablet()) {
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

			sessionDetailFragment = new SessionDetailFragment();
			Bundle arg = new Bundle();
			arg.putString("session", new Gson().toJson(session));
			sessionDetailFragment.setArguments(arg);
			ft.replace(R.id.session_list_container, sessionDetailFragment);
			ft.addToBackStack(null);
			ft.commit();
		}
	}

}
