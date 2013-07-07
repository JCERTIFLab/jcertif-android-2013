package com.jcertif.android;

import android.app.Application;
import android.content.res.Configuration;

public class JcertifApplication extends Application{

	public static final String BASE_URL="http://jcertif-backend.roddet.cloudbees.net";
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		super.onLowMemory();
	}

	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		super.onTerminate();
	}

	
	
}
