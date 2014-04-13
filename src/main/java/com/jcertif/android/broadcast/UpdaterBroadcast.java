/*
 * Copyright 2013 JCertifLab.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
