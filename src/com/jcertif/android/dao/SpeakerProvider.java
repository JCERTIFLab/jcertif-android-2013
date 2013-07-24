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

	@SuppressWarnings("serial")
	public Speaker getByEmail(final String email) {
		List<Speaker> list = new ArrayList<Speaker>();
		list = db().query(new Predicate<Speaker>() {

			@Override
			public boolean match(Speaker test) {
				return test.getEmail().equals(email);
			}

		});
		if (!list.isEmpty()) {
		return	list.get(0);
		} 
			return null;
	}

	
}
