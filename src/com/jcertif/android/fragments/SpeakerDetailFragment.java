package com.jcertif.android.fragments;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.jcertif.android.R;
import com.jcertif.android.adapters.SpeakerAdapter;
import com.jcertif.android.adapters.SpeedScrollListener;
import com.jcertif.android.dao.SpeakerProvider;
import com.jcertif.android.model.Speaker;
import com.jcertif.android.service.RESTService;
import com.squareup.picasso.Picasso;



public class SpeakerDetailFragment extends RESTResponderFragment {

	private static String TAG = SpeakerDetailFragment.class.getName();
	
	private Speaker speaker;
	TextView tv_fullname, tv_company, tv_website, tv_country, tv_bio;
	ImageView img_sp_avatar;

	private List<Speaker> mSpeakers=new ArrayList<Speaker>();
	private ListView mLvSpeakers; 
	private SpeakerAdapter mAdapter; 
	private SpeakerProvider mProvider; 
	private SpeedScrollListener mListener; 
	private Button btn_session;

	public SpeakerDetailFragment() {
		// Empty constructor required for fragment subclasses
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_speaker_detail, container,
				false);
		
		getActivity().setTitle(R.string.app_name);
		setHasOptionsMenu(true);
		
		img_sp_avatar=(ImageView)rootView.findViewById(R.id.img_sp_avatar);
		tv_fullname=(TextView)rootView.findViewById(R.id.tv_sp_fullname);
	
		tv_company=(TextView)rootView.findViewById(R.id.tv_sp_company);
		tv_website=(TextView)rootView.findViewById(R.id.tv_sp_website);
		tv_country=(TextView)rootView.findViewById(R.id.tv_sp_country);
		tv_bio=(TextView)rootView.findViewById(R.id.tv_sp_bio);
		btn_session=(Button) rootView.findViewById(R.id.btn_see_sessions_speaker);
		
		
		btn_session.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(SpeakerDetailFragment.this.getSherlockActivity(),SpeakerSessionFragment.class);
				intent.putExtra(SpeakerSessionFragment.SPEAKER_ID, speaker.getEmail());
				startActivity(intent);
			}
		});
		String speakerjson=null;
		if(getArguments() != null && !getArguments().isEmpty()){
			speakerjson=getArguments().get("speaker").toString();
		}
		if(speakerjson != null){		
			speaker=(Speaker)new Gson().fromJson((speakerjson),Speaker.class);
			updateSpeakerData(speaker);
		}
		return rootView;
	}

	
	
	/** Instance of SpeakerProvider */
	public SpeakerProvider getProvider() {
		if (mProvider == null)
			mProvider = new SpeakerProvider(this.getSherlockActivity());
		return mProvider;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getSherlockActivity().getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		
	}

	
	public void updateSpeakerData(Speaker s) {
		this.speaker=s;
		tv_fullname.setText(speaker.getFirstname() + " " + speaker.getLastname());
	
		tv_company.setText(speaker.getCompany());
		tv_website.setText(speaker.getWebsite());
		tv_country.setText(speaker.getCountry());
		tv_bio.setText(Html.fromHtml(speaker.getBiography()));
		Picasso.with(getSherlockActivity()).load(s.getPhoto()).into(img_sp_avatar);
		
	}

	@Override
	public void onRESTResult(int code, Bundle resultData) {
		String result=	resultData.getString(RESTService.REST_RESULT);
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
	}
}
