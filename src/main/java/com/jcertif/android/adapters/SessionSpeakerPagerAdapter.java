package com.jcertif.android.adapters;
import java.util.List;

import com.jcertif.android.fragments.SessionDetailFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


public class SessionSpeakerPagerAdapter extends FragmentPagerAdapter {

		private final List<SessionDetailFragment> fragments;

		//On fournit à l'adapter la liste des fragments à afficher
		public SessionSpeakerPagerAdapter(FragmentManager fm, List<SessionDetailFragment> fragments) {
			super(fm);
			this.fragments = fragments;
		}

		@Override
		public Fragment getItem(int position) {
			return this.fragments.get(position);
		}

		@Override
		public int getCount() {
			return this.fragments.size();
		}
	}

