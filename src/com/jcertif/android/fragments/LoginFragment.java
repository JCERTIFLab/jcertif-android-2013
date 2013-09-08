package com.jcertif.android.fragments;

import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.plus.PlusClient;
import com.google.android.gms.plus.PlusClient.OnAccessRevokedListener;
import com.google.android.gms.plus.model.people.Person;
import com.google.gson.Gson;
import com.jcertif.android.BuildConfig;
import com.jcertif.android.Config;
import com.jcertif.android.JcertifApplication;
import com.jcertif.android.R;
import com.jcertif.android.fragments.RegistrationFormFragment.OnUserDialogReturns;
import com.jcertif.android.model.Participant;
import com.jcertif.android.service.RESTService;

/**
 * 
 * @author bashizip
 * 
 */
public class LoginFragment extends RESTResponderFragment implements ConnectionCallbacks, OnConnectionFailedListener,
		OnAccessRevokedListener, OnClickListener, PlusClient.OnPersonLoadedListener, OnUserDialogReturns {

	private static final int REQUEST_CODE_RESOLVE_ERR = 9000;
	private static final String TAG = "Login Fragment";
	private static final String REGISTER_URI = JcertifApplication.BASE_URL + "/participant/register";

	PlusClient mPlusClient;
	private ConnectionResult mConnectionResult;
	private ProgressDialog mConnectionProgressDialog;
	private SignInButton mPlusOneButton;

	private Participant user;
	OnSignedInListener mSignedCallback;

	// TWITTER
	private static twitter4j.Twitter twitter;
	private static RequestToken requestToken;

	// Shared Preferences
	private static SharedPreferences mSharedPreferences;

	// permet de savoir a qui le FragmentDialog doit envoyer le mail
	public static final int DIALOG_FRAGMENT = 100;

	// Bouton de connexion à twitter
	private Button twitterSigninButton;

	// Container Activity must implement this interface
	public interface OnSignedInListener {
		public void onSignedIn(Participant user, ProgressDialog dlg);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		setRetainInstance(true);
		View rootView = inflater.inflate(R.layout.fragment_login, container, false);
		getActivity().setTitle(getString(R.string.login));
		mPlusOneButton = (SignInButton) rootView.findViewById(R.id.sign_in_button);
		twitterSigninButton = (Button) rootView.findViewById(R.id.sign_in_twitter);
		mPlusOneButton.setOnClickListener(this);
		twitterSigninButton.setOnClickListener(this);

		mSharedPreferences = getActivity().getSharedPreferences(Config.PREFERENCE_JCERTIF_TWITTER, 0);

		// recuperation des cles oauth via les serveurs twitter
		Bundle bundle = getArguments();
		if (bundle != null && bundle.containsKey("params")) {
			TwitterConnect connect = new TwitterConnect();
			connect.execute(bundle.getString("params"));
		}

		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		getSherlockActivity().getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

		mPlusClient = new PlusClient.Builder(this.getActivity().getApplicationContext(), this, this)
				.setVisibleActivities("http://schemas.google.com/AddActivity", "http://schemas.google.com/BuyActivity")
				.setScopes(Scopes.PLUS_LOGIN, Scopes.PLUS_PROFILE).build();

		mConnectionProgressDialog = new ProgressDialog(this.getActivity());
		mConnectionProgressDialog.setTitle("Login");
		// mConnectionProgressDialog.setCancelable(false);
		mConnectionProgressDialog.setMessage(getSherlockActivity().getString(R.string.google_login_));
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mSignedCallback = (OnSignedInListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement OnSignedInListener");
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

		Toast.makeText(this.getActivity(), "Access revoked!", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onConnectionFailed(ConnectionResult res) {
		if (mConnectionProgressDialog.isShowing()) {

			if (res.hasResolution()) {
				try {
					res.startResolutionForResult(this.getActivity(), REQUEST_CODE_RESOLVE_ERR);
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
	public void onActivityResult(int requestCode, int responseCode, Intent intent) {
		if (requestCode == REQUEST_CODE_RESOLVE_ERR && responseCode == Activity.RESULT_OK) {
			mConnectionResult = null;
			mPlusClient.connect();
		}
		if (requestCode == DIALOG_FRAGMENT && responseCode == Activity.RESULT_OK) {
			LoginFragment.this.user.setEmail(intent.getExtras().getString("email"));
			registerUser();
		}

	}

	@Override
	public void onConnected(Bundle connectionHint) {
		/*
		 * if (mConnectionProgressDialog.isShowing()) {
		 * mConnectionProgressDialog.dismiss(); }
		 */

		String accountName = mPlusClient.getAccountName();
		mPlusClient.loadPerson(this, "me");
		Log.d(TAG, "Display Name: " + accountName);
		Toast.makeText(this.getActivity(), "Connected:" + accountName, Toast.LENGTH_LONG).show();

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
					mConnectionResult.startResolutionForResult(this.getActivity(), REQUEST_CODE_RESOLVE_ERR);

				} catch (SendIntentException e) {
					// Try connecting again.
					mConnectionResult = null;
					mPlusClient.connect();
				}
			}
		} else if (v.getId() == R.id.sign_in_twitter) {
			// Connexion avec twitter ici
			Thread th = new Thread(new Runnable() {
				public void run() {
					loginToTwitter();
				}

			});

			th.start();
		}

	}

	private void showRegisterDialog() {
		FragmentManager fm = getSherlockActivity().getSupportFragmentManager();
		RegistrationFormFragment registerDialog = new RegistrationFormFragment();
		registerDialog.register(this);
		registerDialog.show(fm, "fragment_register");
	}

	@Override
	public void onPersonLoaded(ConnectionResult status, Person person) {
		if (status.getErrorCode() == ConnectionResult.SUCCESS) {

			user = new Participant();
			String fullName = person.getDisplayName();
			user.setFirstname(fullName.substring(0, fullName.indexOf(' ')));
			user.setLastname(fullName.substring(fullName.indexOf(' ')));
			user.setEmail(mPlusClient.getAccountName());
			user.setBiography(person.getTagline());
			user.setCity((person.getCurrentLocation() == null) ? "N/A" : person.getCurrentLocation());
			user.setCountry((person.getCurrentLocation() == null) ? "N/A" : person.getCurrentLocation());
			user.setCompany(person.getOrganizations().get(0).getName());
			user.setPhoto(getBestPictureSize(person.getImage().getUrl()));
			user.setWebsite(person.getUrl());
			user.setPassword(getFakePassword());
			user.setPhone("N/A");

			registerUser();

		} else {
			// TODO handle this
		}
	}

	/**
	 * G+ return pic size of 50, we need a bigger size, say 200 Url are like :
	 * https://lh6.googleusercontent.com/-MgVQQ8F_Buk/AAAAAAAAAAI/AAAAAAAACSQ/8
	 * mfy3fB3xcs/photo.jpg?sz=50
	 * 
	 * @param url
	 * @return
	 */
	private String getBestPictureSize(String url) {
		String url_param = url.substring(0, url.indexOf('=') + 1);
		String best = url_param + "200";
		return best;
	}

	/**
	 * The backend need a not null password
	 * 
	 * @return
	 */
	private String getFakePassword() {
		return "vfdvbdpfjvjvperj5455vre";
	}

	private void registerUser() {
		Activity activity = getActivity();
		Intent intent = new Intent(activity, RESTService.class);
		intent.setData(Uri.parse(REGISTER_URI));

		// Here we are going to place our REST call parameters.
		Bundle params = new Bundle();
		String payload = new Gson().toJson(user, Participant.class);
		params.putString(RESTService.KEY_JSON_PLAYLOAD, payload);
		intent.putExtra(RESTService.EXTRA_HTTP_VERB, RESTService.POST);
		intent.putExtra(RESTService.EXTRA_PARAMS, params);
		intent.putExtra(RESTService.EXTRA_RESULT_RECEIVER, getResultReceiver());

		activity.startService(intent);
	}

	@Override
	public void onRESTResult(int code, Bundle resultData) {
		Activity activity = getActivity();
		// Here is where we handle our REST response.
		// Check to see if we got an HTTP 200 code and have some data.
		String result = resultData.getString(RESTService.REST_RESULT);
		if (code == 200 && result != null) {

			mSignedCallback.onSignedIn(user, mConnectionProgressDialog);

			Log.d(TAG, result);
			Toast.makeText(activity, "Successfully Registered !", Toast.LENGTH_SHORT).show();
		} else if (code == 409 && result != null) {
			mSignedCallback.onSignedIn(user, mConnectionProgressDialog);
			Toast.makeText(activity, "Welcome back, " + user.getFirstname(), Toast.LENGTH_SHORT).show();
		} else

		if (activity != null) {
			Log.d(TAG, result);
			Toast.makeText(activity, "Failed to Register.Try again.", Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	public void onUserEdited(Participant p) {
		this.user = p;
		registerUser();
	}

	private void loginToTwitter() {
		// Check if already logged in

		ConfigurationBuilder builder = new ConfigurationBuilder();
		builder.setOAuthConsumerKey(Config.TWITTER_CONSUMER_KEY);
		builder.setOAuthConsumerSecret(Config.TWITTER_CONSUMER_SECRET);
		Configuration configuration = builder.build();

		TwitterFactory factory = new TwitterFactory(configuration);
		twitter = factory.getInstance();

		try {
			requestToken = twitter.getOAuthRequestToken(Config.TWITTER_CALLBACK_URL);
			this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(requestToken.getAuthenticationURL())));
		} catch (TwitterException e) {
			e.printStackTrace();
		}
	}

	private class TwitterConnect extends AsyncTask<String, Void, Participant> {

		@Override
		protected Participant doInBackground(String... params) {
			Participant participant = null;
			try {
				final AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, params[0]);
				Editor e = mSharedPreferences.edit();

				e.putString(Config.PREF_KEY_OAUTH_TOKEN, accessToken.getToken());
				e.putString(Config.PREF_KEY_OAUTH_SECRET, accessToken.getTokenSecret());
				e.putBoolean(Config.PREF_KEY_TWITTER_LOGIN, true);
				e.commit(); // save changes

				Log.d(Config.PREF_KEY_OAUTH_TOKEN, accessToken.getToken());
				Log.d(Config.PREF_KEY_OAUTH_SECRET, accessToken.getTokenSecret());

				long userID = accessToken.getUserId();
				// Twitter user
				User user = twitter.showUser(userID);
				Log.d(TAG, user.toString());
				participant = twitterUserToParticipant(user);

			} catch (TwitterException e) {
				e.printStackTrace();
			}
			return participant;
		}

		@Override
		protected void onPostExecute(Participant participant) {
			if (participant != null) {
				// TODO Aller au profil du user
				LoginFragment.this.user = participant;
				TwitterUserMailDialoFragment fragment = new TwitterUserMailDialoFragment();
				fragment.setTargetFragment(LoginFragment.this, DIALOG_FRAGMENT);
				fragment.show(getActivity().getSupportFragmentManager(), "TwitterUserMailDialoFragment");
			}
		}
	}

	// permet le passage d'un user twitter à un participant jcertif
	public Participant twitterUserToParticipant(User user) {
		Participant participant = new Participant();

		String fullName = user.getName();
		String location = user.getLocation();
		participant.setFirstname(fullName.substring(0, fullName.indexOf(' ')));
		participant.setLastname(fullName.substring(fullName.indexOf(' ')));
		participant.setBiography(user.getDescription());
		// participant.setCity(location.substring(0, location.indexOf(", ")));
		// participant.setCountry(location.substring(location.indexOf(", ")));
		participant.setPhone(getBestPictureSize(user.getProfileImageURL()));
		participant.setPhone("N/A");
		participant.setPassword(getFakePassword());

		// TODO certaines info ne sont pas fourni par twitter
		// participant.setCompany();
		// participant.setWebsite(user.get);

		return participant;
	}
}
