package com.jcertif.android.fragments;

import java.util.Arrays;
import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.jcertif.android.JcertifApplication;
import com.jcertif.android.MainActivity;
import com.jcertif.android.R;
import com.jcertif.android.dao.JCertifDb4oHelper;
import com.jcertif.android.dao.SponsorProvider;
import com.jcertif.android.model.JCertifObject;
import com.jcertif.android.model.Session;
import com.jcertif.android.model.Sponsor;
import com.jcertif.android.service.RESTService;


/**
 * 
 * @author bashizip
 *
 */
public class InitialisationFragment extends RESTResponderFragment {

	private ProgressBar pb_init;
	private TextView tv_init;

    private final String SPONSOR_LEVEL_URI=JcertifApplication.BASE_URL+"/ref/sponsorlevel/list";
    private final String SESSION_STATUS_URI=JcertifApplication.BASE_URL+"/ref/sessionstatus/list";
    private final String CIVILITES__URI=JcertifApplication.BASE_URL+"/ref/title/list";
    private final String CATEGORIES__URI=JcertifApplication.BASE_URL+"/ref/category/list";
   
   
	public InitialisationFragment() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setRetainInstance(true);
		View rootView = inflater.inflate(R.layout.fragment_init, container,
				false);
		pb_init=(ProgressBar)rootView.findViewById(R.id.pb_init);
		tv_init=(TextView)rootView.findViewById(R.id.tv_init);
		getActivity().setTitle(R.string.app_name);	
		return rootView;
	}


	void loadData(String URI){
		
		MainActivity activity = (MainActivity) getActivity();
		Intent intent = new Intent(activity, RESTService.class);
		intent.setData(Uri.parse(URI));
		Bundle params = new Bundle();
		params.putString(RESTService.KEY_JSON_PLAYLOAD, null);		
		intent.putExtra(RESTService.EXTRA_PARAMS, params);
		intent.putExtra(RESTService.EXTRA_RESULT_RECEIVER,
				getResultReceiver());
		activity.startService(intent);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getSherlockActivity().getSupportActionBar().
		setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
	}

	@Override
	public void onRESTResult(int code, Bundle resultData) {
		String result=	resultData.getString(RESTService.REST_RESULT);
		String resultType= resultData.getString(RESTService.KEY_URI_SENT);
		
	
		if(resultType.equals(SPONSOR_LEVEL_URI)){
			List<Sponsor> sponsors= parseSponsorJson(result);
			saveSponsorToCache(sponsors);
		}
	}


	private List<Sponsor> parseSponsorJson(String result) {
		Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy hh:mm")
				.create();
		Sponsor[] sessions = gson.fromJson(result, Sponsor[].class);

		return Arrays.asList(sessions);
	}
	
	
	protected void saveSponsorToCache(final List<Sponsor> sponsors) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				for (Sponsor session : sponsors)
					new SponsorProvider(InitialisationFragment.this.getSherlockActivity()).store(session);
			}
		}).start();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

	}

}
