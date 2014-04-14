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
				ctx.getResources().getString(R.string.sposors),
				ctx.getResources().getString(R.string.contributor),
				ctx.getResources().getString(R.string.open_source_licence)
				};
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
