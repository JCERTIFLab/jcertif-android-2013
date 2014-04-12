package com.jcertif.android.compound;

import com.jcertif.android.model.Sponsor;

import android.app.Activity;
import android.content.Context;
import android.widget.LinearLayout;

public class SponsorBadge extends LinearLayout{

	private Sponsor sponsor;
	private Activity activity;
	
	public SponsorBadge(Context context) {
		super(context);		
	}

	public SponsorBadge(Activity activity,Sponsor sponsor) {
		this(activity);
		this.sponsor = sponsor;
		this.activity = activity;
	}


}
