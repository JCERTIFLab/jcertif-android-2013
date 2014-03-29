package com.jcertif.android.dao;

import android.content.Context;

import com.jcertif.android.model.Participant;;

public class UserProvider extends JCertifDb4oHelper<Participant>{

	public UserProvider(Context ctx) {
		super(ctx);
	}

}
