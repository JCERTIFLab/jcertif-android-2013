package com.jcertif.android;

import android.app.Activity;
import android.app.Application;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @author bashizip
 * @author Komi Serge Innocent <komi.innocent@gmail.com>
 */
public class JcertifApplication extends Application{

	//public static final String BASE_URL="http://jcertif-backend.msomda.cloudbees.net";
	public static final String BASE_URL="http://jcertif.backend.vm-host.net";

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

    public Boolean isApplicationConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }


	
}
