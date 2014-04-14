/*
 * Copyright 2013 JCertifLab.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jcertif.android.fragments;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.text.Html;
import android.text.SpannableString;
import android.text.format.DateFormat;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.google.gson.Gson;
import com.jcertif.android.R;
import com.jcertif.android.compound.SpeakerBadge;
import com.jcertif.android.dao.SessionProvider;
import com.jcertif.android.dao.SpeakerProvider;
import com.jcertif.android.model.Session;
import com.jcertif.android.model.Speaker;
import com.jcertif.android.service.RESTService;

public class SessionDetailFragment extends RESTResponderFragment {

	private Session session;
	TextView tv_title, tv_desc, tv_date_room, tv_sep_desc, tv_sep_speaker;
    private List<Speaker> speakers= new ArrayList<Speaker>();
	
	private LinearLayout lyt_detail;
	
	public SessionDetailFragment() {
		super();
		setRetainInstance(true);
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		 
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
			lyt_detail.addView(new SpeakerBadge(SessionDetailFragment.this.getSherlockActivity(),
					SessionDetailFragment.this,sp));
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
		tv_desc.setText(Html.fromHtml(session.getDescription()));
		tv_date_room.setText(getString(R.string.from_) + " "
				+ session.getStart().toGMTString() + getString(R.string._to_)
				+ session.getEnd().toGMTString() + ". "+getString(R.string.room) + session.getSalle());
		tv_sep_speaker.setText(getResources().getString(
				R.string.spakers).toUpperCase());
		tv_sep_desc.setText(getResources().getString(
				R.string.desc));
		
	new SpeakerLoaderTask().execute();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.context_menu_session, menu);
	}



	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_share:
			shareSessionItem();
			break;
		case R.id.menu_add_to_schedule:
			addSessionItemToSchedule();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void addSessionItemToSchedule() {
		
		if (android.os.Build.VERSION.SDK_INT >= 14){
		Intent intent = new Intent(Intent.ACTION_INSERT);
		intent.setType("vnd.android.cursor.item/event");
		intent.putExtra(Events.TITLE, session.getTitle());
		intent.putExtra(Events.EVENT_LOCATION,"Room"+ session.getSalle());
		intent.putExtra(Events.DESCRIPTION, session.getDescription());

		
		Date evStartDate= session.getStart();
		Date evEndDate= session.getStart();
	
		// Setting dates
		GregorianCalendar startcalDate = new GregorianCalendar();
		startcalDate.setTime(evStartDate);
		
		// Setting dates
		GregorianCalendar endCalDate = new GregorianCalendar();
	    endCalDate.setTime(evEndDate);
		
		intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,startcalDate.getTimeInMillis());
		intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,endCalDate.getTimeInMillis());
		// Make it a full day event
		intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);
		// Make it a recurring Event
	//	intent.putExtra(Events.RRULE, "WKST=SU");
		// Making it private and shown as busy
		intent.putExtra(Events.ACCESS_LEVEL, Events.ACCESS_PRIVATE);
		intent.putExtra(Events.AVAILABILITY, Events.AVAILABILITY_BUSY); 
		//intent.putExtra(Events.DISPLAY_COLOR, Events.EVENT_COLOR); 
		startActivity(intent);
		}else{
			Toast.makeText(this.getSherlockActivity(), 
					"Not supported for your device :(", Toast.LENGTH_SHORT).show();
		} 
	}

	private void shareSessionItem() {

		Speaker sp = new SpeakerProvider(this.getSherlockActivity())
				.getByEmail(session.getSpeakers()[0]);
		Intent intent = new Intent(android.content.Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

		intent.putExtra(Intent.EXTRA_SUBJECT, "JCertif 2013:"+ session.getTitle());
		intent.putExtra(
				Intent.EXTRA_TEXT,
				"Checking out this  #Jcertif2013 session : "
						+ session.getTitle() + " by "
						+ sp.getFirstname() + " " + sp.getLastname());

		startActivity(intent);
	}
	String format(Date date) {
		return DateFormat.format("dd/mm/yyyy", date.getTime()).toString();
	}

	@Override
	public void onRESTResult(int code, Bundle resultData) {
		String result=	resultData.getString(RESTService.REST_RESULT);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

	}

}
