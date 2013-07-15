package com.jcertif.android.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.google.gson.Gson;
import com.jcertif.android.JcertifApplication;
import com.jcertif.android.R;
import com.jcertif.android.model.Participant;
import com.jcertif.android.service.RESTService;

public class RegistrationFormFragment extends RESTResponderFragment implements	
		 OnClickListener {

	private EditText et_email,et_password,et_city, et_country, et_company,
		et_phone, et_website, et_lastname, et_firstname, et_biography;
	private Button bt_cancel, bt_register;
	private Participant user;
	private ProgressDialog ConProgressDialog;
	

	private static final int REQUEST_CODE_RESOLVE_ERR = 9000;
	private static final String TAG="Registration Fragment";
	private static final String REGISTER_URI=JcertifApplication.BASE_URL+"/particpant/register";
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setRetainInstance(true);
		View rootView = inflater.inflate(R.layout.fragment_registration, container,
				false);
		getActivity().setTitle(R.string.registration);
		
		et_email = (EditText) rootView.findViewById(R.id.r_email);
		et_password=(EditText) rootView.findViewById(R.id.r_password);
		et_lastname=(EditText) rootView.findViewById(R.id.r_lastname);
		et_firstname=(EditText) rootView.findViewById(R.id.r_firstname);
		et_city=(EditText) rootView.findViewById(R.id.r_city);
		et_website=(EditText) rootView.findViewById(R.id.r_website);
		et_country=(EditText) rootView.findViewById(R.id.r_country);
		et_company=(EditText) rootView.findViewById(R.id.r_company);
		et_phone=(EditText) rootView.findViewById(R.id.r_phone);
		et_biography=(EditText) rootView.findViewById(R.id.r_biography);
		
		bt_cancel.setOnClickListener(this);
		bt_register.setOnClickListener(this);
		// mettre la suite ici avec toutes les autres valeurs du formulaire
		
		return rootView;
	}
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getSherlockActivity().getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		
		ConProgressDialog = new ProgressDialog(this.getActivity());
		ConProgressDialog.setTitle("Register");
		// mConnectionProgressDialog.setCancelable(false);
		ConProgressDialog.setMessage("User registration ...");
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.bt_cancel){
			
		}
		else if(v.getId()==R.id.bt_register){
					
			onRegisterUser();	
			ConProgressDialog.show();
				
		}
		
	}
	
	@Override
	public void onRESTResult(int code, String result) {
		Activity activity=getActivity();
		if(code==200 && result!=null){
			Log.d(TAG, result);
			Toast.makeText(
					activity,
					"Successfully registered !",
					Toast.LENGTH_SHORT).show();
		}else{
			if(activity != null){
				Log.d(TAG, result);
				Toast.makeText(
						activity,
						"Failed to register. Check your Internet settings !",
						Toast.LENGTH_SHORT).show();
			}
		}
		
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	
	private void registerUser(){
		Activity activity=getActivity();
		Intent intent=new Intent(activity,RESTService.class);
		intent.setData(Uri.parse(REGISTER_URI));
		
		Bundle params=new Bundle();
		String payload=new Gson().toJson(user,Participant.class);
		params.putString(RESTService.KEY_JSON_PLAYLOAD, payload);
		intent.putExtra(RESTService.EXTRA_HTTP_VERB, RESTService.POST);
		intent.putExtra(RESTService.EXTRA_PARAMS, params);
		intent.putExtra(RESTService.EXTRA_RESULT_RECEIVER, getResultReceiver());
		
		activity.startActivity(intent);
	}
	
	
	public void onRegisterUser() {
		
			user= new Participant();
			user.setFirstname(et_firstname.getText().toString());
			user.setLastname(et_lastname.getText().toString());
			user.setEmail(et_email.getText().toString());
			user.setBiography(et_biography.getText().toString());
			user.setCity(et_city.getText().toString());
			user.setCountry(et_country.getText().toString());
			user.setCompany(et_company.getText().toString());
			//user.setPhoto(person.getImage().getUrl());
			user.setWebsite(et_website.getText().toString());
			user.setPassword(et_password.getText().toString());
			user.setPhone(et_phone.getText().toString());
			
			registerUser();		
					
	}
	
	
}
