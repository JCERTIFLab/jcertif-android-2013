package com.jcertif.android.fragments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.ActionBar;
import com.google.android.gms.internal.m;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jcertif.android.JcertifApplication;
import com.jcertif.android.MainActivity;
import com.jcertif.android.R;
import com.jcertif.android.adapters.SessionAdapter;
import com.jcertif.android.adapters.SpeedScrollListener;
import com.jcertif.android.adapters.SponsorsAdapter;
import com.jcertif.android.dao.SessionProvider;
import com.jcertif.android.dao.SponsorProvider;
import com.jcertif.android.model.Participant;
import com.jcertif.android.model.Session;
import com.jcertif.android.model.Speaker;
import com.jcertif.android.model.Sponsor;
import com.jcertif.android.service.RESTService;
import com.squareup.picasso.Picasso;

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
		
		tv_bout_jcertif.setText(R.string.lorem);
		
	
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
