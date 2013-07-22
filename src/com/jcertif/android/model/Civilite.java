package com.jcertif.android.model;

public class Civilite extends JCertifObject {
	private String label;

	public Civilite(String label) {
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
