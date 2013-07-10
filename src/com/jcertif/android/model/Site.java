package com.jcertif.android.model;


public class Site extends JCertifObject {


	private String id;	
    private String name;	
    private String street;	
    private String city;
    private String country;
    private String contact;
    private String website;
    private String description;
    private String photo;
    
	public Site(String id, String name, String street, String city,
			String country, String contact, String website, String description,
			String photo) {
		super();
		this.id = id;
		this.name = name;
		this.street = street;
		this.city = city;
		this.country = country;
		this.contact = contact;
		this.website = website;
		this.description = description;
		this.photo = photo;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
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

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}
    
   

}
