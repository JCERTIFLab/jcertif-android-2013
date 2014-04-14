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

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.db4o.cs.internal.messages.MFailed;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.Button;

import com.google.gson.Gson;
import com.jcertif.android.*;
import com.jcertif.android.adapters.SessionSpeakerPagerAdapter;
import com.jcertif.android.dao.SessionProvider;
import com.jcertif.android.model.Session;

public class SpeakerSessionFragment extends SherlockFragmentActivity {

	PagerAdapter mPagerAdapter;
	int position = 0;
	ViewPager pager;
	List<SessionDetailFragment> fragments;
	Handler handler ;

	public static final String SPEAKER_ID = "speakerID";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.seaker_session_fragment);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		String speakerId = getIntent().getStringExtra(SPEAKER_ID);

		List<Session> sessions = new SessionProvider(this)
				.getAllSessionOfSpeaker(speakerId);
		 fragments = new ArrayList<SessionDetailFragment>();

		for (Session s : sessions) {
			SessionDetailFragment f = new SessionDetailFragment();
			Bundle b = new Bundle();
			b.putString("session", new Gson().toJson(s));
			f.setArguments(b);
			fragments.add(f);
		}

		this.mPagerAdapter = new SessionSpeakerPagerAdapter(
				super.getSupportFragmentManager(), fragments);

		pager = (ViewPager) super.findViewById(R.id.viewpager_speker_session);
		pager.setAdapter(this.mPagerAdapter);

	//	handler = new Handler();
		//handler.postDelayed(runnable, 1000);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case android.R.id.home:
	      onBackPressed();
	        return true;
	    default: return super.onOptionsItemSelected(item);  
	    }
	}
}
/*
	final Runnable runnable = new Runnable() {
		public void run() {
			if (position >=fragments.size()-1) {
				position = 0;
			} else {
				position = position + 1;
			}
			pager.setCurrentItem(position, true);
			handler.postDelayed(runnable, 5000);
		}
	};*/
	
/*	@Override
	public void onPause() {
	    super.onPause();
	    if (handler!= null) {
	        handler.removeCallbacks(runnable);
	    }
	}


	@Override
	public void onResume() {
	    super.onResume();  // Always call the superclass method first
	    handler.postDelayed(runnable, 5000);
	}
}*/
