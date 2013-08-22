package com.jcertif.android.compound;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jcertif.android.R;
import com.jcertif.android.model.Speaker;
import com.squareup.picasso.Picasso;

/**
 * 
 * @author Patrick Bashizi
 * 
 */
public class SpeakerBadge extends LinearLayout implements android.view.View.OnClickListener{

	private ImageView pic;
	private TextView tv_name;
	private TextView tv_company;
	private TextView tv_country;
	private Speaker speaker;
	private Fragment parent;
	private Context context;

	public SpeakerBadge(Context context) {
		super(context);
		String infService = Context.LAYOUT_INFLATER_SERVICE;
		LayoutInflater li;
		li = (LayoutInflater) getContext().getSystemService(infService);
		li.inflate(R.layout.item_speaker, this, true);
        this.context=context;
		tv_name = (TextView) findViewById(R.id.tv_speaker_name);
		tv_company = (TextView) findViewById(R.id.tv_company);
		tv_country = (TextView) findViewById(R.id.tv_city_country);
		pic = (ImageView) findViewById(R.id.logo);
		
		pic.setOnClickListener(this);
	}

	/**
	 * 
	 * @param context
	 * @param speaker
	 */
	public SpeakerBadge(Context context,Fragment parent, Speaker speaker) {
		this(context);
		this.speaker = speaker;
		this.parent=parent;
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
		
		Toast.makeText(context, speaker.getLastname(), Toast.LENGTH_SHORT).show();
		
	}

}
