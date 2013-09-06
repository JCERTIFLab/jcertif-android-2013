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
public class SponsorsFragment extends RESTResponderFragment {

	private GridView gv_sponsors;
	SponsorsAdapter adapter;
	private List<Sponsor> mSponsors = new ArrayList<Sponsor>();
	private SponsorProvider mProvider;

	private static final String SPONSOR_LIST_URI = JcertifApplication.BASE_URL
			+ "/sponsor/list";
	private static final String TAG = "About Fragment";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setRetainInstance(true);
		View rootView = inflater.inflate(R.layout.fragment_sponsors, container,
				false);
		getActivity().setTitle(R.string.about);
		
		
		gv_sponsors = (GridView) rootView.findViewById(R.id.gv_sponsors);
		return rootView;
	}

	public SponsorProvider getProvider() {
		if (mProvider == null){
			mProvider = new SponsorProvider(this.getSherlockActivity());
		}
		return mProvider;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		getSherlockActivity().getSupportActionBar().setNavigationMode(
				ActionBar.NAVIGATION_MODE_STANDARD);

		mSponsors = loadSponsorsFromCache();
		
		gv_sponsors.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int arg2,
					long position) {
				Sponsor sp = ((Sponsor) parent
						.getItemAtPosition((int) position));
				browseTo(sp.getWebsite());
			}

			
		});
		setSposors();
	}
	private void browseTo(String website) {		
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(website));
		startActivity(i);
	}

	private List<Sponsor> loadSponsorsFromCache() {
		List<Sponsor> list = getProvider().getAll(Sponsor.class);
		return list;
	}

	@Override
	public void onRESTResult(int code, Bundle resultData) {
		if(resultData==null)
		{Toast.makeText(
				SponsorsFragment.this.getSherlockActivity(),
				R.string.failed_to_load_data_check_your_internet_settings_,
				Toast.LENGTH_SHORT).show();
			return;
		}
		String result = resultData.getString(RESTService.REST_RESULT);
	
		if (code == 200 && result != null) {

			mSponsors = parseSponsorJson(result);
			updateList();
			Log.d(TAG, result);
			setSposors();
			saveToCache(mSponsors);
		
		
			
		} else {
			Activity activity = getActivity();
			if (activity != null) {
				Toast.makeText(
						activity,
						R.string.failed_to_load_data_check_your_internet_settings_,
						Toast.LENGTH_SHORT).show();

			}
		}
	}

	private void saveToCache(final List<Sponsor> sps) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				for (Sponsor sp : sps)
					mProvider.store(sp);
			}
		}).start();

	}

	private List<Sponsor> parseSponsorJson(String result) {
		Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy hh:mm")
				.create();
		Sponsor[] sp = gson.fromJson(result, Sponsor[].class);
		return Arrays.asList(sp);
	}

	private void setSposors() {
		Activity activity = getSherlockActivity();
		setLoading(true);
		if (mSponsors.isEmpty() && activity != null) {

			Intent intent = new Intent(activity, RESTService.class);
			intent.setData(Uri.parse(SPONSOR_LIST_URI));

			// Here we are going to place our REST call parameters.
			Bundle params = new Bundle();
			params.putString(RESTService.KEY_JSON_PLAYLOAD, null);

			intent.putExtra(RESTService.EXTRA_PARAMS, params);
			intent.putExtra(RESTService.EXTRA_RESULT_RECEIVER,
					getResultReceiver());

			activity.startService(intent);
		} else if (activity != null) {
			updateList();
			setLoading(false);
		}
	}
	private SpeedScrollListener mListener;

	void updateList() {

		mListener = new SpeedScrollListener();
		gv_sponsors.setOnScrollListener(mListener);
	
		adapter = new SponsorsAdapter(this.getSherlockActivity(), mListener, mSponsors);
		gv_sponsors.setAdapter(adapter);
		
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

	}

}
