package com.jcertif.android.model;




public class Speaker extends JCertifObject {

	
	private String email;	
	private String password;
	private String title;
	private String firstname;
	private String lastname;
	private String website;
	private String country;
	private String city;
	private String company;
	private String phone;
	private String photo;
	private String biographie;

	public Speaker() {
	}

	public Speaker(String email, String password, String title,
			String firstname, String lastname, String website, String country,
			String city, String company, String phone, String photo,
			String biographie, int version, boolean deleted) {
		super(version, deleted);
		this.email = email;
		this.password = password;
		this.title = title;
		this.firstname = firstname;
		this.lastname = lastname;
		this.website = website;
		this.country = country;
		this.city = city;
		this.company = company;
		this.phone = phone;
		this.photo = photo;
		this.biographie = biographie;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getBiographie() {
		return biographie;
	}

	public void setBiographie(String biographie) {
		this.biographie = biographie;
	}



}
