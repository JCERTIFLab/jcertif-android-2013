package com.jcertif.android.compound;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jcertif.android.R;
import com.jcertif.android.fragments.SpeakerDetailFragmentActivity;
import com.jcertif.android.model.Speaker;
import com.squareup.picasso.Picasso;

/**
 * 
 * @author Patrick Bashizi
 * 
 */
public class SpeakerBadge extends LinearLayout implements
		android.view.View.OnClickListener {

	private ImageView pic;
	private TextView tv_name;
	private TextView tv_company;
	private TextView tv_country;
	private Speaker speaker;
	private Fragment parent;
	private Activity activity;

	public SpeakerBadge(Context context) {
		super(context);
		String infService = Context.LAYOUT_INFLATER_SERVICE;
		LayoutInflater li;
		li = (LayoutInflater) getContext().getSystemService(infService);
		li.inflate(R.layout.item_speaker, this, true);

		tv_name = (TextView) findViewById(R.id.tv_speaker_name);
		tv_company = (TextView) findViewById(R.id.tv_company);
		tv_country = (TextView) findViewById(R.id.tv_city_country);
		pic = (ImageView) findViewById(R.id.logo);

		pic.setOnClickListener(this);
		tv_country.setOnClickListener(this);
		tv_country.setOnClickListener(this);
		tv_company.setOnClickListener(this);
		tv_name.setOnClickListener(this);
	}

	/**
	 * 
	 * @param context
	 * @param speaker
	 */
	public SpeakerBadge(Activity activity, Fragment parent, Speaker speaker) {
		this(activity.getBaseContext());
		this.speaker = speaker;
		this.parent = parent;
		this.activity = activity;
		initData();
	}

	/**
 * 
 */
	private void initData() {
		tv_name.setText(speaker.getFirstname() + " " + speaker.getLastname());
		tv_company.setText(speaker.getCompany());
		tv_country.setText(speaker.getCity() + ", " + speaker.getCountry());
		Picasso.with(getContext()).load(speaker.getPhoto()).resize(200, 200)
				.into(pic);
	}

	@Override
	public void onClick(View v) {

		Intent intent = new Intent(this.activity,
				SpeakerDetailFragmentActivity.class);
       // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		String speakerJson = new Gson().toJson(speaker);
		intent.putExtra("speaker", speakerJson);

		activity.startActivity(intent);
		activity.overridePendingTransition(R.anim.slide_up_right, R.anim.slide_up_left);
	}

}
