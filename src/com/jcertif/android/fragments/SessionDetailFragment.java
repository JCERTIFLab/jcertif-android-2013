package com.jcertif.android.fragments;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.format.DateFormat;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.google.gson.Gson;
import com.jcertif.android.R;
import com.jcertif.android.compound.SpeakerBadge;
import com.jcertif.android.dao.SessionProvider;
import com.jcertif.android.dao.SpeakerProvider;
import com.jcertif.android.model.Session;
import com.jcertif.android.model.Speaker;

public class SessionDetailFragment extends RESTResponderFragment {

	private Session session;
	TextView tv_title, tv_desc, tv_date_room, tv_sep_desc, tv_sep_speaker;
    private List<Speaker> speakers= new ArrayList<Speaker>();
	
	private LinearLayout lyt_detail;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// setRetainInstance(true);
		View rootView = inflater.inflate(R.layout.fragment_detail_session,
				container, false);
		getActivity().setTitle(R.string.app_name);
		setHasOptionsMenu(true);
		tv_title = (TextView) rootView
				.findViewById(R.id.tv_title_session_detail);
		tv_desc = (TextView) rootView.findViewById(R.id.tv_description);
		tv_sep_desc = (TextView) rootView.findViewById(R.id.tv_separator_desc);
		tv_date_room = (TextView) rootView.findViewById(R.id.tv_date_room);
		tv_sep_speaker = (TextView) rootView
				.findViewById(R.id.tv_separator_speaker);
		
		lyt_detail=(LinearLayout)rootView.findViewById(R.id.lyt_spesaker_badge);

		Object sessionjson = null;
		if (getArguments() != null && !getArguments().isEmpty()) {
			sessionjson = getArguments().get("session");
		}
		if (sessionjson != null) {
			session = (Session) new Gson().fromJson(sessionjson.toString(),
					Session.class);
			updateSessionData(session);
		}
		return rootView;
	}

	void loadSpeakers() {
		// load speakers
		SpeakerProvider seProvider = new SpeakerProvider(this.getActivity());
		String[] speakerEmails = session.getSpeakers();
		for (int i = 0; i < speakerEmails.length; i++) {
			Speaker speaker = seProvider.getByEmail(speakerEmails[i]);
			speakers.add(speaker);
		}
		
	}
	
	
	class SpeakerLoaderTask extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... params) {
		loadSpeakers();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(!speakers.isEmpty())
			for(Speaker sp:speakers){
				lyt_detail.removeAllViews();
			lyt_detail.addView(new SpeakerBadge(SessionDetailFragment.this.getActivity(),sp));
			}
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
		
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

	}

	public void updateSessionData(Session session) {
		this.session=session;
		tv_title.setText(session.getTitle());
		tv_desc.setText(session.getDescription());
		tv_date_room.setText(getString(R.string.from_) + " "
				+ session.getStart() + getString(R.string._to_)
				+ session.getEnd() + " in room " + session.getSalle());
		tv_sep_speaker.setText(formatSeparator(getResources().getString(
				R.string.spakers).toUpperCase()));
		tv_sep_desc.setText(formatSeparator(getResources().getString(
				R.string.desc)));
		
	new SpeakerLoaderTask().execute();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.menu_detail_session, menu);
	}

	SpannableString formatSeparator(String sep) {
		SpannableString content = new SpannableString(sep);
		content.setSpan(new UnderlineSpan(), 0, sep.length(), 0);
		return content;
	}

	String format(Date date) {
		return DateFormat.format("dd/mm/yyyy", date.getTime()).toString();
	}

	@Override
	public void onRESTResult(int code, String result) {

	}

	@Override
	public void onDestroy() {
		super.onDestroy();

	}

}
