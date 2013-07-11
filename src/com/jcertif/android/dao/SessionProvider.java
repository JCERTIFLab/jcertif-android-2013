package com.jcertif.android.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.db4o.query.Predicate;
import com.jcertif.android.model.Category;
import com.jcertif.android.model.Session;

import android.content.Context;

public class SessionProvider extends JCertifDb4oHelper<Session> {

	public SessionProvider(Context ctx) {
		super(ctx);
	}

	public List<Session> getSessionsByCategory(final String categorie) {
		List<Session> list = new ArrayList<Session>();
		list = db().query(new Predicate<Session>() {
			@Override
			public boolean match(Session test) {
				return Arrays.asList(test.getCategory()).contains(categorie);
			}
		});
		return list;

	}
}