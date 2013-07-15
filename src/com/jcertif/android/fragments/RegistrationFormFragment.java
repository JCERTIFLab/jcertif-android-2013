package com.jcertif.android.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.actionbarsherlock.app.SherlockDialogFragment;
import com.jcertif.android.JcertifApplication;
import com.jcertif.android.R;
import com.jcertif.android.model.Participant;

public class RegistrationFormFragment extends SherlockDialogFragment implements OnClickListener{

	private EditText et_email,et_password,et_city, et_country, et_company,
		et_phone, et_website, et_lastname, et_firstname, et_biography;
	private Button bt_cancel, bt_register;
	private Participant user;

	private static final int REQUEST_CODE_RESOLVE_ERR = 9000;
	private static final String TAG="Registration Form Fragment";
	private static final String REGISTER_URI=JcertifApplication.BASE_URL+"/particpant/register";
	
	private OnUserDialogReturns listener;
	
	
	public RegistrationFormFragment() {
		super();
	}


	
	public void register(OnUserDialogReturns listener) {
		this.listener=listener;
	}

	public void unregister() {
		this.listener=null;
	}

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
		bt_cancel=(Button)rootView.findViewById(R.id.bt_cancel);
		bt_register=(Button)rootView.findViewById(R.id.bt_register);
		bt_cancel.setOnClickListener(this);
		bt_register.setOnClickListener(this);
	    getDialog().setTitle("Registration");
		
		return rootView;
	}
	
	
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.bt_cancel){
			dismiss();
			
		}
		else if(v.getId()==R.id.bt_register){					
			onRegisterUser();				
		}
		
	}
	
	
	@Override
	public void onDestroy() {
		super.onDestroy();
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
			
			  
	           listener.onUserEdited(user);
	           this.dismiss();
	}
	
	public interface OnUserDialogReturns{
		
		public void onUserEdited(Participant p);
	}
	
}
