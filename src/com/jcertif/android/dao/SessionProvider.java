package com.jcertif.android.dao;

import com.jcertif.android.model.Session;

import android.content.Context;

public class SessionProvider extends JCertifDb4oHelper<Session> {
	
	public SessionProvider(Context ctx) {
		super(ctx);
	}
}
