package com.jcertif.android.dao;

import java.util.List;

import android.content.Context;

import com.jcertif.android.model.Session;
import com.jcertif.android.model.Speaker;

public class SpeakerProvider extends JCertifDb4oHelper<Speaker> {

	public SpeakerProvider(Context ctx) {
		super(ctx);
	}

	
	public Speaker getByEmail(final String email) {
		List<Speaker> list =getAll(Speaker.class);
		if (!list.isEmpty())
		for(Speaker s: list){
			if(s.getEmail().equals(email)){
				return s;
			}
		}
			return new Speaker();
	}




	
}
