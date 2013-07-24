package com.jcertif.android.compound;

import com.jcertif.android.R;
import com.jcertif.android.model.Speaker;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 
 * @author Patrick Bashizi
 *
 */
public class SpeakerBadge extends LinearLayout{

	private ImageView pic;
	private TextView tv_name;
	private TextView tv_company;
	private TextView tv_country;
    private Speaker speaker;

	
	public SpeakerBadge(Context context) {
		super(context);
		String infService = Context.LAYOUT_INFLATER_SERVICE;
		LayoutInflater li;
		li = (LayoutInflater)getContext().getSystemService(infService);
		li.inflate(R.layout.item_speaker, this, true);
		
		tv_name = (TextView)findViewById(R.id.tv_speaker_name);
		tv_company= (TextView) findViewById(R.id.tv_company);
		tv_country= (TextView) findViewById(R.id.tv_city_country);
		pic = (ImageView)findViewById(R.id.avatar);	
	}
	
	

	public SpeakerBadge(Context context, Speaker speaker) {
		super(context);
		this.speaker = speaker;
		initData();
	}



	private void initData() {	
		tv_name.setText(speaker.getFirstname()+ " "+speaker.getLastname());
		tv_company.setText(speaker.getCompany());
		tv_country.setText(	speaker.getCity()+", "+speaker.getCountry());
		Picasso.with(getContext()).load(speaker.getPhoto()).into(pic);	
	}

}
