package com.jcertif.android.model;

public class Category extends JCertifObject{

	private String label;

	public Category(String label) {
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
