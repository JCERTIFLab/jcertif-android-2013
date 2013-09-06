package com.jcertif.android.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.jcertif.android.service.DataUpdaterService;

/**
 * 
 * @author bashizip
 * 
 */
public class UpdaterBroadcast extends BroadcastReceiver {

	private Context ctx;

	public UpdaterBroadcast(Context ctx) {
		super();
		this.ctx = ctx;
	}

	public UpdaterBroadcast() {
		super();
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		this.ctx = context;
		ctx.startService(new Intent(ctx, DataUpdaterService.class));

		Toast.makeText(ctx, "JCertif : Mise à jour des données...",
				Toast.LENGTH_SHORT).show();

	}

}
