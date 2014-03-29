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
