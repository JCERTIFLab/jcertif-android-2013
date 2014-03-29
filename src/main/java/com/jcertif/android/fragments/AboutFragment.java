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
