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
