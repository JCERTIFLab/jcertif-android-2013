package com.jcertif.android.fragments;

import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.google.gson.Gson;
import com.jcertif.android.R;
import com.jcertif.android.adapters.SpeakerAdapter;
import com.jcertif.android.adapters.SpeedScrollListener;
import com.jcertif.android.dao.SpeakerProvider;
import com.jcertif.android.model.Speaker;
import com.jcertif.android.service.RESTService;


/**
 * *
 * @author Patrick Bashizi
 *
 */
public class SpeakerDetailFragment extends RESTResponderFragment {

	private static String TAG = SessionListFragment.class.getName();
	
	private Speaker speaker;
	TextView tv_fullname, tv_title, tv_company, tv_website, tv_country, tv_bio;
	ImageView img_sp_avatar;

	private List<Speaker> mSpeakers=new ArrayList<Speaker>();
	private ListView mLvSpeakers; 
	private SpeakerAdapter mAdapter; 
	private SpeakerProvider mProvider; 
	private SpeedScrollListener mListener; 

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
		tv_title=(TextView)rootView.findViewById(R.id.tv_sp_title);
		tv_company=(TextView)rootView.findViewById(R.id.tv_sp_company);
		tv_website=(TextView)rootView.findViewById(R.id.tv_sp_website);
		tv_country=(TextView)rootView.findViewById(R.id.tv_sp_country);
		tv_bio=(TextView)rootView.findViewById(R.id.tv_sp_bio);
		
		Object speakerjson=null;
		if(getArguments() != null && !getArguments().isEmpty()){
			speakerjson=getArguments().get("speaker");
		}
		if(speakerjson != null){
			speaker=(Speaker)new Gson().fromJson(speakerjson.toString(),Speaker.class);
			updateSpeakerData(speaker);
		}
		
		
		return rootView;
	}

	
	class SpeakerDetailsLoaderTask extends AsyncTask<Void,Void,Void>{

		@Override
		protected Void doInBackground(Void... params) {
			//Load speaker
			String speakerEmail=speaker.getEmail();
			Speaker speaker=getProvider().getByEmail(speakerEmail);
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}	
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
		tv_title.setText(speaker.getTitle());
		tv_company.setText(speaker.getCompany());
		tv_website.setText(speaker.getWebsite());
		tv_country.setText(speaker.getCountry());
		tv_bio.setText(speaker.getBiography());
		
		/** execute the task*/
		new SpeakerDetailsLoaderTask().execute();
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
