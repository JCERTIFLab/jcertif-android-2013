package com.jcertif.android.fragments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.actionbarsherlock.app.ActionBar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jcertif.android.R;
import com.jcertif.android.adapters.ContributorsAdapter;
import com.jcertif.android.adapters.SpeedScrollListener;
import com.jcertif.android.adapters.ContributorsAdapter;
import com.jcertif.android.dao.ContributorProvider;
import com.jcertif.android.dao.ContributorProvider;
import com.jcertif.android.model.Contributor;
import com.jcertif.android.model.Contributor;
import com.jcertif.android.service.RESTService;

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
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 
 * @author bashizip
 * 
 */
public class ContributorFragment extends RESTResponderFragment {

	public static final String GITHUB_CONTRIBUTOR_API_URL = "https://api.github.com/repos/JCERTIFLab/jcertif-android-2013/contributors";
	private static final String TAG = "Contributors Fragment";
	ContributorsAdapter adapter;
	private List<Contributor> mContributors = new ArrayList<Contributor>();
	private ContributorProvider mProvider;
	private GridView gv_contr;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setRetainInstance(true);
		View rootView = inflater.inflate(R.layout.fragment_contributions,
				container, false);
		getActivity().setTitle(R.string.about);
		gv_contr = (GridView) rootView.findViewById(R.id.gv_contributors);

		return rootView;
	}

	public ContributorProvider getProvider() {
		if (mProvider == null) {
			mProvider = new ContributorProvider(this.getSherlockActivity());
		}
		return mProvider;

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		getSherlockActivity().getSupportActionBar().setNavigationMode(
				ActionBar.NAVIGATION_MODE_STANDARD);

		setLoading(true);
		
		mContributors = loadContributorsFromCache();

		gv_contr.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int arg2,
					long position) {
				Contributor sp = ((Contributor) parent
						.getItemAtPosition((int) position));
				browseTo(sp.getHtml_url());
			}

		});
		setContributors();
	}

	private void browseTo(String website) {
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(website));
		startActivity(i);
	}

	private List<Contributor> loadContributorsFromCache() {
		List<Contributor> list = getProvider().getAll(Contributor.class);
		return list;
	}

	@Override
	public void onRESTResult(int code, Bundle resultData) {
		if (resultData == null) {
			Toast.makeText(ContributorFragment.this.getSherlockActivity(),
					R.string.failed_to_load_data_check_your_internet_settings_,
					Toast.LENGTH_SHORT).show();
			return;
		}
		String result = resultData.getString(RESTService.REST_RESULT);

		if (code == 200 && result != null) {

			mContributors = parseContributorJson(result);
			updateList();
			Log.d(TAG, result);
			setContributors();
			saveToCache(mContributors);

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

	private void saveToCache(final List<Contributor> sps) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				for (Contributor sp : sps)
					mProvider.store(sp);
			}
		}).start();

	}

	private List<Contributor> parseContributorJson(String result) {
		Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy hh:mm")
				.create();
		Contributor[] sp = gson.fromJson(result, Contributor[].class);
		return Arrays.asList(sp);
	}

	private void setContributors() {
		Activity activity = getSherlockActivity();
		setLoading(true);
		if (mContributors.isEmpty() && activity != null) {

			Intent intent = new Intent(activity, RESTService.class);
			intent.setData(Uri.parse(GITHUB_CONTRIBUTOR_API_URL));

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
		gv_contr.setOnScrollListener(mListener);
		adapter = new ContributorsAdapter(this.getSherlockActivity(),
				mListener, mContributors);
		gv_contr.setAdapter(adapter);

		setLoading(false);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

	}
}
