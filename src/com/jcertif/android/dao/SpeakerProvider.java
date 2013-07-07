package com.jcertif.android.dao;

import com.jcertif.android.model.Session;
import com.jcertif.android.model.Speaker;

import android.content.Context;

public class SpeakerProvider extends JCertifDb4oHelper<Speaker> {
	
	public SpeakerProvider(Context ctx) {
		super(ctx);
	}
}
