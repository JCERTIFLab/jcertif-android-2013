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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.db4o.query.Predicate;
import com.jcertif.android.model.Category;
import com.jcertif.android.model.Session;

import android.content.Context;

public class SessionProvider extends JCertifDb4oHelper<Session> {

	public SessionProvider(Context ctx) {
		super(ctx);
	}

	public List<Session> getSessionsByCategory(final String categorie) {
		List<Session> list = new ArrayList<Session>();
		list = db().query(new Predicate<Session>() {
			@Override
			public boolean match(Session test) {
				return Arrays.asList(test.getCategory()).contains(categorie);
			}
		});
		return list;

	}

	public List<Session> getAllSessionOfSpeaker(final String speakerId) {
		List<Session> list = new ArrayList<Session>();
		List<Session>	test = getAll(Session.class);
		
		for(Session s: test){
			if(Arrays.asList(s.getSpeakers()).contains(speakerId)){
				list.add(s);
			}
		}
		return list;
	}
	

}