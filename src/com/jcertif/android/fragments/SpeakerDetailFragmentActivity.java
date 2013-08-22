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
public class SpeakerDetailFragmentActivity extends SherlockFragmentActivity {

	Fragment speakerDetailFragment;

	public SpeakerDetailFragmentActivity() {
		super();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_speaker_detail_activity);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setNavigationMode(
				com.actionbarsherlock.app.ActionBar.NAVIGATION_MODE_STANDARD);
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		speakerDetailFragment = new SpeakerDetailFragment();
		
		Bundle arg = new Bundle();
		String speakJson=getIntent().getExtras().get("speaker").toString();
		arg.putString("speaker",speakJson );
		speakerDetailFragment.setArguments(arg);		
		ft.add(R.id.speaker_detail_container2, speakerDetailFragment);
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
