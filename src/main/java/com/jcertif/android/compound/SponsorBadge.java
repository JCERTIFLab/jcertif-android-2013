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
package com.jcertif.android.compound;

import com.jcertif.android.model.Sponsor;

import android.app.Activity;
import android.content.Context;
import android.widget.LinearLayout;

public class SponsorBadge extends LinearLayout{

	private Sponsor sponsor;
	private Activity activity;
	
	public SponsorBadge(Context context) {
		super(context);		
	}

	public SponsorBadge(Activity activity,Sponsor sponsor) {
		this(activity);
		this.sponsor = sponsor;
		this.activity = activity;
	}


}
