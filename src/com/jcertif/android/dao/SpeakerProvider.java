package com.jcertif.android.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.db4o.query.Predicate;
import com.jcertif.android.model.Session;
import com.jcertif.android.model.Speaker;

import android.content.Context;

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
