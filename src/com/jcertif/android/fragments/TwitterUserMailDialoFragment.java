package com.jcertif.android.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockDialogFragment;
import com.jcertif.android.R;

public class TwitterUserMailDialoFragment extends SherlockDialogFragment {

	private TextView email;
	private View view;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new Builder(getActivity());

		LayoutInflater inflater = getActivity().getLayoutInflater();
		view = inflater.inflate(R.layout.twitter_user_mail_set, null);

		email = (TextView) view.findViewById(R.id.email);

		builder.setView(view).setPositiveButton("Ajouter", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent data = getActivity().getIntent();
				data.putExtra("email", email.getText().toString().trim());
				getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, data);
			}
		}).setNegativeButton("Annuller", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				TwitterUserMailDialoFragment.this.getDialog().cancel();
			}
		});

		return builder.create();
	}
}
