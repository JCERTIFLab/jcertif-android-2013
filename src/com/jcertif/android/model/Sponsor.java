package com.jcertif.android.model;


public class Sponsor extends Member {

	
    private String name;
    private String logo;	
    private String level;	
    private String about;
    
	public Sponsor(String name, String logo, String level, String about) {
		super();
		this.name = name;
		this.logo = logo;
		this.level = level;
		this.about = about;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getAbout() {
		return about;
	}
	public void setAbout(String about) {
		this.about = about;
	}
    
    
    
}