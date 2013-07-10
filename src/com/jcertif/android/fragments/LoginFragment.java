package com.jcertif.android.fragments;

import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.plus.PlusClient;
import com.google.android.gms.plus.PlusClient.OnAccessRevokedListener;
import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.plus.model.people.Person.Emails;
import com.jcertif.android.R;
import com.jcertif.android.model.Participant;


public class LoginFragment extends RESTResponderFragment implements
		ConnectionCallbacks, OnConnectionFailedListener,
		OnAccessRevokedListener, OnClickListener,
		PlusClient.OnPersonLoadedListener {

	private static final int REQUEST_CODE_RESOLVE_ERR = 9000;
	private static final String TAG = "Login Fragment";

	PlusClient mPlusClient;
	private ConnectionResult mConnectionResult;
	private ProgressDialog mConnectionProgressDialog;
	private SignInButton mPlusOneButton;
	private EditText et_email;
	private EditText et_password;
	private Participant user;

	
	 OnSignedInListener mSignedCallback;

	    // Container Activity must implement this interface
	    public interface OnSignedInListener {
	        public void onSignedIn(Participant user);
	    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setRetainInstance(true);
		View rootView = inflater.inflate(R.layout.fragment_login, container,
				false);
		getActivity().setTitle(getString(R.string.login));
		mPlusOneButton = (SignInButton) rootView
				.findViewById(R.id.sign_in_button);

		mPlusOneButton.setOnClickListener(this);
		et_email = (EditText) rootView.findViewById(R.id.et_email);
		et_password = (EditText) rootView.findViewById(R.id.et_password);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		mPlusClient = new PlusClient.Builder(this.getActivity()
				.getApplicationContext(), this, this)
				.setVisibleActivities("http://schemas.google.com/AddActivity",
						"http://schemas.google.com/BuyActivity")
				.setScopes(Scopes.PLUS_LOGIN,Scopes.PLUS_PROFILE).build();

		mConnectionProgressDialog = new ProgressDialog(this.getActivity());
		mConnectionProgressDialog.setTitle("Login");
		// mConnectionProgressDialog.setCancelable(false);
		mConnectionProgressDialog.setMessage("Google Login...");
	}

	@Override
	public void onRESTResult(int code, String result) {

	}
	
	 @Override
	    public void onAttach(Activity activity) {
	        super.onAttach(activity);
	       try {
	        	mSignedCallback = (OnSignedInListener) activity;
	        } catch (ClassCastException e) {
	            throw new ClassCastException(activity.toString()
	                    + " must implement OnSignedInListener");
	        }
	    }
	

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (mPlusClient.isConnected())
			mPlusClient.disconnect();
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		if (mPlusClient.isConnected())
			mPlusClient.disconnect();
	}

	@Override
	public void onAccessRevoked(ConnectionResult res) {
		Log.e(TAG, res.toString() + "");

		Toast.makeText(this.getActivity(), "Access revoked!", Toast.LENGTH_LONG)
				.show();
	}

	@Override
	public void onConnectionFailed(ConnectionResult res) {
		if (mConnectionProgressDialog.isShowing()) {

			if (res.hasResolution()) {
				try {
					res.startResolutionForResult(this.getActivity(),
							REQUEST_CODE_RESOLVE_ERR);
				} catch (SendIntentException e) {
					mPlusClient.connect();
				}
			}
		}

		// Save the intent so that we can start an activity when the user clicks
		// the sign-in button.
		mConnectionResult = res;
	}

	@Override
	public void onActivityResult(int requestCode, int responseCode,
			Intent intent) {
		if (requestCode == REQUEST_CODE_RESOLVE_ERR
				&& responseCode == Activity.RESULT_OK) {
			mConnectionResult = null;
			mPlusClient.connect();
		}
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		mConnectionProgressDialog.dismiss();

		String accountName = mPlusClient.getAccountName();
		mPlusClient.loadPerson(this, "me");
		Log.d(TAG, "Display Name: " + accountName);
		Toast.makeText(this.getActivity(), "Conncted:" + accountName,
				Toast.LENGTH_LONG).show();
	}

	@Override
	public void onDisconnected() {

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.sign_in_button && !mPlusClient.isConnected()) {
			if (mConnectionResult == null) {
				mPlusClient.connect();
				mConnectionProgressDialog.show();
			} else {
				try {
					mConnectionResult.startResolutionForResult(
							this.getActivity(), REQUEST_CODE_RESOLVE_ERR);
					mConnectionProgressDialog.dismiss();
				} catch (SendIntentException e) {
					// Try connecting again.
					mConnectionResult = null;
					mPlusClient.connect();
				}
			}
		}

	}

	@Override
	public void onPersonLoaded(ConnectionResult status, Person person) {
		if (status.getErrorCode() == ConnectionResult.SUCCESS) {
			et_email.setText(person.getDisplayName());
			user= new Participant();
			user.setFirstname(person.getDisplayName());
			//user.setLastname(person.getName().getFamilyName());
			user.setEmail(mPlusClient.getAccountName());
			user.setBiography(person.getTagline());
			user.setCity(person.getCurrentLocation());
			user.setCountry(person.getCurrentLocation());
			user.setCompany(person.getOrganizations().get(0).getName());
			user.setPhone("");
			user.setPhoto(person.getImage().getUrl());
			user.setWebsite(person.getUrl());
			
			mSignedCallback.onSignedIn(user);
		}
		else{
			//TODO handle this
		}
	}
}
