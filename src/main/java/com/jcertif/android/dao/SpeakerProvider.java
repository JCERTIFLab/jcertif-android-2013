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
package com.jcertif.android.dao;

import java.util.List;

import android.content.Context;

import com.jcertif.android.model.Session;
import com.jcertif.android.model.Speaker;

public class SpeakerProvider extends JCertifDb4oHelper<Speaker> {

	public SpeakerProvider(Context ctx) {
		super(ctx);
	}

	
	public Speaker getByEmail(final String email) {
		List<Speaker> list =getAll(Speaker.class);
		if (!list.isEmpty())
		for(Speaker s: list){
			if(s.getEmail().equals(email)){
				return s;
			}
		}
			return new Speaker();
	}




	
}
