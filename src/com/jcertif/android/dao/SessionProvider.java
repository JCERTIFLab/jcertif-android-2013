package com.jcertif.android.dao;

import com.jcertif.android.model.Session;

import android.content.Context;

public class SessionProvider extends JCertifDb4oHelper<Session> {

	private static SessionProvider instance = null;

	public SessionProvider(Context ctx) {
		super(ctx);
	}

	public static SessionProvider getInstance(Context ctx) {
		if (instance == null)
			instance = new SessionProvider(ctx);
		return instance;
	}

}
