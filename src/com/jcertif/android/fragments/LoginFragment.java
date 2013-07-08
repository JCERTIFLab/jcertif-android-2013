package com.jcertif.android.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jcertif.android.R;

public class LoginFragment extends RESTResponderFragment {


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setRetainInstance(true);
		View rootView = inflater.inflate(R.layout.fragment_login, container,
				false);
		getActivity().setTitle(getString(R.string.login));
		return rootView;
	}
	@Override
	public void onRESTResult(int code, String result) {
	
	}
	
}
