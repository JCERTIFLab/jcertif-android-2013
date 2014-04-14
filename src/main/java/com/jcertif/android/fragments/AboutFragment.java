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
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.jcertif.android.R;
import com.jcertif.android.util.Utils;

import java.io.IOException;
import java.io.InputStream;

/**
 * 
 * @author bashizip
 *
 */
public class AboutFragment extends RESTResponderFragment {

	private TextView tv_bout_jcertif;


	private static final String TAG = "About Fragment";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setRetainInstance(true);
		View rootView = inflater.inflate(R.layout.fragment_about, container,
				false);
		getActivity().setTitle(R.string.about);
		tv_bout_jcertif = (TextView) rootView
				.findViewById(R.id.tv_about_jcertif);
		
	
        InputStream is = null;
        try {
            is = getResources().getAssets().open("about.html");
            tv_bout_jcertif.setText(Html.fromHtml(Utils.inputStreamToString(is)));
        } catch (IOException e) {
            e.printStackTrace();  
        }
		
		return rootView;
	}

	  
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		getSherlockActivity().getSupportActionBar().setNavigationMode(
				ActionBar.NAVIGATION_MODE_STANDARD);

	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

	}

	@Override
	public void onRESTResult(int code, Bundle result) {
		// TODO Auto-generated method stub
		
	}

}
