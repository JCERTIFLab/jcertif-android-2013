package com.jcertif.android.model;

public class SessionStatus extends JCertifObject{

	private String label;

	public SessionStatus(String label) {
		super();
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	
	
}
