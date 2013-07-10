package com.jcertif.android.model;

public class User {

	private String firstName;
	private String lastName;
	private String imgUrl;

	public User(String firstName, String lastName, String imgUrl) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.imgUrl = imgUrl;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

}
