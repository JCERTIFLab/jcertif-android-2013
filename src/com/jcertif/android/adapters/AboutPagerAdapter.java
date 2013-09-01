package com.jcertif.android.adapters;

import java.util.List;

import com.jcertif.android.R;
import com.jcertif.android.fragments.SessionDetailFragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class AboutPagerAdapter extends FragmentPagerAdapter {

	private final List<Fragment> fragments;
	protected static String[] CONTENT;
	private Context ctx;

	public AboutPagerAdapter(Context ctx, FragmentManager fm,
			List<Fragment> fragments) {
		super(fm);
		this.ctx = ctx;
		this.fragments = fragments;
		CONTENT = new String[] { ctx.getResources().getString(R.string.about),
				ctx.getResources().getString(R.string.sposors) };
	}

	@Override
	public Fragment getItem(int position) {
		return this.fragments.get(position);
	}

	@Override
	public int getCount() {
		return this.fragments.size();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return CONTENT[position];
	}


}
