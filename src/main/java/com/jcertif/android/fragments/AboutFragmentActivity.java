package com.jcertif.android.fragments;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.google.gson.Gson;
import com.jcertif.android.R;
import com.jcertif.android.adapters.AboutPagerAdapter;
import com.jcertif.android.adapters.SessionSpeakerPagerAdapter;
import com.viewpagerindicator.TitlePageIndicator;

/**
 * 
 * @author Patrick Bashizi
 * 
 */
public class AboutFragmentActivity extends SherlockFragmentActivity {

	Fragment aboutFragment;
	Fragment sponsorsFragment;
	Fragment contributorFragment;
	Fragment externalLibsFragment;

	AboutPagerAdapter mAdapter;
	private List<Fragment> fragments;

	public AboutFragmentActivity() {
		super();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_about_pager);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		getSupportActionBar().setNavigationMode(
				com.actionbarsherlock.app.ActionBar.NAVIGATION_MODE_STANDARD);

		aboutFragment = new AboutFragment();
		sponsorsFragment = new SponsorsFragment();
		contributorFragment= new ContributorFragment();
		externalLibsFragment = new ExternalLibsUsedFragment();

		fragments = new ArrayList<Fragment>();
		fragments.add(aboutFragment);
		fragments.add(sponsorsFragment);
		fragments.add(contributorFragment);
		fragments.add(externalLibsFragment);

		mAdapter = new AboutPagerAdapter(this,
				super.getSupportFragmentManager(), fragments);

		ViewPager pager = (ViewPager) findViewById(R.id.viewpager_about);
		pager.setAdapter(mAdapter);

		TitlePageIndicator titleIndicator = (TitlePageIndicator) findViewById(R.id.vpi_about);
		titleIndicator.setViewPager(pager);
     	titleIndicator.setBackgroundColor(Color.WHITE);
		titleIndicator.setFooterColor(getResources().getColor(
				R.color.body_text_1_negative));
		titleIndicator.setTextColor(getResources().getColor(
				R.color.body_text_1_negative));
		titleIndicator.setSelectedColor(getResources().getColor(
				R.color.body_text_1_negative));
		titleIndicator.setSelectedBold(true);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
