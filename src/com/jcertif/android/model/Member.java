package com.jcertif.android.model;




public abstract class Member extends JCertifObject {
	
	
	private String email;
    private String password;
    private String title;	
    private String lastname;
    private String firstname;
    private String website;  
    private String city;
    private String country;
    private String company;
    private String phone;
    private String photo;
    private String biography;

    public Member() {
		super();
	}

	public Member(String email, String password, String title, String lastname,
			String firstname, String website, String city, String country,
			String company, String phone, String photo, String biography,int version, boolean deleted) {
		super(version,deleted);
		this.email = email;
		this.password = password;
		this.title = title;
		this.lastname = lastname;
		this.firstname = firstname;
		this.website = website;
		this.city = city;
		this.country = country;
		this.company = company;
		this.phone = phone;
		this.photo = photo;
		this.biography = biography;
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

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
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

	public String getBiography() {
		return biography;
	}

	public void setBiography(String biography) {
		this.biography = biography;
	}

    
}