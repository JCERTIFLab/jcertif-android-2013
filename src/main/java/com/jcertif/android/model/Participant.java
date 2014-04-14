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
package com.jcertif.android.model;

/**
 * 
 * @author bashizip
 *
 */


public class Participant extends Member {	
	private String[] sessions;

	public Participant(String[] sessions) {
		super();
		this.sessions = sessions;
	}

	public Participant() {
		super();
	}

	public String[] getSessions() {
		return sessions;
	}

	public void setSessions(String[] sessions) {
		this.sessions = sessions;
	}

}
