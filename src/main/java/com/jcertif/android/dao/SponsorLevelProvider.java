package com.jcertif.android.dao;

import android.content.Context;

import com.jcertif.android.model.Sponsor;
import com.jcertif.android.model.SponsorLevel;

public class SponsorLevelProvider extends JCertifDb4oHelper<SponsorLevel> {

	public SponsorLevelProvider(Context ctx) {
		super(ctx);
	}

}
