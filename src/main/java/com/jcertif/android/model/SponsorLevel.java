package com.jcertif.android.model;

/**
 * 
 * @author Patrick Bashizi
 *
 */
public class SponsorLevel extends JCertifObject {

	private String label;

	public SponsorLevel(String label) {
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
