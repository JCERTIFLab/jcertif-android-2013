package com.jcertif.android.dao;

import android.content.Context;

import com.jcertif.android.model.User;

public class UserProvider extends JCertifDb4oHelper<User>{

	public UserProvider(Context ctx) {
		super(ctx);
	}

}
