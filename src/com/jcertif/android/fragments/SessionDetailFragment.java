package com.jcertif.android.fragments;

import java.util.Calendar;
import java.util.Date;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.format.DateFormat;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jcertif.android.R;
import com.jcertif.android.model.Session;

public class SessionDetailFragment extends RESTResponderFragment {

	private Session session;
	TextView tv_title, tv_desc, tv_date_room, tv_sep_desc, tv_sep_speaker;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// setRetainInstance(true);
		View rootView = inflater.inflate(R.layout.fragment_detail_session,
				container, false);
		getActivity().setTitle(R.string.app_name);

		tv_title = (TextView) rootView
				.findViewById(R.id.tv_title_session_detail);
		tv_desc = (TextView) rootView.findViewById(R.id.tv_description);
		tv_sep_desc = (TextView) rootView.findViewById(R.id.tv_separator_desc);
		tv_date_room = (TextView) rootView.findViewById(R.id.tv_date_room);
		tv_sep_speaker = (TextView) rootView
				.findViewById(R.id.tv_separator_speaker);
		
		Object sessionjson=null;
		if(getArguments()!=null && !getArguments().isEmpty()){
		 sessionjson = getArguments().get("session");
		}
		if (sessionjson != null) {
			session = (Session) new Gson().fromJson(sessionjson.toString(), Session.class);
			updateSessionData(session);
		}
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

	}

	public void updateSessionData(Session session) {
		tv_title.setText(session.getTitle());
		tv_desc.setText(session.getDescription());
		tv_date_room.setText(getString(R.string.from_ )+ " " + session.getStart()
				+ getString(R.string._to_) + session.getEnd() + " in room "
				+ session.getSalle());
		tv_sep_speaker.setText(formatSeparator(getResources().getString(
				R.string.spakers).toUpperCase()));
		tv_sep_desc.setText(formatSeparator(getResources().getString(
				R.string.desc)));
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
		// TODO Auto-generated method stub
		super.onDestroy();

	}

}
