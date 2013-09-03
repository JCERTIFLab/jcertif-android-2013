package com.jcertif.android.fragments;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.jcertif.android.R;

/**
 * 
 * @author bashizip
 *
 */
public class ExternalLibsUsedFragment extends RESTResponderFragment {

	private TextView tv_ext;
	private static final String TAG = "About Fragment";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setRetainInstance(true);
		View rootView = inflater.inflate(R.layout.fragment_extt, container,
				false);
		getActivity().setTitle(R.string.about);
		tv_ext = (TextView) rootView
				.findViewById(R.id.tv_ext);
		

        InputStream is = null;
        try {
            is = getResources().getAssets().open("ext.html");
            tv_ext.setText(Html.fromHtml(inputStreamToString(is)));
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
		
		return rootView;
	}

	
	   public static String inputStreamToString(InputStream is) throws IOException {
	        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	        StringBuilder sb = new StringBuilder();
	        String line = null;

	        while ((line = reader.readLine()) != null) {
	            sb.append(line);
	        }

	        is.close();

	        return sb.toString();
	    }

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		getSherlockActivity().getSupportActionBar().setNavigationMode(
				ActionBar.NAVIGATION_MODE_STANDARD);

	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

	}

	@Override
	public void onRESTResult(int code, Bundle result) {
		// TODO Auto-generated method stub
		
	}

}
