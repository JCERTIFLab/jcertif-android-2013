package com.jcertif.android.fragments;

import java.util.List;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.google.gson.Gson;
import com.jcertif.android.R;
import com.jcertif.android.model.Session;

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
			"Entreprise", "Web Design"

	};

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
		if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE) {
		
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

		return rootView;
	}

	private void updateList(String cat) {
		if (sessionListFragment != null) {
			((SessionListFragment) sessionListFragment).updateList(cat);
		}
	}

	@Override
	public void onSessionUpdated(Session session) {
		if (sessionDetailFragment != null) {
			((SessionDetailFragment) sessionDetailFragment)
					.updateSessionData(session);
		} else if ((getResources().getConfiguration().screenLayout & 
				Configuration.SCREENLAYOUT_SIZE_MASK) < Configuration.SCREENLAYOUT_SIZE_LARGE) {
			getSherlockActivity().getSupportActionBar().setNavigationMode(
					com.actionbarsherlock.app.ActionBar.NAVIGATION_MODE_STANDARD);
			FragmentTransaction ft = getChildFragmentManager()
					.beginTransaction();
		
			
			ft.setCustomAnimations(android.R.anim.slide_in_left,
	                android.R.anim.slide_out_right, android.R.anim.slide_in_left,
	                android.R.anim.slide_out_right);
			
		/*	ft.setCustomAnimations(
                    R.animator.card_flip_right_in, R.animator.card_flip_right_out,
                    R.animator.card_flip_left_in, R.animator.card_flip_left_out);*/
			
			sessionDetailFragment = new SessionDetailFragment();
			Bundle arg = new Bundle();
			arg.putString("session", new Gson().toJson(session));
			sessionDetailFragment.setArguments(arg);
			ft.replace(R.id.list_container, sessionDetailFragment).addToBackStack(null);
			ft.commit();
		}
	}

}
