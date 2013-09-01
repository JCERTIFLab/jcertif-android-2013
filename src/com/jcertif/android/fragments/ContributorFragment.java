package com.jcertif.android.fragments;

import com.jcertif.android.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * 
 * @author bashizip
 *
 */
public class ContributorFragment extends RESTResponderFragment{

	private static final String GITHUB_CONTRIBUTOR_API_URL= "https://api.github.com/repos/bashizip/jcertif-android-2013/contributors";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setRetainInstance(true);
		View rootView = inflater.inflate(R.layout.fragment_contributions, container,false);
		getActivity().setTitle(R.string.about);
		return rootView;
	}
	
	@Override
	public void onRESTResult(int code, Bundle result) {
		// TODO Auto-generated method stub
		
	}

}
