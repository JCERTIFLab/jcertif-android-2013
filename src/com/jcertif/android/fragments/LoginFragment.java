package com.jcertif.android.fragments;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.db4o.config.TVector;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.plus.PlusClient;
import com.google.android.gms.plus.PlusClient.OnAccessRevokedListener;
import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.plus.model.people.PersonBuffer;
import com.jcertif.android.R;

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

		/*mPlusClient = new PlusClient.Builder(this.getActivity()
				.getApplicationContext(), this, this)
				.setVisibleActivities("http://schemas.google.com/AddActivity",
						"http://schemas.google.com/BuyActivity")
				.setScopes(Scopes.PLUS_LOGIN,Scopes.PLUS_PROFILE).build();*/
		
		mPlusClient = new PlusClient.Builder(this.getActivity()
				.getApplicationContext(), this, this)
				.build();

		mConnectionProgressDialog = new ProgressDialog(this.getActivity());
		mConnectionProgressDialog.setTitle("Login");
		// mConnectionProgressDialog.setCancelable(false);
		mConnectionProgressDialog.setMessage("Google Login...");
	}

	@Override
	public void onRESTResult(int code, String result) {

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
		}
		else{
			new ConnectAsyncTask().execute();
		}
	}
	
	
	private class ConnectAsyncTask extends AsyncTask<String, Void, String> {

	    private static final int MY_ACTIVITYS_AUTH_REQUEST_CODE = 1;
		String sAccessToken = null;

	    @Override
	    protected void onPostExecute(String info) {

	        Log.d("JSONData = ", "" + info);
	        //This contains all the data you wanted.
	    }

	    @Override
	    protected String doInBackground(String... params) {

	        HttpURLConnection urlConnection = null;
	        try {
	            URL url = new URL(
	                    "https://www.googleapis.com/oauth2/v1/userinfo");
	            sAccessToken = GoogleAuthUtil
	                    .getToken(
	                    		  LoginFragment.this.getActivity(),
	                            mPlusClient.getAccountName() + "",
	                            "oauth2:"
	                                    + Scopes.PLUS_PROFILE
	                                    + " https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo.email");

	            //This is probably not the right way to do.
	            //What I am doing here is, get an AccessToken, invalidate it and get a new
	            //AccessToken again. Because I couldn't find a way to check whether the 
	            //AccessToken is expired or not.

	            GoogleAuthUtil.invalidateToken(LoginFragment.this.getActivity(), sAccessToken);

	            sAccessToken = GoogleAuthUtil
	                    .getToken(
	                           LoginFragment.this.getActivity(),
	                            mPlusClient.getAccountName() + "",
	                            "oauth2:"
	                                    + Scopes.PLUS_PROFILE
	                                    + " https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo.email");

	            urlConnection = (HttpURLConnection) url.openConnection();
	            urlConnection.setRequestProperty("Authorization", "Bearer "
	                    + sAccessToken);

	            BufferedReader r = new BufferedReader(new InputStreamReader(
	                    urlConnection.getInputStream(), "UTF-8"));
	            StringBuilder total = new StringBuilder();
	            String line=null;
	            while ((line = r.readLine()) != null) {
	                total.append(line);
	            }
	            line = total.toString();
	            if (!TextUtils.isEmpty(line)) {
	                return line;
	            } else {
	                return null;
	            }
	        } catch (UserRecoverableAuthException userAuthEx) {
	            // Start the user recoverable action using the intent returned
	            // by getIntent()

	            userAuthEx.printStackTrace();
	            LoginFragment.this.getActivity().startActivityForResult(
	                    userAuthEx.getIntent(), MY_ACTIVITYS_AUTH_REQUEST_CODE);
	            return null;
	        } catch (FileNotFoundException e) {
	            //You get this exception when the AccessToken is expired.
	            //I don't know how its a FileNotFoundException
	            //Thats why instead of relying on this, I did the above to get
	            //new AccessToken
	        } catch (Exception e) {
	            e.printStackTrace();
	            return null;
	        } finally {
	            if (urlConnection != null) {
	                urlConnection.disconnect();
	            }
	        }
	       return null;
	    }

	    @Override
	    protected void onPreExecute() {
	    }
	}
}
