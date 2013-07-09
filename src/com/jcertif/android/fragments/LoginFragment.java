package com.jcertif.android.fragments;

import android.app.ProgressDialog;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.plus.PlusClient;
import com.google.android.gms.plus.PlusClient.OnAccessRevokedListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.jcertif.android.R;

public class LoginFragment extends RESTResponderFragment implements
		ConnectionCallbacks, OnConnectionFailedListener,
		OnAccessRevokedListener, OnClickListener {
	
	private static final int REQUEST_CODE_RESOLVE_ERR = 0;
	private static final String TAG = "Login Fragment";
	
	PlusClient mPlusClient;
	private ConnectionResult mConnectionResult;
	private ProgressDialog mConnectionProgressDialog;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setRetainInstance(true);
		View rootView = inflater.inflate(R.layout.fragment_login, container,
				false);
		getActivity().setTitle(getString(R.string.login));
	
		rootView.findViewById(R.id.sign_in_button).setOnClickListener(this);
			
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		mPlusClient = new PlusClient.Builder(this.getActivity()
				.getApplicationContext(), this, this).setVisibleActivities(
				"http://schemas.google.com/AddActivity",
				"http://schemas.google.com/BuyActivity").build();
		
		mConnectionProgressDialog=new ProgressDialog(this.getActivity());
		mConnectionProgressDialog.setTitle("Login");
	//	mConnectionProgressDialog.setCancelable(false);
		mConnectionProgressDialog.setMessage("Google Login...");
	}

	@Override
	public void onRESTResult(int code, String result) {

	}

	@Override
	public void onAccessRevoked(ConnectionResult res) {
		Log.e(TAG, res.toString()+"");
		Toast.makeText(this.getActivity(), "Access revoked!",
				Toast.LENGTH_LONG).show();
	}

	@Override
	public void onConnectionFailed(ConnectionResult res) {
		mConnectionProgressDialog.dismiss();
		Log.e(TAG, res.toString()+"");
		Toast.makeText(this.getActivity(), "Connection Failed!",
				Toast.LENGTH_LONG).show();
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		mConnectionProgressDialog.dismiss();
		Toast.makeText(this.getActivity(), "User is connected!",
				Toast.LENGTH_LONG).show();
		
	}

	@Override
	public void onDisconnected() {
		
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.sign_in_button && !mPlusClient.isConnected()) {
			if (mConnectionResult == null) {
				mConnectionProgressDialog.show();
			} else {
				try {
					mConnectionResult.startResolutionForResult(this.getActivity(), REQUEST_CODE_RESOLVE_ERR);
				} catch (SendIntentException e) {
					// Try connecting again.
					mConnectionResult = null;
					mPlusClient.connect();
				}
			}
		}

	}

}
