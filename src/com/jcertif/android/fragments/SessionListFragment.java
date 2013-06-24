package com.jcertif.android.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.jcertif.android.R;

public class SessionListFragment extends SherlockFragment{
	
	  private static final String ARG_SESSION_ID ="0x1";

	public SessionListFragment() {
	         // Empty constructor required for fragment subclasses
	      }

	      @Override
	      public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	         View rootView = inflater.inflate(R.layout.fragment_session, container, false);
	        // int i = getArguments().getInt(ARG_SESSION_ID);
	         String session = getResources().getStringArray(R.array.menu_array)[0];

	         getActivity().setTitle(session);
	         return rootView;
	      }
}
