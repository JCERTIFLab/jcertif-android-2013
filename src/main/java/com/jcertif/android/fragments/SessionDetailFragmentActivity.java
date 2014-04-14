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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.google.gson.Gson;
import com.jcertif.android.R;

/**
 * 
 * @author Patrick Bashizi
 * 
 */
public class SessionDetailFragmentActivity extends SherlockFragmentActivity {

	Fragment sessionDetailFragment;

	public SessionDetailFragmentActivity() {
		super();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_session_detail_activity);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		getSupportActionBar().setNavigationMode(
				com.actionbarsherlock.app.ActionBar.NAVIGATION_MODE_STANDARD);
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		sessionDetailFragment = new SessionDetailFragment();
		
		Bundle arg = new Bundle();
		String sessJson=getIntent().getExtras().get("session").toString();
		arg.putString("session",sessJson);
		sessionDetailFragment.setArguments(arg);		
		ft.add(R.id.session_detail_container2, sessionDetailFragment);
		ft.commit();

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
